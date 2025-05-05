package app.utilities;

import java.awt.Rectangle;
import java.util.List;
import java.util.prefs.Preferences;

import com.formdev.flatlaf.util.StringUtils;

public class ResolutionManager {
	
	private static final Preferences prefs = Preferences.userNodeForPackage(ResolutionManager.class);
    private static final String KEY = "currentResolution";
    private static final String DEFAULT_RESOLUTION_STRING = "100,100,1080,495";
    public static final Rectangle DEFAULT_RESOLUTION = new Rectangle(100, 100, 1080, 495);
    
    private ResolutionManager() {
    	throw new IllegalStateException("Utility Class");
    }
    public static void saveResolution(Rectangle resolution) {
    	if(resolution!=null) {
    		prefs.put(KEY, parseRectangleResolutionToString(resolution));
    	}
    }

    public static Rectangle getResolution() {
    	return parseStringResolutionToRectangle(prefs.get(KEY, DEFAULT_RESOLUTION_STRING));
    }
    
    private static String parseRectangleResolutionToString(Rectangle r) {
    	Rectangle bounds = r.getBounds();
    	return bounds.x +
                "," +
                bounds.y +
                "," +
                bounds.width +
                "," +
                bounds.height;
    }
    
    private static Rectangle parseStringResolutionToRectangle(String resolution) {
    	List<String> resolutionBounds = StringUtils.split(resolution, ',', true, true);
    	Rectangle r = new Rectangle();
    	r.x=Integer.parseInt(resolutionBounds.get(0));
    	r.y=Integer.parseInt(resolutionBounds.get(1));
    	r.width = Integer.parseInt(resolutionBounds.get(2));
    	r.height = Integer.parseInt(resolutionBounds.get(3));
    	
    	return r;
    }
    
}
