package com.damarsan.FindMyParking;

//import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import java.io.BufferedReader;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



public class HelloMapView extends MapActivity implements LocationListener
{
        MapController mapController;
	MapView mapview;
        List<Overlay> mapOverlays;
        Drawable drawable=null, drawable2;
        HelloItemizedOverlay itemizedOverlay;
        GeoPoint geopoint = null;
        GeoPoint geopoint2 = null;
        Context mContext;
        Location location;
        LocationListener locationListener;
        LocationManager locationManager;
        private String TAG ="MyActivity";
        private Projection projection;
        String provider;
        Criteria criteria;
        Canvas c1 = new Canvas();
        
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   

        mapview = (MapView)findViewById(R.id.mapview);        
	mapview.setBuiltInZoomControls(true);  //activamos controles de Zoom
	mapController = mapview.getController();
	mapController.setZoom(20);

          //Definimos los criterios para seleccionar al mejor proveedor
          criteria = new Criteria();
          //precisión elevada
          criteria.setAccuracy(Criteria.ACCURACY_FINE);
          //no se requiere informaciñón de altitud
          criteria.setAltitudeRequired(false);
          //no se requiere información de rumbo
          criteria.setBearingRequired(false);
          //se permite gasto
          criteria.setCostAllowed(true);
          //nivel de energia
          criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
          //Alta precisión horizontal y vertical
          criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
          criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
          
          
          //En primer lugar se instancia el LocationManager
          locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
          
           //Obtenemos el mejor proveedor con los criterios descritos anteriormente
           provider = locationManager.getBestProvider(criteria, true);

           //
           location = locationManager.getLastKnownLocation(provider);

                //Activamos la OverlayLayer y añadimos el recurso de imagen androidmarker
                mapOverlays = mapview.getOverlays();
                projection = mapview.getProjection();

                
                drawable = this.getResources().getDrawable(R.drawable.androidmarker);

              //  itemizedOverlay = new HelloItemizedOverlay(drawable,mContext);
                  itemizedOverlay = new HelloItemizedOverlay(drawable,this);
              //  itemizedOverlay = new HelloItemizedOverlay(geopoint,geopoint2,drawable,mapview);
                
                locationListener = new LocationListener() {
			
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
			public void makeUseOfNewLocation(Location location) {
                            
				double lon = (double) (location.getLongitude() * 1E6);
				double lat = (double) (location.getLatitude() * 1E6);
				
				int lontitue = (int)lon;
				int latitute = (int)lat;
				
				Toast.makeText(getApplicationContext(), "Longitud = "+ lontitue +"\n Latitud = "+ latitute, Toast.LENGTH_LONG).show();
				//locationDetails = (TextView)findViewById(R.id.textViewMap);
			//	locationDetails.setText("Longitud = "+ lontitue +"\n Latitud = "+ latitute);
			        geopoint = new GeoPoint(latitute, lontitue);
				mapController.animateTo(geopoint);
                                
                                //creamos el OverlayItem a partir del GeoPoint
                                OverlayItem overlayitem = new OverlayItem(geopoint, "", "");
                                //Añadimos el item al Array de Overlays
                                itemizedOverlay.addOverlay(overlayitem);
                                //Añadimos a la lista de Overlays el item.
                                mapOverlays.add(itemizedOverlay);          
				mapview.invalidate();
			}
                        
                       
		};   //FIN DE LOCATION LISTENER
 
                //Establecemos que las actualizaciones se realizen si hay 5 metros de distancia desde la última
	//	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5,locationListener);               
                // Register the listener with the Location Manager to receive location updates
	//	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,5,locationListener);
            
                
                //Añadimos el item a la OverlayLayer
                
