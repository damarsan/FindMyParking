package com.damarsan.FindMyParking;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;


public class HelloItemizedOverlay extends ItemizedOverlay {

 private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
 private Drawable marker = null;
 Context mContext;


 public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
  super(boundCenterBottom(defaultMarker));
  mContext = context;
  populate();
 }

 public void addOverlay(OverlayItem overlay) {
     mOverlays.add(overlay);
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
}
 
 
 

