function image(){
  $(".image form").submit(function(e){
    e.preventDefault();
    var file = $(".file-input")[0].files[0];
    var data = new FormData();
    data.append("userID", userInfo.id);
    data.append("lat", locationInfo.pos.lat);
    data.append("lon", locationInfo.pos.lon);
    data.append("timestamp", Date.now());
    data.append("text", "This is mesage!");
    data.append("private", 0);
    data.append("pic", file);

    $.ajax({
      url: $(this).attr("action"),
      type: "POST",
      data: data,
      cache: false,
      contentType: false,
      processData: false,
      success: function(res){
        console.log(res);},
    });
  });
}
