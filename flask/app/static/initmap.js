var CHI = new google.maps.LatLng(41.877009, -87.630047);
caricon = 'http://maps.google.com/mapfiles/kml/pal2/icon47.png';

function initialize() {
  var mapOptions = { zoom: 14,
                     center: CHI,
                     streetViewControl: false };
  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

  var marker = new google.maps.Marker({position: CHI,
					 zIndex:14,
					 icon: caricon,
					 title: "Spot!"});
  marker.setMap(map);
}

google.maps.event.addDomListener(window, 'load', initialize);
