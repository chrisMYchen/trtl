var notes = [
    {"content": "First post. Hope you like it!", "handle": "cpax", "userreal": "Christina Paxson", "time": "April 8th"},
    {"content": "Hey! Hope you are enjoying the library! It seems like I spend all my time here sometimes.", "handle": "josiah", "userreal": "Josiah Carberry", "time": "April 9th"},
    {"content": "All nighter, yet again!", "handle": "kliu", "userreal": "Kate Liu", "time": "April 10th"},
    {"content": "You should check out the northeast corner of the fifth floor!", "handle": "cchen", "userreal": "Chris Chen", "time": "April 11th"},
    {"content": "I hate this place! You should go to the rock instead", "handle": "hkaul", "userreal": "Hemang Kaul", "time": "April 11th"}
];

function displayNotes(){
  notesDOM(notes);

}

function notesDOM(notes){
   for(var i = 0; i < posts.length; i++){
       var post = posts[i];
       var dom = formatPost(post);
       $("#posts").append(dom);
   }
}

function formatNote(note){
  var dom = $("<div></div>").attr("class","post");

  /* User */
  var user = $("<div></div>").attr("class","post-user");
  var userrn = $("<div></div>").attr("class","post-realname").append(post.userreal);
  var handle = $("<a></a>").attr("class","post-handle").attr("href","/user/" + post.handle).append("@" + post.handle);;
  user.append(userrn).append(handle);

  /* Content */
  var content = $("<div></div>").attr("class","post-content").append(post.content);

  /* Meta */
  var meta = $("<div></div>").attr("class","post-meta");
  var time = $("<div></div>").attr("class","post-time").append(post.time);
  var share = $("<a></a>").attr("class","post-share").append("Share");
  meta.append(time).append(share);

  dom.append(user).append(content).append(meta);
  return dom;
}

function getNotes(range, location, timestamp, radius){
  var newMax = range.min;
  var req = {
    userID: null,
    lat: location.lat,
    lon: location.lng,
    timestamp: timestamp,
    minPost: range.min,
    maxPost: range.max,
    radius: radius
  }

  $.post("/getNotes", reg, function(data){
    if(data.error == ""){
      notesDom(data.notes);
      newMax = range.max;
    }
    else{
      displayError(data.error);
      newMax = range.min;
    }
  });
  return newMax;
}
