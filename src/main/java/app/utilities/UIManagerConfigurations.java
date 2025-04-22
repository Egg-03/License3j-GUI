package app.utilities;

import java.awt.Insets;

import javax.swing.UIManager;

// custom UI manager configurations that allow slight changes to the LAF
// Applies on all LAFs
// Invoke this class only AFTER you've used UIManager.setLookAndFeel() and BEFORE you have laid out your components
public class UIManagerConfigurations {
	private UIManagerConfigurations() {
		throw new IllegalStateException("Utility Class");
	}
	
	public static void enableRoundComponents() {
		UIManager.put( "Button.arc", 999 );
		UIManager.put( "Component.arc", 999 );
		UIManager.put( "ProgressBar.arc", 999 );
		UIManager.put( "TextComponent.arc", 999 );
		
		UIManager.put( "ScrollBar.thumbArc", 999 );
		UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );
		
		UIManager.put( "ScrollBar.trackArc", 999 );
		UIManager.put( "ScrollBar.thumbArc", 999 );
		UIManager.put( "ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ) );
		UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );
	}
	
	public static void enableTabSeparators(boolean value) {
		UIManager.put( "TabbedPane.showTabSeparators", value );
		UIManager.put( "TabbedPane.tabSeparatorsFullHeight", value );
	}
}
