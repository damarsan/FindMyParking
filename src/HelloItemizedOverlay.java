
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author damarsan
 */
public class HelloItemizedOverlay implements ItemizedOverlay {
    
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    super(boundCenterBottom(defaultMarker)); 
    
    public void addOverlay(OverlayItem overlay) {
    mOverlays.add(overlay);
    populate();
}
    
   // @Override
protected OverlayItem createItem(int i) {
  return mOverlays.get(i);
}
protected int size(){
    return mOverlays.size();
}
    
}
