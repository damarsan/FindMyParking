package com.damarsan.FindMyParking;

//import android.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.util.List;

public class HelloMapView extends MapActivity
{
    LinearLayout linearLayout;
    MapView mapView;
    List<Overlay> mapOverlays;
    Drawable drawable;
    HelloItemizedOverlay itemizedOverlay;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedOverlay = new GeoPoint(19240000,-99120000);
        //  itemizedoverlay = new HelloItemizedOverlay(drawable);
        GeoPoint point = new GeoPoint(19240000,-99120000);
        OverlayItem overlayitem = new OverlayItem(point, "", "");
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        
    }
    
    @Override
    protected boolean isRouteDisplayed() {
    return false;
}
}
