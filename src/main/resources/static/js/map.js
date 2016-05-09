var mapObj = null;

function initMap(){

  $(window).load(mapSize);
  $(window).resize(mapSize);

  var counter = 0;
  var intervalID = window.setInterval(function(){
    if(locationInfo.pos == null){
      counter++;
    }
    else if(counter > 20){
      window.clearInterval(intervalID);
    }
    else{
      window.clearInterval(intervalID);
      mapSetup();
    }
  }, 2000);
}

function mapSetup(){

  var stylearray = [
    {
      featureType: "all",
      stylers: [
       { saturation: -80 }
      ]
    },{
      featureType: "road.arterial",
      elementType: "geometry",
      stylers: [
        { hue: "#00ffee" },
        { saturation: 50 }
      ]
    },{
      featureType: "poi.business",
      elementType: "labels",
      stylers: [
        { visibility: "off" }
      ]
    }
  ];

  var pos = {lat: locationInfo.pos.lat, lng: locationInfo.pos.lon};
  var bounds = mapBounds(pos, 100);
  var mapdom = $("#map").get(0);
  var map = new google.maps.Map(mapdom, {
    center: pos,
    scrollwheel: false,
    styles: stylearray,
    draggable:false,
    fullscreenControl: false,
    rotateControl: false,
    mapTypeControl: false,
    streetViewControl: false,
    zoomControl: false,
    zoom: 11,
    disableDefaultUI: true
  });
  map.fitBounds(bounds);

  $(window).resize(function(){
    window.setTimeout(function(){
      map.fitBounds(bounds);
    }, 100);
  });
  mapObj = map;

  var rectangle = new google.maps.Rectangle({
    strokeColor: '#1F98FF',
    strokeOpacity: 0.0,
    strokeWeight: 1,
    fillColor: '#1F98FF',
    fillOpacity: 0.15,
    map: map,
    bounds: bounds
  });

}

function mapBounds(pos, radius){
  var km = radius / 1000;
  var latDist = km/110.574;
  var lngDist = km/(111.32*Math.cos(pos.lat));
  var bounds = {
    north: pos.lat + latDist,
    south: pos.lat - latDist,
    east: pos.lng - lngDist,
    west: pos.lng + lngDist
  }

  return bounds;
}

function addMarker(pos){
  if(mapObj){
    return new google.maps.Marker({
      position: pos,
      map: mapObj,
      icon: {
        path:google.maps.SymbolPath.CIRCLE,
        scale: 2,
        fillColor: "#52981A",
        fillOpacity: 0.5,
        strokeColor: "#fff",
        strokeOpacity: 0.0
      }
    });
  } else{
    window.setTimeout(function(){
      addMarker(pos)
    }, 1000);
  }
}

function mapSize(){
  var width = $("#map-container").width();
  $("#map-container").height(width);
}
