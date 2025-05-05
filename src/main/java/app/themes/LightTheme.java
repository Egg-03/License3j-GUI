package app.themes;

import com.formdev.flatlaf.FlatLightLaf;

import java.io.Serial;

public class LightTheme extends FlatLightLaf {
	
	@Serial
	private static final long serialVersionUID = 1225413335661960024L;
	
	public static boolean setup() {
		return setup(new LightTheme());
	}
	
	@Override
    public String getName() {
        return "LightTheme";
    }

}
