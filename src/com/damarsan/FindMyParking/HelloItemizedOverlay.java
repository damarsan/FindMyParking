package com.damarsan.FindMyParking;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
 private Paint accuracyPaint;
 private Point center;
 private Point left;
 private Drawable drawable;
 private int width;
 private int height;
 private static final double defaultLatitude = 0.0;
 private static final double defaultLongitude = 0.0;
  private static final float defaultAccuracy = 250f; // or whatever

  Location currentLocation; // this should be already known


 private String TAG ="MyActivity";



 public HelloItemizedOverlay(GeoPoint prePoint,GeoPoint currentPoint, Drawable defaultMarker,MapView mapview) {

    super(boundCenterBottom(defaultMarker));
    this.p1=currentPoint;
    this.p2 = prePoint;
    mapView=mapview;
     double lat1 = ((double)p1.getLatitudeE6()) / 1e6;
     double lng1 = ((double)p1.getLongitudeE6()) / 1e6;
     currentLocation.setLatitude(lat1);
     currentLocation.setLongitude(lng1);
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


   /* @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);

        accuracyPaint = new Paint();
        accuracyPaint.setAntiAlias(true);
        accuracyPaint.setStrokeWidth(2.0f);

        drawable = mapView.getContext().getResources().getDrawable(R.drawable.androidmarker);
        width = drawable.getIntrinsicWidth();
        height = drawable.getIntrinsicHeight();
        center = new Point();
        left = new Point();
        double latitude;
        double longitude;
        float accuracy;
        Projection projection = mapView.getProjection();

        if(currentLocation == null) {
            latitude = defaultLatitude;
            longitude = defaultLongitude;
            accuracy = defaultAccuracy;
        } else {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            accuracy = currentLocation.getAccuracy();
        }

        float[] result = new float[1];

        Location.distanceBetween(latitude, longitude, latitude, longitude + 1, result);
        float longitudeLineDistance = result[0];

        GeoPoint leftGeo = new GeoPoint((int)(latitude * 1E6), (int)((longitude - accuracy / longitudeLineDistance) * 1E6));
        projection.toPixels(leftGeo, left);
      //  projection.toPixels(p2, center);
        int radius = center.x - left.x;

        accuracyPaint.setColor(0xff6666ff);
        accuracyPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

        accuracyPaint.setColor(0x186666ff);
        accuracyPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

        drawable.setBounds(center.x - width / 2, center.y - height / 2, center.x + width / 2, center.y + height / 2);
        drawable.draw(canvas);

        return true;
    }

     */

   /*
   @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {


       Paint lp4;
       lp4 = new Paint();
       lp4.setColor(RED);
       lp4.setAntiAlias(true);
       lp4.setStyle(Paint.Style.STROKE);
       canvas.drawCircle(mapView.getWidth()/2, mapView.getHeight()/2, 20, lp4);
       drawable = this.getResources().getDrawable(R.drawable.androidmarker);

       mapView.invalidate();


    }
      */
    

}
 
 
 

