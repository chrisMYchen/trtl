function imageSetup(){
  $("#image-upload").click(function(){
    if(userInfo.id != -1){
      $("#image-input").click();
    }
  });

  $("#image-upload").click(displayImageHover);

  $("#image-input").click(function(e){
    e.stopImmediatePropagation();
  });


  $(window).load(function(){
      clearImages();
      $("#image-input").trigger("change");
  });

  $("#image-input").on("change", imageControl);
}

function imageHandle(post, privacy){
  var file = $("#image-input")[0].files[0];
  var data = new FormData();
  data.append("userID", userInfo.id);
  data.append("lat", locationInfo.pos.lat);
  data.append("lon", locationInfo.pos.lon);
  data.append("timestamp", Date.now());
  data.append("text", post);
  data.append("private", privacy);
  data.append("pic", file);

  if(file.size > 10000000){
    postError("Image is too large! Max image size is 10MB.");
  }
  else{
    $.ajax({
      url: "postNoteImage",
      type: "POST",
      data: data,
      cache: false,
      contentType: false,
      processData: false,
      success: function(response){
        var res = JSON.parse(response);
        if(res.error != "no-error"){
          console.log(res.error);
          postError(res.error);
        }
        else{
          $(".input-content").val("");
          clearImages();
        }
      },
      error: function(err){
        postError("Posting failed.");
      }
    });
  }
}

function imageControl(){
  var files = this.files;
  $("#image-thumb").empty();
  $("#image-thumb").hide();

  if(files.length > 0){
    var clear = $("<div></div>").attr("id", "clear");
    var icon = $("<i></i>").addClass("material-icons").html("close");
    clear.append(icon);
    $("#image-thumb").append(clear);
    clear.click(clearImages);

    for(var i = 0; i < files.length; i++){
      var file = $("<div></div>").html(files[0].name);
      $("#image-thumb").append(file);
    }
    $("#image-thumb").show();
  }
}

function clearImages(){
  var elem = $("#image-input");
  elem.wrap("<form>").closest("form").get(0).reset();
  elem.unwrap();
  elem.trigger("change");
}

function displayImageHover(e){
var elem = $("<div></div>").addClass("hovermenu").html("Please signup or login to post images.");
  $("body").append(elem);
  elem.offset({top:e.pageY,left:e.pageX});
  elem.delay(1000).fadeOut();
  window.setTimeout(function(){elem.remove()}, 2000);
}
