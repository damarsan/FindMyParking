package com.damarsan.FindMyParking;

//import android.R;
import android.R.drawable;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.util.List;


public class HelloMapView extends MapActivity implements LocationListener
{
    MapController mapController;
	MapView mapview;
        List<Overlay> mapOverlays;
        Drawable drawable;
        HelloItemizedOverlay itemizedOverlay;
        GeoPoint geopoint = null;
         Context mContext;

        
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapview = (MapView)findViewById(R.id.mapview);        
	mapview.setBuiltInZoomControls(true);  //activamos controles de Zoom
	mapController = mapview.getController();
	mapController.setZoom(16);
              
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        
        
        
                //Activamos la OverlayLayer y añadimos el recurso de imagen androidmarker
                mapOverlays = mapview.getOverlays();
                drawable = this.getResources().getDrawable(R.drawable.androidmarker);
                itemizedOverlay = new HelloItemizedOverlay(drawable,mContext);
                
        
        
        LocationListener locationListener = new LocationListener() {
			
			private TextView locationDetails;
                        // Acquire a reference to the system Location Manager

                        

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				makeUseOfNewLocation(location);
				//Toast.makeText(getApplicationContext(), "New Locationdd", Toast.LENGTH_LONG).show();
				
			}

			private void makeUseOfNewLocation(Location location) {
				
				
				double lon = (double) (location.getLongitude() * 1E6);
				double lat = (double) (location.getLatitude() * 1E6);
				
				int lontitue = (int)lon;
				int latitute = (int)lat;
				
				Toast.makeText(getApplicationContext(), "New Lontitue = "+ lontitue +"\n New Latitute = "+ latitute, Toast.LENGTH_LONG).show();
				locationDetails = (TextView)findViewById(R.id.textViewMap);
				locationDetails.setText("New Lontitue = "+ lontitue +"\n New Latitute = "+ latitute);
			        geopoint = new GeoPoint(latitute, lontitue);
				mapController.animateTo(geopoint);
                                
                                OverlayItem overlayitem = new OverlayItem(geopoint, "", "");
                                itemizedOverlay.addOverlay(overlayitem);
                                mapOverlays.add(itemizedOverlay);
                                
                                
				
				//mapview.invalidate();
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                
                // Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                
                
                //Añadimos el item a la OverlayLayer
                if (geopoint != null) {
                OverlayItem overlayitem = new OverlayItem(geopoint, "", "");
                itemizedOverlay.addOverlay(overlayitem);
                mapOverlays.add(itemizedOverlay);
                } else 	Toast.makeText(getApplicationContext(), "MAPA CARGADO", Toast.LENGTH_LONG).show();

    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

    public void onLocationChanged(Location arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onProviderEnabled(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onProviderDisabled(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}