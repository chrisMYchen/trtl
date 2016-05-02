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

    var elem = $("<p></p>").html(error.message);
    locationDropdown(elem);
}

function locationHandler(position){
  var icon = $("<i></i>").addClass("material-icons").html("location_on");
  $("#location").empty().append(icon)

  /* Create dropdown */
  locationInfo.pos = {lat: position.lat, lon: position.lng};
  var lat = $("<p></p>").html("<span class='label'>lat:</span>"+ position.lat.toFixed(4));
  var lng = $("<p></p>").html("<span class='label'>lng:</span>"+ position.lng.toFixed(4));
  var wrapper = $("<p></p>").addClass("latlon").append(lat).append(lng);

  locationDropdown(wrapper);
}

function locationDropdown(elem){
  console.log(elem)
  var div = $("<div></div>").addClass("dropdown");
  div.append(elem);

  /* Get parent */
  var locDiv = $("#location");

  $("#location").append(div);
  div.toggle(false);
}