                if (geopoint != null) {
               OverlayItem overlayitem = new OverlayItem(geopoint, "", "");
               itemizedOverlay.addOverlay(overlayitem);
                mapOverlays.add(itemizedOverlay);
                } else 	Toast.makeText(getApplicationContext(), "MAPA CARGADO", Toast.LENGTH_LONG).show();         
    }  // FIN DE ONCREATE()
    
    
    

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
        
        
        
    //GENERAMOS EL MENU PRINCIPAL
    
     @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            populateMenu(menu);
            return (super.onCreateOptionsMenu(menu));
            
        }
     
     
     //AÑADIMOS LAS OPCIONES AL MENU
    private void populateMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Almacenar Parking");
        menu.add(Menu.NONE, 2, Menu.NONE, "Donde Estoy");
        menu.add(Menu.NONE, 3, Menu.NONE, "Recuperar Parking");
        menu.add(Menu.NONE, 4, Menu.NONE, "Limpiar Ubicaciones");
        menu.add(Menu.NONE, 5, Menu.NONE, "Dibujar ruta");
       // throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    
    //SIN ESTO NO FUNCIONA
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return(applyMenuChoice(item) || super.onOptionsItemSelected(item));
    }
    
    //AÑADIMOS LAS ACCIONES DEL MENU
    
    private boolean applyMenuChoice(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 1:
                //Almacenamos la ubicación donde hemos aparcado
                drawable = this.getResources().getDrawable(R.drawable.androidmarker);

                locationManager.requestSingleUpdate(criteria, locationListener, null);

                try {
                    
                FileOutputStream out = openFileOutput("location.txt",Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(out);
                osw.write(Double.toString(geopoint.getLongitudeE6() / 1E6));
                osw.write("\n");
                osw.write(Double.toString(geopoint.getLatitudeE6() / 1E6));
                osw.flush();
                osw.close();
               	Toast.makeText(getApplicationContext(), "Ubicación de Parking Almacenada", Toast.LENGTH_LONG).show();
                }
                catch (IOException t) {
                    Log.v(TAG, "MIERRRDAAA");
                }

             //  Log.v(TAG, "MIERDAAAAAAA "+t.getMessage().toString());}
           
                return(true);
                
            case 2:
                 //Cambiamos el icono de ubicación
                 drawable = this.getResources().getDrawable(R.drawable.androidmarker);

                //Obtenemos la ubicación actual
                 locationManager.requestSingleUpdate(criteria, locationListener, null);
                return(true);
                
            case 3:
                
                //Obtenemos la ubicación almacenada y la mostramos
                String[] separated=null;
                try {
                FileInputStream in = openFileInput("location.txt");
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String str;
                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str+"\n");
                }
                //separamos la longitud y latitud
                separated = buf.toString().split("\n");
                separated[0].trim();
                separated[1].trim();
                //Cerramos FileInputStream y InputStreamReader
                isr.close();
               // in.close();                                  
                Log.v(TAG, separated[0]);
                Log.v(TAG, separated[1]); 
    
                }catch (Throwable t) {}
                
                double lng = Double.parseDouble(separated[0]);
                double lat = Double.parseDouble(separated[1]);
                

               geopoint2 = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
                mapController.setZoom(18); 
                mapController.animateTo(geopoint2);
                 
                 
                Log.v(TAG, geopoint2.toString());
                 
                OverlayItem overlayitem2 = new OverlayItem(geopoint2, "", "");
                drawable2 = this.getResources().getDrawable(R.drawable.parking);
                drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                overlayitem2.setMarker(drawable2);
                itemizedOverlay.addOverlay(overlayitem2);
               // mapOverlays.clear();
                mapOverlays.add(itemizedOverlay);
                mapview.invalidate();
                Toast.makeText(getApplicationContext(), "Ubicación de Parking Recuperada", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Longitud = "+ lng +" Latitud = "+ lat, Toast.LENGTH_LONG).show();
                return(true);
                
            case 4:
                if(!mapOverlays.isEmpty()) 
                    { 
                        itemizedOverlay.clearOverlay();
                        mapOverlays.clear();
                        mapview.postInvalidate();
                    } else Toast.makeText(getApplicationContext(),"No hay ubicaciones", Toast.LENGTH_LONG).show();

                return(true);
                
            case 5:
                itemizedOverlay.draw2(c1, mapview, true);
                return(true);

        }
        return (false);
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
    
}  //FIN DE LA CLASE