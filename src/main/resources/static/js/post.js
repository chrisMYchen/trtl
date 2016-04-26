function postNotes(){
  $(".input-submit").click(postHandler);
}

function postHandler(data){
  data.preventDefault();
  console.log(data);
  var post = $(".input-content").val();
  var privacy = getPrivacy();
  postNote(post);
}

function postNote(post, privacy){
  var noteInfo = {
    userID: userInfo.id,
    lat: locationInfo.pos.lat,
    lon: locationInfo.pos.lon,
    timestamp: Date.now(),
    text: post,
    private: privacy
  }

  $.post("/postNote", noteInfo, function(res){
    var response = JSON.parse(res);
    console.log(response);
    if(response.error != "no-error"){
      postError(response.error);
    } else{
      $(".input-content").val("");
    }
  });
}

function postError(message){
  var error = $("<div></div>").attr("class", "post error").html(message);
  $("#posts").prepend(error);
}

function getPrivacy(){
  var val = 0;
  if(userInfo != null){
    var privacy = $("#input-privacy").val();
    if(privacy == "private"){
      val = 1;
    }
  }
  return val;
}