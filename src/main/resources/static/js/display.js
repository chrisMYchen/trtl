var filter_setting = {option: 0, username: null};
var note_loc_radius = 100;
var maxPost = 0;
var update_info = {time: Date.now()};

function displayNotes(){
  var time = Date.now();
  var radius = note_loc_radius;
  initialLoad(time, radius);

  $(window).on("scroll", {time: time, radius: radius}, scrollCallback);

  $("body").on("click", ".post-delete", deletePost);
  $("body").on("click", ".expand", expandPost);
  $("body").on("click", ".collapse", collapsePost);

  $(".filter-option").click(changeFilter);

  $("body").on("click",".post-handle",filterUser);
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

function resetNotes(){
  var time = Date.now();
  var radius = note_loc_radius;
  window.clearInterval(update_info.interval_id);
  $(".post").remove();
  maxPost = 0;
  getNotes(time, radius);
  update(time, radius);
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
    filter: filter_setting.option
  }

  if(userInfo != null){
    req.userID = userInfo.id;
  }

  if(filter_setting.username != null){
    req.username = filter_setting.username;
  }

  $.post("/getNotes", req, function(data){
    var res = JSON.parse(data);
    console.log(req);
    console.log(res);
    if(res.error == "no-error"){
      notesDOM(res.notes, range.min);
      setupScrollHandler(time, radius);
      if(res.notes.length > 0){
        scrollCallback({data: {time, radius}});
      }
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
  var range = {min: maxPost, max: maxPost + 10};
  return range;
}

function notesDOM(notes, start){
  var posts = $("#posts").get(0);
  maxPost += notes.length + 1;
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
    content: sanitizer.sanitizeHTML(note.text),
    dom: note.dom,
    time: new Date(note.timestamp),
    order: note.order,
    image: note.image,
    user: note.user,
    vote: note.vote,
    voteStatus: note.voteStatus
  };

  if(note.privacy == 1){
    compiledNote.private = true;
  } else{
    compiledNote.private = false;
  }

  formatNote(compiledNote);

  var marker = addMarker(note.latlng);

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
  if(note.content.length > 500){
    content.addClass("collapsed");
    var expand = $("<div></div>").addClass("expand-button").addClass("expand").html("expand");
    dom.append(expand);
  }

  /* Image */
  if(note.image != null){
    var image = $("<img></img>").attr("class","post-image");
    image.attr("src", note.image);
    dom.append(image);
  }

  /* Meta */
  var meta = $("<div></div>").attr("class","post-meta");
  var upvote_div = $("<div></div>").addClass("upvote");
  if(note.voteStatus){
    upvote_div.addClass("upvoted");
  }
  if(userInfo.id == -1){
    upvote_div.addClass("disabled");
  } else{
    upvote_div.addClass("enabled");
  }
  var upvote_icon = $("<i></div>").addClass("upvote-icon").html("u");
   upvote_div.append(upvote_icon);
  var upvote_count = $("<p></p>").addClass("upvote-count").html(note.vote);
  upvote_div.append(upvote_count);
  meta.append(upvote_div);

  var timestring = formatTime(note.time);
  var time = $("<div></div>").attr("class","post-time").append(timestring);
  meta.append(time);
  dom.append(meta);

  /* User */
  var user = $("<div></div>").attr("class","post-user");
  top.append(user);
  if(note.user == "anon"){
    user.addClass("anon");
    var icon = $("<i></i>").addClass("material-icons").html("account_circle");
    var anon = $("<div></div>").append("Anonymous");
    user.append(icon).append(anon);
  }
  else if(note.user){
    user.addClass("user");
    var icon = $("<i></i>").addClass("material-icons").html("account_circle");
    var handle = $("<p></p>").attr("class","post-handle").append("@" + note.user);
    handle.attr("data-username", note.user);
    user.append(icon).append(handle);
    if(note.user == userInfo.username){
      var del = $("<div></div>").addClass("post-delete").html("Delete");
      meta.append(del);
    }
  }
  else{
    user.addClass("user-hidden");
    var icon = $("<i></i>").addClass("material-icons").html("account_circle");
    var anon = $("<div></div>").append("HIDDEN");
    user.append(icon).append(anon);
  }

  /* Privacy */
  if(note.private){
    dom.addClass("private");
    var priv = $("<div></div>").addClass("post-privacy");
    var icon = $("<i></i>").addClass("material-icons").html("lock");
    var text = $("<div></div>").html("Private");
    priv.append(icon).append(text);
    top.append(priv);
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
  var origHeight = content.get(0).scrollHeight - 30;

  content.animate({height: origHeight}, 1000, function(){
    content.height("auto");
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
  content.animate({height: "200px"}, 1000, function(){
    button.html("Expand");
    content.toggleClass("expanded", false);
    content.toggleClass("collapsed", true);
    button.toggleClass("expand", true);
    button.toggleClass("collapse", false);
  });
}

function changeFilter(){
  $(".filter-option").toggleClass("active", false);
  $(this).toggleClass("active", true);
  filter_setting.option = $(this).attr("data-filter");
  filter_setting.username = null;
  $("#user-filter-display").hide();
  $("#user-filter-display").empty();
  resetNotes();
}

function filterUser(){
  var username = $(this).attr("data-username");
  if(username == userInfo.username){
    $("#mine.filter-option").click();
    $("#user-filter-display").hide();
    $("#user-filter-display").empty();
  } else{
    $(".filter-option").toggleClass("active", false);
    filter_setting.option = 3;
    filter_setting.username = username;
    resetNotes();

    var userDiv = $("<span></span>").html(username).addClass("name");
    $("#user-filter-display").html("Viewing notes from ").append(userDiv);
    $("#user-filter-display").show();
  }
}

function displayError(message){
  var error = $("<div></div>").attr("class", "post error").html(message);
  $("#posts").prepend(error);
  error.delay(7000).fadeOut();
}
