function displayNotes(){
  var time = Date.now();
  initialLoad(time);

  $(window).scroll(function(){
    displayCallback(time);
  });

  update(time);
}

function initialLoad(time){
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
      displayCallback(time);
    }
  }, 2000);
}

function update(time){
  /* Check for updates at interval, or use sockets */
}

function displayCallback(time){
  var scrollPos = $("#posts").height() - $(window).scrollTop();
  var threshold = $(window).height() + 50;
  if(scrollPos < threshold){
    getNotes(locationInfo.pos, time, 100);
  }
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
   for(var i = 0; i < notes.length; i++){
       var note = notes[i];
       note.order = start + i;
       var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);
       $("#posts").append(dom);
       note.dom = dom;
       processNote(note);
   }
}

function processNote(note){
  var compiledNote = {
    content: note.text,
    dom: note.dom,
    time: new Date(note.timestamp),
    order: note.order
  };

  if((note.user) && (note.user.id) && (note.user.id != -1)){
    var userReq = {userID: note.user.id};
    $.post("/getUser", userReq, function(data){
      var res = JSON.parse(data);
      if(res.error == "no-error"){
        compiledNote.user = {username: res.username};
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
  var dom = note.dom;
  /* User */
  var user = $("<div></div>").attr("class","post-user");
  if(note.user){
    /*var userrn = $("<div></div>").attr("class","post-realname").append(note.user.fullname);*/
    var handle = $("<a></a>").attr("class","post-handle").attr("href","/user/" + note.user.username).append("@" + note.user.username);
    user.append(handle);
  }
  else{
    var anon = $("<div></div>").attr("class","anon").append("Anonymous");
    user.append(anon);
  }

  /* Content */
  var content = $("<div></div>").attr("class","post-content").append(note.content);

  /* Meta */
  var timestring = formatTime(note.time);
  var meta = $("<div></div>").attr("class","post-meta");
  var time = $("<div></div>").attr("class","post-time").append(timestring);
  meta.append(time);

  dom.append(user).append(content).append(meta);
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

function getNotes(location, timestamp, radius){
  var range = getRange();
  var req = {
    userID: userInfo.id,
    lat: locationInfo.pos.lat,
    lon: locationInfo.pos.lon,
    timestamp: timestamp,
    minPost: range.min,
    maxPost: range.max,
    radius: radius
  }
  if(userInfo != null){
    req.userID = userInfo.id;
  }

  $.post("/getNotes", req, function(data){
    var res = JSON.parse(data);
    if(res.error == "no-error"){
      notesDOM(res.notes, range.min);
    }
    else{
      displayError(res.error);
    }
  });
}

function displayError(message){
  var error = $("<div></div>").attr("class", "post error").html(message);
  $("#posts").append(error);
}
