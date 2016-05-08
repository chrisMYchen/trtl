function displayNotes(){
  var time = Date.now();
  var radius = 100;
  initialLoad(time, radius);

  $(window).on("scroll", {time: time, radius: radius}, scrollCallback);

  $("body").on("click", ".post-delete", deletePost);
  $("body").on("click", ".expand", expandPost);
  $("body").on("click", ".collapse", collapsePost);
}

function initialLoad(time, radius){
  var counter = 0;
  var intervalID = window.setInterval(function(){
    if(locationInfo.pos == null){
      counter++;
    }
    else if(counter > 20){
      window.clearInterval(intervalID);
    }
    else{
      window.clearInterval(intervalID);
      getNotes(time, radius);
      update(time, radius);
    }
  }, 2000);
}

function scrollCallback(event){
  var scrollPos = $("#posts > div").height() - $(window).scrollTop();
  var threshold = $(window).height() + 50;
  if(scrollPos < threshold){
    console.log(scrollPos);
    var time = event.data.time;
    var radius = event.data.radius;
    getNotes(time, radius);
  }
}

function getNotes(time, radius){
  $(window).off("scroll", scrollCallback);
  var range = getRange();
  var req = {
    userID: userInfo.id,
    lat: locationInfo.pos.lat,
    lon: locationInfo.pos.lon,
    timestamp: time,
    minPost: range.min,
    maxPost: range.max,
    radius: radius,
    filter: 0
  }

  if(userInfo != null){
    req.userID = userInfo.id;
  }

  $.post("/getNotes", req, function(data){
    var res = JSON.parse(data);
    console.log(req);
    console.log(res);
    if(res.error == "no-error"){
      console.log(res.notes);
      notesDOM(res.notes, range.min);
      setupScrollHandler(time, radius);
    }
    else{
      displayError(res.error);
      setupScrollHandler(time, radius);
    }
  }).fail(function(){
    setupScrollHandler(time, radius);
  });
}

function setupScrollHandler(time, radius){
  $(window).on("scroll", {time: time, radius: radius}, scrollCallback);
}

function getRange(){
  var last = -1;
  var post = $(".post").last();
  if(post.length > 0){
    last = parseInt(post.attr("data-order"), 10);
    if(last === undefined){
      last = 0;
    }
  }
  var range = {min: last + 1, max: last + 11};
  return range;
}

function notesDOM(notes, start){
  var posts = $("#posts").get(0);
   for(var i = 0; i < notes.length; i++){
       var note = notes[i];
       note.order = start + i;
       var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);
       salvattore.appendElements(posts, [dom.get(0)]);
       note.dom = dom;
       processNote(note);
   }

   /* Check to see if the notes go off the page */
}

function processNote(note){
  var compiledNote = {
    id: note.id,
    content: note.text,
    dom: note.dom,
    time: new Date(note.timestamp),
    order: note.order,
    image: note.image,
    marker: addMarker(note.latlng)
  };

  if((note.user) && (note.user.id) && (note.user.id != -1)){
    var userReq = {userID: note.user.id};
    $.post("/getUser", userReq, function(data){
      var res = JSON.parse(data);
      if(res.error == "no-error"){
        compiledNote.user = {userid: note.user.id, username: res.username};
        formatNote(compiledNote);
      }
      else{
        formatNote(compiledNote);
      }
    });
  }
  else{
    formatNote(compiledNote);
  }
}

function formatNote(note){
  /* Container */
  var dom = note.dom;
  dom.attr("data-noteid", note.id);

  /* Top */
  var top = $("<div></div>").attr("class", "post-top");
  dom.append(top);

  /* Content */
  var content = $("<div></div>").attr("class","post-content").append(note.content);
  content.linkify();
  dom.append(content);

  /* Collapse content */
  if(note.content.length > 1000){
    content.addClass("collapsed");
    var expand = $("<div></div>").addClass("expand-button").addClass("expand").html("expand");
    dom.append(expand);
  }

  /* Meta */
  var timestring = formatTime(note.time);
  var meta = $("<div></div>").attr("class","post-meta");
  var time = $("<div></div>").attr("class","post-time").append(timestring);
  meta.append(time);
  dom.append(meta);

  /* User */
  var user = $("<div></div>").attr("class","post-user");
  top.append(user);
  if(note.user){
    var handle = $("<p></p>").attr("class","post-handle").append("@" + note.user.username);
    user.append(handle);
    if(note.user.userid == userInfo.id){
      var del = $("<div></div>").addClass("post-delete").html("Delete");
      meta.append(del);
    }
  }
  else{
    var icon = $("<i></i>").addClass("material-icons").html("account_circle");
    var anon = $("<div></div>").attr("class","anon").append("Anonymous");
    user.append(icon).append(anon);
  }

  /* Image */
  if(note.image != null){
    var image = $("<img></img>").attr("class","post-image");
    image.attr("src", note.image);
    dom.after(content,image);
  }
}

function formatTime(time){
  var dateOptions = {
      weekday: 'short',
      day: "numeric",
      month: "short",
  }
  var timeOptions = {
    hour: "2-digit",
    minute: "2-digit",
  };
  var timestring = time.toLocaleTimeString('en-US', timeOptions);
  var datestring = time.toLocaleDateString('en-US', dateOptions);
  return timestring + " " + datestring;
}

function deletePost(){
  var parent = $(this).closest(".post");
  var noteid = parent.attr("data-noteid");
  var req = {noteID: noteid, userID: userInfo.id};
  console.log(req);
  $.post("/removeNote", req, function(data){
    var res = JSON.parse(data);
    if(res.error == "no-error"){
      parent.fadeOut(1000, function(){
        parent.remove();
      });
    }
    else{
      var msg = "This post could not be deleted."
      console.log(res.error);
      displayError(msg);
    }
  });
}

function expandPost(e){
  var button = $(this);
  
  var content = button.siblings(".post-content");
  var origHeight = content.get(0).scrollHeight;
  
  content.animate({height: origHeight}, 1000, function(){
    button.html("Collapse");
    button.toggleClass("expand", false);
    button.toggleClass("collapse", true);
    content.toggleClass("expanded", true);
    content.toggleClass("collapsed", false);
  });
}

function collapsePost(e){
  var button = $(this);
  
  var content = $(this).siblings(".post-content");
  content.animate({height: "300px"}, 1000, function(){
    button.html("Expand");
    content.toggleClass("expanded", false);
    content.toggleClass("collapsed", true);
    button.toggleClass("expand", true);
    button.toggleClass("collapse", false);
  });
  
}

function displayError(message){
  var error = $("<div></div>").attr("class", "post error").html(message);
  $("#posts").prepend(error);
  error.delay(7000).fadeOut();
}
