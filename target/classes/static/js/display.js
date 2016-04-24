var notes = [
    {"content": "First post. Hope you like it!", "handle": "cpax", "userreal": "Christina Paxson", "time": "April 8th"},
    {"content": "Hey! Hope you are enjoying the library! It seems like I spend all my time here sometimes.", "handle": "josiah", "userreal": "Josiah Carberry", "time": "April 9th"},
    {"content": "All nighter, yet again!", "handle": "kliu", "userreal": "Kate Liu", "time": "April 10th"},
    {"content": "You should check out the northeast corner of the fifth floor!", "handle": "cchen", "userreal": "Chris Chen", "time": "April 11th"},
    {"content": "I hate this place! You should go to the rock instead", "handle": "hkaul", "userreal": "Hemang Kaul", "time": "April 11th"}
];

function displayNotes(){
  window.setTimeout(displayCallback, 1000);
}

function displayCallback(data){
  var range = {min: 0, max: 10};
  getNotes(range, locationInfo.pos, Date.now(), 10);
}

function notesDOM(notes, start){
   for(var i = 0; i < notes.length; i++){
       var note = notes[i];
       note.order = start + i;
       var dom = formatNote(note);
       $("#posts").append(dom);
   }
}

function formatNote(note){
  var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);

  /* User */
  var user = $("<div></div>").attr("class","post-user");
  var userrn = $("<div></div>").attr("class","post-realname").append(note.userreal);
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
      notesDom(res.notes, range.min);
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
