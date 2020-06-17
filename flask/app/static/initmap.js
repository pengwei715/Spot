var CHI = new google.maps.LatLng(41.877009, -87.630047);


function initialize() {
  var mapOptions = { zoom: 13,
                     center: CHI,
                     streetViewControl: false };
  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

  var marker = new google.maps.Marker({position: CHI,
                                       title:    "Spot!"});
  marker.setMap(map);
}


google.maps.event.addDomListener(window, 'load', initialize);
