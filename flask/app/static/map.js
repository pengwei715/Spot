var CHI = new google.maps.LatLng(41.877009, -87.630047);
var map;
var marker = false;

caricon = 'http://maps.google.com/mapfiles/kml/pal2/icon47.png';

function initMap() {
    var mapOptions = { zoom: 11,
		       center: CHI,
		       streetViewControl: false};
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    
    google.maps.event.addListener(map, 'click', function(event) {                
        //Get the location that the user clicked.
        var clickedLocation = event.latLng;
        //If the marker hasn't been added.
        if(marker === false){
            //Create the marker.
            marker = new google.maps.Marker({
                position: clickedLocation,
                map: map,
                draggable: true //make it draggable
            });
            //Listen for drag events!
            google.maps.event.addListener(marker, 'dragend', function(event){
                markerLocation();
            });
        } else{
            //Marker has already been added, so just change its location.
            marker.setPosition(clickedLocation);
        }
        //Get the marker's location.
        markerLocation();
    });
}
        
//This function will get the marker's current location and then add the lat/long
//values to our textfields so that we can save the location.
function markerLocation(){
    //Get location.
    var currentLocation = marker.getPosition();
    //Add lat and lng values to a field that we can save.
    document.getElementById('lat').value = currentLocation.lat(); //latitude
    document.getElementById('lng').value = currentLocation.lng(); //longitude
}

function updateBlock() {
    $.getJSON('/query',
	      function(data) {
		  
	      }
    

}
        
//Load the map when the page has finished loading.
google.maps.event.addDomListener(window, 'load', initMap);
