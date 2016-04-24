function postNotes(){
<<<<<<< HEAD
  $(".input-submit").click(postHandler);
}

function postHandler(data){
  data.preventDefault();
  console.log(data);
  var post = $(".input-content").val();
  postNote(post);
}

function postNote(post){
  var noteInfo = {
    userID: userInfo.id,
    lat: locationInfo.pos.lat,
    lon: locationInfo.pos.lon,
    timestamp: Date.now(),
    text: post,
  }

  console.log(noteInfo);

  $.post("/postNote", noteInfo, function(res){
    var response = JSON.parse(res);
    console.log(response);
    if(response.error != "no-error"){
      postError(response.error);
    } else{
      $(".input-content").val("");
    }
  })
=======
  $(".input").submit(function(data){
    data.preventDefault();
    console.log(data);
  });
>>>>>>> chriskatie
}

function postError(message){
  var error = $("<div></div>").attr("class", "post error").html(message);
  $("#posts").prepend(error);
}
