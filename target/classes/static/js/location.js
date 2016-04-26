function locationStart(){
  var loc_options = {
    enableHighAccuracy: true,
    timeout: 27000,
    maximumAge: 30000
  }

  if(navigator.geolocation){
    var id = navigator.geolocation.watchPosition(
      function(data){
        locationHandler({lat: data.coords.latitude, lng: data.coords.longitude});
      },
      function(error) {
        locationError(error, id);
      }, loc_options);
  } else{
    $("#location").html("Location not available");
  }
}

function locationError(error, id){
    navigator.geolocation.clearWatch(id);
    var icon = $("<i></i>").addClass("material-icons").html("location_off");
    $("#location").empty().append(icon);
    $("#location .dropdown").html(error.message);
}

function locationHandler(position){
  var icon = $("<i></i>").addClass("material-icons").html("location_on");
  $("#location").empty().append(icon)

  /* Create dropdown */
  locationInfo.pos = {lat: position.lat, lon: position.lng};
  var div = $("#location .dropdown");
  var lat = $("<p></p>").html("<b>lat:</b> &nbsp;&nbsp;"+ position.lat.toFixed(4));
  var lng = $("<p></p>").html("<b>lng:</b> &nbsp;&nbsp;"+ position.lng.toFixed(4));
  div.append(lat).append(lng);

  /* Get parent */
  var locDiv = $("#location");
  var locOffset = locDiv.position();
  var left = locOffset.left - Math.floor(div.width() / 2);

  /*Position dropdown */
  var position = {top: locOffset.top + locDiv.height(), left: left};
  div.offset(position);

  div.toggle(false);
  locDiv.click(function(){
    div.toggle();
  })
}
