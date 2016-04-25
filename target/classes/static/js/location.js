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
    $("#location").html(error.message);
}

function locationHandler(position){
  locationInfo.pos = {lat: position.lat, lon: position.lng};

  $("#location").html("Your location is: " + position.lat + ", " + position.lng);
}
