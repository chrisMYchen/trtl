function postNotes(){
  $(".input-submit").click(postHandler);
}

function postHandler(data){
  data.preventDefault();
  var post = $(".input-content").val();
  var privacy = getPrivacy();

  if($("#image-input")[0].files.length > 0){
    imageHandle(post, privacy);
  }
  else if (post.length > 0){
    post = sanitizer.sanitizeHTML(post);
    if(post.length > 0){
      postNote(post, privacy);
    } else {
      postError("Your note had some content we didn't like. Please try again.");
    }
  }
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

  console.log(noteInfo);
  $.post("/postNote", noteInfo, function(res){
    var response = JSON.parse(res);
    if(response.error != "no-error"){
      postError(response.error);
    } else{
      $(".input-content").val("");
      window.clearInterval(update_info.interval_id);
      updateNotes(note_loc_radius);
    }
  });
}

function postError(message){
  var error = $("<div></div>").attr("class", "post error").html(message);
  $("#posts").prepend(error);
  error.delay(10000).fadeOut();
}

function getPrivacy(){
  var val = 0;
  if(userInfo.id != -1){
    var privacy = $("#input-privacy").val();
    if(privacy == "private"){
      val = 1;
    }
  }
  return val;
}
