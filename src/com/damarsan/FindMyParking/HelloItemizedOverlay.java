package com.damarsan.FindMyParking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class HelloItemizedOverlay extends ItemizedOverlay {

 private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
 private Drawable marker = null;
 private Context context;

 GeoPoint p1=null,p2=null;
 MapView mapView=null;
 Paint paint=new Paint();
 private String TAG ="MyActivity";



 public HelloItemizedOverlay(GeoPoint prePoint,GeoPoint currentPoint, Drawable defaultMarker,MapView mapview) {

    super(boundCenterBottom(defaultMarker));
    this.p1=currentPoint;
    this.p2 = prePoint;
    mapView=mapview;
    // TODO Auto-generated constructor stub
}
 
 public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
  super(boundCenterBottom(defaultMarker));
  this.context = context;
  populate();
 }
 
 public void addOverlay(OverlayItem overlay) {
     mOverlays.add(overlay);
     populate();
 }
 
 public void clearOverlay()
 {
     mOverlays.clear();
 }

 public void removeOverlay(int i) {
     mOverlays.remove(i);
     populate();
 }
 
 @Override
 protected OverlayItem createItem(int i) {
   return mOverlays.get(i);
 }

 @Override
 public int size() {
   return mOverlays.size();
 }
 
 
 
 @Override
 protected boolean onTap(int index)
 {
 OverlayItem item = mOverlays.get(index);
 GeoPoint p = item.getPoint();
   Geocoder gc = new Geocoder(
         this.context, Locale.getDefault());
         try
         {
             List<Address> addresses = gc.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);
             
             String add = "";
             if (addresses.size() > 0 )
             {
                 for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
                     add += addresses.get(0).getAddressLine(i) + "\n";
             }
             
                Toast.makeText(this.context, add, Toast.LENGTH_SHORT).show();
         }
            catch (IOException e){
                e.printStackTrace();
                                 }     
             
 return true;
 }


      
   @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

      /*
       Paint lp4;
       lp4 = new Paint();
       lp4.setColor(Color.RED);
       lp4.setAntiAlias(true);
       lp4.setStyle(Paint.Style.STROKE);
       canvas.drawCircle(mapView.getWidth()/2, mapView.getHeight()/2, 20, lp4);
      // drawable = this.getResources().getDrawable(R.drawable.androidmarker);

       mapView.invalidate();

         */
    }
    
    

}
 
 
 

