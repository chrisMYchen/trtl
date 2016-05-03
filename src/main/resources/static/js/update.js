function update(time, radius){
  window.setTimeout(function(){
    updateNotes(time, radius);
  }, 2000);
}

function updatesDOM(notes, start){

   for(var i = 0; i < notes.length; i++){
       var note = notes[i];
       note.order = start + i;
       var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);
       $("#updates").prepend(dom);
       note.dom = dom;
       processNote(note);
   }
}

function updateNotes(time, radius){
  var range = {min: 0, max: 10};
  var req = {
    userID: userInfo.id,
    lat: locationInfo.pos.lat,
    lon: locationInfo.pos.lon,
    timestamp: time,
    minPost: range.min,
    maxPost: range.max,
    radius: radius
  }
  if(userInfo != null){
    req.userID = userInfo.id;
  }

  $.post("/updateNotes", req, function(data){
    var res = JSON.parse(data);
    if(res.error == "no-error"){
      updatesDOM(res.notes, range.min);
      setupUpdateHandlers(Date.now(), radius);
    }
    else{
      displayError(res.error);
      setupUpdateHandlers(Date.now(), radius);
    }
  });
}

function setupUpdateHandlers(time, radius){
  window.setTimeout(function(){
    updateNotes(time, radius);
  }, 2000);
}
