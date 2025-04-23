package app.utilities;

public class VersionAndOtherInfo {

	private VersionAndOtherInfo() {
		throw new IllegalStateException("Utility Class");
	}
	
	public static final String APP_AUTHOR = "Egg-03";
	public static final String APP_VERSION = "1.0.0";
	
	public static final String LICENSE3J_AUTHOR = "Peter Verhas";
	public static final String LICENSE3J_VERSION = "3.3.0";
	
	public static final String ABOUT = """
			## About License3J GUI
			License3J GUI is a Graphical User Interface developed by Egg-03 for the free and open source [License3j](https://github.com/verhas/License3j) library. The source code is based on the [License3j-REPL Application](https://github.com/verhas/License3jRepl). This application provides a user-friendly interface to create, view, and validate license files, generate keys for signing the licenses, using the powerful features of the License3j library. See the source code on [GitHub](https://github.com/Egg-03/License3j-GUI)

			## About License3J
			License3j is a free and open-source Java library to manage license files in Java programs.
			A license file is a special electronically signed configuration file. The library can create (including signing), read, and verify such license files. Learn more about License3j on [GitHub](https://github.com/verhas/License3j)

			## Licensing
			Both License3J and the GUI application are licensed under the [Apache2.0-License](https://www.apache.org/licenses/LICENSE-2.0). 

			## Other Libraries Used 
			This app also depends on the following libraries:
			
			[TinyLog](https://tinylog.org/),
			[MigLayout](https://github.com/mikaelgrev/miglayout),
			[Apache Commons IO](https://commons.apache.org/proper/commons-io/),
			[FlatLaf](https://www.formdev.com/flatlaf/),
			[Flexmark](https://github.com/vsch/flexmark-java)
			""";
}
