package app.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class DarkTheme extends FlatDarkLaf {
	
	private static final long serialVersionUID = -5458540350973081729L;
	public static final String NAME = "DarkTheme";

	public static boolean setup() {
		return setup( new DarkTheme() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, DarkTheme.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}
