package com.damarsan.FindMyParking;

//import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import java.util.List;


public class HelloMapView extends MapActivity implements LocationListener
{
    private MapController mapController;
        List<Overlay> mapOverlays;
        Drawable drawable;
        HelloItemizedOverlay itemizedOverlay;
	private MapView mapView;
	private LocationManager locationManager;
        public GeoPoint point;    
        private static final String TAG = "LocationActivity";
        private Geocoder geocoder;
        private TextView locationText; 
        
    private class MyOverlay extends com.google.android.maps.Overlay{
        @Override
    
    public void draw(Canvas canvas, MapView mapView, boolean shadow) { //1
        super.draw(canvas, mapView, shadow);
        
       //  Toast.makeText(getApplicationContext(), "MIERRRRRDA", Toast.LENGTH_LONG).show();
        
        if(!shadow) { //2
            Point point2 = new Point();

            mapView.getProjection().toPixels(point, point2);//3

                
             //   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                
            
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.androidmarker); //4
            int x = point2.x - bmp.getWidth() / 2; //5
            int y = point2.y - bmp.getHeight();    //6
            
            canvas.drawBitmap(bmp, x, y, null); //7
        }
    }
    
}
        
       @Override
	public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
	setContentView(R.layout.main);
        mapView = (MapView)findViewById(R.id.mapview);
	
	mapController = mapView.getController();
	mapController.setZoom(23);
        mapView.setBuiltInZoomControls(true);
       
        MapView.LayoutParams params = new MapView.LayoutParams(
    LayoutParams.WRAP_CONTENT,
    LayoutParams.WRAP_CONTENT,
    mapView.getWidth() /2, mapView.getHeight(),
    MapView.LayoutParams.BOTTOM_CENTER);
          mapView.addView(mapView.getZoomControls(), params);


        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        
        LocationListener locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {			// TODO Auto-generated method stub
				
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
				 point = new GeoPoint(latitute, lontitue);
				mapController.animateTo(point);
                               
                                //Añadimos el Overlay
                                List<Overlay> overlays = mapView.getOverlays();
                                overlays.clear();
                                overlays.add(new MyOverlay());
                                mapView.invalidate();
                               // mapView.removeView(mapView);
                                MapView.LayoutParams params = new MapView.LayoutParams(
                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT,
                                    mapView.getWidth() /2, mapView.getHeight(),
                                    MapView.LayoutParams.BOTTOM_CENTER);
                                mapView.addView(mapView.getZoomControls(), params);
			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }
       
     


/*
 * 1 The draw method has a couple of arguments. The first argument is a handle to an instance of Canvas which we will use to draw our marker on. The second is an instance of MapView on which this overlay is displayed. The third argument is a boolean which indicates whether we are drawing the actual image, or the shadow. In fact, this method is called twice. Once to draw the shadow and once to draw the actual thing you want to draw.
   2 We don't want to draw a shadow
   3  We translate the geo point to actual pixels and store this information in the point variable.
   4 We use the resource identifier to decode it to an actual instance of Bitmap so we can draw it on the canvas
   5 We calculate the x-coordinate of where to draw the marker. We shift it to the left so the center of the image is aligned with the x-coordinate of the geo point
   6 We calculate the y-coordinate of where to draw the marker. We shift it upward so the bottom of the image is aligned with the y-coordinate of the geo point
   7 We draw the bitmap at the calculated x and y locations. 
 */

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
 
