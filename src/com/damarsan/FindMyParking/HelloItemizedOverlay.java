package com.damarsan.FindMyParking;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import java.io.IOException;
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

 public void deleteOverlay(OverlayItem overlay) {
     mOverlays.remove(overlay);
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
      
   /* @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {


        Point screenCoords=new Point();
        Point screenCoords1=new Point();
        
        if (p1!=null && p2!= null) {
            
        p1 = this.getItem(0).getPoint();
        p2 = this.getItem(1).getPoint();
       
        Log.v(TAG, p1.toString());
        Log.v(TAG, p2.toString());
        
        mapView.getProjection().toPixels(p1, screenCoords);
        int x1=screenCoords.x;
        int y1=screenCoords.y;

        mapView.getProjection().toPixels(p2, screenCoords1);
        int x2=screenCoords1.x;
        int y2=screenCoords1.y;

        paint.setStrokeWidth(2); 
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); 
        paint.setColor(Color.GREEN);
        canvas.drawLine(x1, y1, x2, y2, paint);
        super.draw(canvas, mapView, shadow);

        }
    }*/
    
    

}
 
 
 

