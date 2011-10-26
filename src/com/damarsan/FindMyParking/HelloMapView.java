package com.damarsan.FindMyParking;

//import android.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.io.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloMapView extends MapActivity implements LocationListener
{
        MapController mapController;
	    MapView mapview;
        List<Overlay> mapOverlays;
        Drawable drawable=null, drawable2;
        HelloItemizedOverlay itemizedOverlay;
        GeoPoint geopoint_p = null;
        GeoPoint geopoint_u= null;
        Context mContext;
        Location location;
        LocationListener locationListener;
        LocationManager locationManager;
        private String TAG ="MyActivity";
        String provider;
        Criteria criteria;
        Canvas c1 = new Canvas();
        private ProgressDialog dialog;
        StringBuilder response = new StringBuilder();
        String unidad=null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

       if(!this.checkInternetConnection())
       {
      /*  AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("AVISO");
        alertDialog.setMessage("Se requiere conexión a Internet");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                     startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                } catch(Throwable t){}; 
            }
                });
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
            */

        //   createDialog("AVISO","Se requiere conexión a Internet","l");
       }

        if(!checkGPS()) {
        //    createDialog("AVISO","Es recomendable activar el GPS para mejorar precisión","g");
        }
        
        if (!checkFirstTime()) Log.v(TAG,"OK, ya se había almacenado el parking"); else  Log.v(TAG,"NO ESTA almacenado el parking");

    mapview = (MapView)findViewById(R.id.mapview);
	mapview.setBuiltInZoomControls(true);  //activamos controles de Zoom
	mapController = mapview.getController();
	mapController.setZoom(18);

          //Definimos los criterios para seleccionar al mejor proveedor
          criteria = new Criteria();
          //precisión elevada
          criteria.setAccuracy(Criteria.ACCURACY_FINE);
          //no se requiere información de altitud
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
          //Mostramos dialogo de carga
          this.runDialog(3);
          //En primer lugar se instancia el LocationManager
          locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
          
           //Obtenemos el mejor proveedor con los criterios descritos anteriormente
           provider = locationManager.getBestProvider(criteria, true);
           //
           location = locationManager.getLastKnownLocation(provider);
                //Activamos la OverlayLayer y añadimos el recurso de imagen androidmarker
                mapOverlays = mapview.getOverlays();
                drawable = this.getResources().getDrawable(R.drawable.androidmarker);
                  itemizedOverlay = new HelloItemizedOverlay(drawable,this);
                  locationListener = new LocationListener() {
                        // Acquire a reference to the system Location Manager
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.v(TAG, "....OnStatusChanged----");
			}
			@Override
			public void onProviderEnabled(String provider) {
                                Log.v(TAG, "....OnProviderEnabled----");
                                

			}
			@Override
			public void onProviderDisabled(String provider) {
				 Log.v(TAG, "....OnProviderDisabled----");
			}
			@Override
			public void onLocationChanged(Location location) {
				makeUseOfNewLocation(location);
			}
			public void makeUseOfNewLocation(Location location) {                            
			
                          if(itemizedOverlay.size() != 0)
                              itemizedOverlay.removeOverlay(0);
                                double lon = (double) (location.getLongitude() * 1E6);
				                double lat = (double) (location.getLatitude() * 1E6);
				                int lontitue = (int)lon;
				                int latitute = (int)lat;
			                    geopoint_u = new GeoPoint(latitute, lontitue);
				                mapController.animateTo(geopoint_u);
                                //creamos el OverlayItem a partir del GeoPoint
                                OverlayItem overlayitem = new OverlayItem(geopoint_u, "", "");
                                //Añadimos el item al Array de Overlays
                                itemizedOverlay.addOverlay(overlayitem);
                                //Añadimos a la lista de Overlays el item.
                                mapOverlays.add(itemizedOverlay); 
                                if(geopoint_p != null && geopoint_u !=null)  centerMapView();
				                mapview.invalidate();
			}         
		};   //FIN DE LOCATION LISTENER
 
                //Establecemos que las actualizaciones se realizen si hay 5 metros de distancia desde la última
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5,locationListener);
                // Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,5,locationListener);
            
                
                //Añadimos el item a la OverlayLayer   
                if (geopoint_p != null) {
               OverlayItem overlayitem = new OverlayItem(geopoint_p, "", "");
               itemizedOverlay.addOverlay(overlayitem);
                mapOverlays.add(itemizedOverlay);
                } else {
                    Toast.makeText(getApplicationContext(), "MAPA CARGADO", Toast.LENGTH_SHORT).show();
                } 	
                             
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
        menu.add(Menu.NONE, 5, Menu.NONE, "Calcular Distancia");
        menu.add(Menu.NONE, 6, Menu.NONE, "Mostrar Ruta");
       // throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    
    //SIN ESTO NO FUNCIONA
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            return(applyMenuChoice(item) || super.onOptionsItemSelected(item));
        } catch (MalformedURLException ex) {
            Logger.getLogger(HelloMapView.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HelloMapView.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       
    }
    
    //AÑADIMOS LAS ACCIONES DEL MENU
    
    private boolean applyMenuChoice(MenuItem item) throws MalformedURLException
    {
        switch (item.getItemId())
        {
            case 1://Almacenar Parking
                //Almacenamos la ubicación donde hemos aparcado
                drawable = this.getResources().getDrawable(R.drawable.androidmarker);
                //Calculamos el Geopoint
                double lon = (double) (location.getLongitude() * 1E6);
				double lati = (double) (location.getLatitude() * 1E6);
				int lontitue = (int)lon;
				int latitute = (int)lati;
                               
			        geopoint_p = new GeoPoint(latitute, lontitue);
                                
                //lo almacenamos                
                try {  
                FileOutputStream out = openFileOutput("location.txt",Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(out);
                osw.write(Double.toString(geopoint_p.getLongitudeE6() / 1E6));
                osw.write("\n");
                osw.write(Double.toString(geopoint_p.getLatitudeE6() / 1E6));
                osw.flush();
                osw.close();
               	Toast.makeText(getApplicationContext(), "Ubicación de Parking Almacenada", Toast.LENGTH_SHORT).show();
                }
                catch (IOException t) {
                }
           
                return(true);
                
            case 2: //Donde estoy
                  this.runDialog(3);
                 //Cambiamos el icono de ubicación               
                 drawable = this.getResources().getDrawable(R.drawable.androidmarker);
                 drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                //Obtenemos la ubicación actual
                 locationManager.requestSingleUpdate(criteria, locationListener, null);
               //  callFoursquare();
                 return(true);
                
            case 3: //Recuperar Parking
                
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
               
               double lng = Double.parseDouble(separated[0]);
               double lat = Double.parseDouble(separated[1]);
                geopoint_p = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
             //   mapController.setZoom(18); 
                mapController.animateTo(geopoint_p);              
                OverlayItem overlayitem2 = new OverlayItem(geopoint_p, "", "");
                drawable2 = this.getResources().getDrawable(R.drawable.parking);
                drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                overlayitem2.setMarker(drawable2);
                itemizedOverlay.addOverlay(overlayitem2);
               // mapOverlays.clear();
                mapOverlays.add(itemizedOverlay);
                mapview.invalidate();
                   this.ViewDialog("Precisión aprox. de "+location.getAccuracy()+ " mts.");

                 } catch (Exception e) {
                   Toast.makeText(getApplicationContext(),"No hay ubicación almacenada", Toast.LENGTH_SHORT).show();
               }
                if(geopoint_p != null && geopoint_u !=null)
                {
                    this.centerMapView();
                    Toast.makeText(getApplicationContext(), "Ubicación de Parking Recuperada", Toast.LENGTH_SHORT).show();
                }
                return(true);
                
            case 4: //Limpiar Ubicaciones
                if(!mapOverlays.isEmpty()) 
                    { 
                        itemizedOverlay.clearOverlay();
                        mapOverlays.clear();
                        mapview.postInvalidate();
                        geopoint_p = null; geopoint_u=null;
                    } else Toast.makeText(getApplicationContext(),"No hay ubicaciones", Toast.LENGTH_SHORT).show();

                return(true);
                
            case 5: //Calcular Distancia
                     if(geopoint_p != null && geopoint_u !=null)
                     {                 
                 Double res = this.getDistanceInKiloMeters(geopoint_p, geopoint_u);

                      this.ViewDialog("Distancia aprox. de "+res.toString()+unidad);      
                     }
                     else 
                       Toast.makeText(getApplicationContext(),"Falta ubicar origen y/o destino", Toast.LENGTH_SHORT).show();
                     
                return(true);
                
                 case 6: //Mostrar Ruta
                      if(geopoint_p != null && geopoint_u !=null)
                     {
                      double lat_geopoint_u = ((double)geopoint_u.getLatitudeE6()) / 1e6;
                      double lon_geopoint_u = ((double)geopoint_u.getLongitudeE6()) / 1e6;
                      double lat_geopoint_p = ((double)geopoint_p.getLatitudeE6()) / 1e6; 
                      double lon_geopoint_p = ((double)geopoint_p.getLongitudeE6()) / 1e6;
                      this.centerMapView();
                      //generamos el intent para llamar a la aplicacion de google maps
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,       
                 Uri.parse("http://maps.google.com/maps?saddr="+
                 Double.toString(lat_geopoint_u)+","+Double.toString(lon_geopoint_u)+"&daddr="+Double.toString(lat_geopoint_p)+
                 ","+Double.toString(lon_geopoint_p)));
                 startActivity(intent);
                     } else  Toast.makeText(getApplicationContext(),"Falta ubicar origen y/o destino", Toast.LENGTH_SHORT).show();
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
    
    public double getDistanceInKiloMeters(GeoPoint p1, GeoPoint p2) {
    double distance;
    double lat1 = ((double)p1.getLatitudeE6()) / 1e6;
    double lng1 = ((double)p1.getLongitudeE6()) / 1e6;
    double lat2 = ((double)p2.getLatitudeE6()) / 1e6;
    double lng2 = ((double)p2.getLongitudeE6()) / 1e6;
    Location locationA = new Location("point A");
    locationA.setLatitude(lat1);  
    locationA.setLongitude(lng1);         
    Location locationB = new Location("point B"); 
    locationB.setLatitude(lat2);
    locationB.setLongitude(lng2);
    distance = locationA.distanceTo(locationB);
    if (distance>1000)
    {
       unidad =" Kms";
        return Math.round(distance/1000);
    }

    else {
        unidad=" Mts.";
        return Math.round(distance);
        }
    }
    
                        public void ViewDialog(String aux) {
                                        LayoutInflater inflater = getLayoutInflater();
                                        View layout = inflater.inflate(R.layout.toast_layout,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));                                       
                                        ImageView image = (ImageView) layout.findViewById(R.id.image);
                                        image.setImageResource(R.drawable.androidmarker);
                                        TextView text = (TextView) layout.findViewById(R.id.text);
                                        text.setText(aux);
                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();
                                        }
                        
                     
        private void createDialog(String title, final String comment, final char type)
        {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(comment);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                 //   if (!(type != "l")) {
                            startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                 //       } else {
                //    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
               // }

                } catch(Throwable t){};
            }
                });
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
       }


                        
      private boolean checkInternetConnection() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
            // ARE WE CONNECTED TO THE NET
          NetworkInfo.State mobile = conMgr.getNetworkInfo(0).getState();
          NetworkInfo.State wifi = conMgr.getNetworkInfo(1).getState();
          if ((mobile == NetworkInfo.State.CONNECTED) || (wifi == NetworkInfo.State.CONNECTED))
           return true;
                else return false;
       }


        private boolean checkGPS()
        {
            LocationManager lm;
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean gps_on = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return gps_on;
        }


     
      private void centerMapView() {
                mapController.zoomToSpan(itemizedOverlay.getLatSpanE6(), itemizedOverlay.getLonSpanE6());      
      }
    
      private boolean checkFirstTime()
      {
          boolean  res=false;
          FileInputStream in2=null;
          try {
          in2 = openFileInput("location.txt");
          in2.close();
      } catch (IOException e) {
        res = true;
      }
          return res;
      }
    
    
    public void callFoursquare()
    {
        Intent intent = new Intent(HelloMapView.this, ActivityWebView.class);
        startActivity(intent);
    }
      
      
      private void runDialog(final int seconds)
	{
	        dialog = ProgressDialog.show(this, "", "Obteniendo Ubicación...");
	        new Thread(new Runnable(){
	            public void run(){
	                try {
	                            Thread.sleep(seconds * 1000);
	                    dialog.dismiss();
	                } catch (InterruptedException e) { }
	            }
	        }).start();
	}

    
}  //FIN DE LA CLASE