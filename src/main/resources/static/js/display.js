function displayNotes(){
  window.setTimeout(displayCallback, 3000);
}

function displayCallback(data){
  var range = {min: 0, max: 10};
  getNotes(range, locationInfo.pos, Date.now(), 10);
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
  var meta = $("<div></div>").attr("class","post-meta");
  var time = $("<div></div>").attr("class","post-time").append(note.time);
  var share = $("<a></a>").attr("class","post-share").append("Share");
  meta.append(time).append(share);

  dom.append(user).append(content).append(meta);
  return dom;
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
  console.log(req);

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
