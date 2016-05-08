
function update(time, radius){
  update_info.time = time;
  update_info.interval_id = window.setTimeout(function(){
    updateNotes(time, radius);
  }, 5000);
}

function updatesDOM(notes, start){
  var posts = $("#posts").get(0);

  for(var i = notes.length - 1; i >= 0; i--){
  /*  var height_before = $("#updates").height();
    var position_before = $(window).scrollTop();*/
    var note = notes[i];
    note.order = start + i;
    var dom = $("<div></div>").attr("class","post").attr("data-order", note.order);
    salvattore.prependElements(posts, [dom[0]]);
    note.dom = dom;
    processNote(note);
    /* Scrolling
    if(position_before > 20){
      var height_after = $("#updates").height();
      var difference = height_after - height_before;
      $(window).scrollTop(position_before + difference);
    } */
  }
}

function updateNotes(radius){
  var start_time = update_info.time;
  var range = {min: 0, max: 10};
  var req = {
    userID: userInfo.id,
    lat: locationInfo.pos.lat,
    lon: locationInfo.pos.lon,
    start_time: start_time,
    end_time: Date.now(),
    minPost: range.min,
    maxPost: range.max,
    radius: radius,
    filter: filter_setting
  }
  if(userInfo != null){
    req.userID = userInfo.id;
  }

  $.post("/updateNotes", req, function(data){
    var res = JSON.parse(data);
    if(res.error == "no-error"){
      console.log(res.notes);
      updatesDOM(res.notes, range.min);
      setupUpdateHandlers(req.end_time, radius);
      update_info.time = req.end_time;
    }
    else{
      displayError(res.error);
      setupUpdateHandlers(req.start_time, radius);
      update_info.time = req.start_time;
    }
  });
}

function setupUpdateHandlers(time, radius){
  update_interval_id = window.setTimeout(function(){
    updateNotes(time, radius);
  }, 5000);
}
