package app.themes;

import com.formdev.flatlaf.FlatLightLaf;

public class LightTheme extends FlatLightLaf {
	
	private static final long serialVersionUID = 1225413335661960024L;
	
	public static boolean setup() {
		return setup(new LightTheme());
	}
	
	@Override
    public String getName() {
        return "LightTheme";
    }

}
