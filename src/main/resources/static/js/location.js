function locationStart(){
  var loc_options = {
    enableHighAccuracy: true,
    timeout: 30000,
    maximumAge: 10000
  }

  if(navigator.geolocation){
    var id = navigator.geolocation.getCurrentPosition(
      function(data){
        locationHandler({lat: data.coords.latitude, lng: data.coords.longitude});
      },
      function(error) {
        locationError(error, id);
      }, loc_options);
  } else{
    $("#location").html("Location not supported");
  }
}

function locationError(error, id){
    navigator.geolocation.clearWatch(id);

    var icon = locationIcon();
    icon.html("location_off");

    var elem = $("<p></p>").html(error.message);
    locationDropdown(elem);
}

function locationHandler(position){
  var icon = locationIcon();
  icon.html("location_on");

  /* Create dropdown */
  locationInfo.pos = {lat: position.lat, lon: position.lng};
  var lat = $("<p></p>").html("<span class='label'>lat:</span>"+ position.lat.toFixed(4));
  var lng = $("<p></p>").html("<span class='label'>lng:</span>"+ position.lng.toFixed(4));
  var wrapper = $("<p></p>").addClass("latlon").append(lat).append(lng);

  locationDropdown(wrapper);
}

function locationIcon(){
  var icon = $("#location i");
  if(icon.length == 0){
      icon = $("<i></i>").addClass("material-icons");
      $("#location").prepend(icon);
  }
  return icon;
}

function locationDropdown(elem){
  var drop = $("#location .dropdown");
  if(drop.length == 0){
    drop = $("<div></div>").addClass("dropdown");
    $("#location").append(drop);
    drop.toggle(false);
  }
  drop.html(elem);
}

function distance(){

}
