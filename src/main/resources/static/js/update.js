function update(time, radius){
  window.setTimeout(function(){
    updateNotes(time, radius);
  }, 3000);
}

function updatesDOM(notes, start){

  for(var i = 0; i < notes.length; i++){
    var height_before = $("#updates").height();
    var position_before = $(window).scrollTop();
    var note = notes[i];
    note.order = start + i;
    var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);
    $("#updates").prepend(dom);
    note.dom = dom;
    processNote(note);
    if(position_before > 20){
      var height_after = $("#updates").height();
      var difference = height_after - height_before;
      $(window).scrollTop(position_before + difference);
    }
  }
  console.log(linkify.test('dev@example.com')); // true
  $('.post').linkify();
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
    radius: radius,
    filter: 2
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
  }, 3000);
}
