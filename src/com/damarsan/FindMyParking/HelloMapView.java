package com.damarsan.FindMyParking;

//import android.R;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.io.IOException;
import java.util.List;


public class HelloMapView extends MapActivity implements LocationListener
{
    private MapController mapController;
        List<Overlay> mapOverlays;
        Drawable drawable;
        HelloItemizedOverlay itemizedOverlay;
	private MapView mapView;
	private LocationManager locationManager;
        private GeoPoint point;    
        private static final String TAG = "LocationActivity";
        private Geocoder geocoder;
        private TextView locationText; 

       @Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main); // bind the layout to the activity
                
                locationText = (TextView)this.findViewById(R.id.lblLocationInfo);
                mapView = (MapView)this.findViewById(R.id.mapview);
                mapView.setBuiltInZoomControls(true);
                
                mapController = mapView.getController(); //<4>
                mapController.setZoom(16); 
                locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE); //<2>   
                geocoder = new Geocoder(this); //<3>
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //<5>
           //     Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //<5>

    if (location != null) {
      Log.d(TAG, location.toString());
      this.onLocationChanged(location); //<6>
    }
  } //End onCreate

    @Override
      protected void onResume() {
      super.onResume();
    // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this); //<7>
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); //<7> 
  }

    
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this); //<8>
  }

  @Override
  public void onLocationChanged(Location location) { //<9>
    Log.d(TAG, "onLocationChanged with location " + location.toString());
    String text = String.format("Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f", location.getLatitude(), 
                  location.getLongitude(), location.getAltitude(), location.getBearing());
    this.locationText.setText(text);
    
    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10); //<10>
      for (Address address : addresses) {
        this.locationText.append("\n" + address.getAddressLine(0));
      }
      
      int latitude = (int)(location.getLatitude() * 1000000);
      int longitude = (int)(location.getLongitude() * 1000000);

      point = new GeoPoint(latitude,longitude);
      mapController.animateTo(point); //<11>
      
    } catch (IOException e) {
      Log.e("LocateMe", "Could not get Geocoder data", e);
    } //end of try
  }// end of OnLocationChanged
  
        @Override
	protected boolean isRouteDisplayed() {
		return false;
	}
  
        @Override
	public void onProviderDisabled(String provider) {
	}
        @Override
	public void onProviderEnabled(String provider) {
		}
        @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
}

 



	

		
              
                       
	
  
