var marker;
car = 'http://maps.google.com/mapfiles/kml/pal2/icon47.png';
function initMap() {
    var pos = new google.maps.LatLng(lat, lng);
    var mapOptions = { zoom: 17,
		       center: pos,
		       streetViewControl: false };
    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    marker = new google.maps.Marker({postition: pos,
				     zIndex: 17,
				     icon: car,
				     title:"car"});
    marker.setMap(map);
    
    var top = Number(lat) + 0.00125;
    var bottom = Number(lat) - 0.00125;
    var left = Number(lng) - 0.00125;
    var right = Number(lng) + 0.00125;
    
    var rec = new google.maps.Rectangle({
	strokeColor: color,
	strokeOpacity: 0.8,
	strokeWeight: 2,
	fillColor: color,
	fillOpacity: 0.35,
	map:map,
	bounds: {
	    north: top,
	    south: bottom,
	    east: right,
	    west: left
	}
    });

    rec.addListener('click', showData);
    infoWindow = new google.maps.InfoWindow();
    function showData(event) {
	var ne = rec.getBounds().getNorthEast();
	var contentString = '<b>Compare to average</b><br>' +
            'Average: ' + avg + '<br>' + 'Count: ' +  count;

	infoWindow.setContent(contentString)
	infoWindow.setPosition(ne);
	infoWindow.open(map);
    }
    
}


//Load the map when the page has finished loading.
google.maps.event.addDomListener(window, 'load', initMap);
