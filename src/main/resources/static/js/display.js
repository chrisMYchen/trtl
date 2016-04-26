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
    var range = getRange();
    getNotes(range, locationInfo.pos, time, 5000);
  }
}

function getRange(){
  var last = 0;
  var post = $(".post").last();
  if(post.length > 0){
    last = parseInt(post.attr("data-order"), 10);
    if(last === undefined){
      last = 0;
    }
  }
  var range = {min: last, max: last + 10};
  return range;
}

function notesDOM(notes, start){
  console.log(notes);
   for(var i = 0; i < notes.length; i++){
       var note = notes[i];
       note.order = start + i;
       var compiledNote = processNote(note);
       var dom = formatNote(compiledNote);
       $("#posts").append(dom);
   }
}

function processNote(note){
  var user = {userid: note.userid}

  var compiledNote = {
    content:note.text,
    user:user,
    time: new Date(note.timestamp),
    order: note.order
  };
  return compiledNote;
}

function formatNote(note){
  var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);

  /* User */
  var user = $("<div></div>").attr("class","post-user");
  var userrn = $("<div></div>").attr("class","post-realname").append(note.fullname);
  var handle = $("<a></a>").attr("class","post-handle").attr("href","/user/" + note.handle).append("@" + note.handle);;
  user.append(userrn).append(handle);

  /* Content */
  var content = $("<div></div>").attr("class","post-content").append(note.content);

  /* Meta */
  var timestring = formatTime(note.time);
  var meta = $("<div></div>").attr("class","post-meta");
  var time = $("<div></div>").attr("class","post-time").append(timestring);
  var share = $("<a></a>").attr("class","post-share").append("Share");
  meta.append(time).append(share);

  dom.append(user).append(content).append(meta);
  return dom;
}

function formatTime(time){
  var date = time.toDateString();
  var hours = time.getHours();
  var minutes = time.getMinutes();
  var timestring = hours + ":" + minutes + " " + date;
  return timestring;
}

function getNotes(range, location, timestamp, radius){
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
  console.log(req);

  $.post("/getNotes", req, function(data){
    var res = JSON.parse(data);
    console.log(res);
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
