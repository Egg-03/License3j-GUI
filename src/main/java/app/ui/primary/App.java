package app.ui.primary;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.tinylog.Logger;

import com.formdev.flatlaf.FlatLaf;

import app.logic.LicenseGeneration;
import app.themes.DarkTheme;
import app.themes.LightTheme;
import app.ui.secondary.AboutUI;
import app.utilities.LogListener;
import app.utilities.ThemeManager;
import app.utilities.UIManagerConfigurations;
import javax0.license3j.HardwareBinder;
import javax0.license3j.io.IOFormat;
import net.miginfocom.swing.MigLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;

public class App {

	private JFrame mainframe;
	private Tailer logTailer;
	private final LicenseGeneration lg = new LicenseGeneration();
	private static final String APP_LOCATION="user.dir";
	private ScheduledExecutorService scheduler;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		FlatLaf.registerCustomDefaultsSource("themes"); // for maven build, this points towards src/main/resources/themes
		UIManagerConfigurations.enableRoundComponents();
		try {
			UIManager.setLookAndFeel(ThemeManager.getRegisteredTheme());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e) {
			Logger.error(e);
		}
		
		EventQueue.invokeLater(()-> {
			App window = new App();
			window.mainframe.setVisible(true);
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainframe = new JFrame();
		mainframe.setIconImage(Toolkit.getDefaultToolkit().getImage(App.class.getResource("/icons/logo.png")));
		mainframe.setResizable(false);
		mainframe.setTitle("License3J GUI");
		mainframe.setBounds(100, 100, 1080, 495);
		mainframe.setLocationRelativeTo(null);
		mainframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		WindowAdapter exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Are you sure you want to close the application?", 
		             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {  	
		        	if(Boolean.TRUE.equals(lg.licenseRequiresSaving())) 
		        		JOptionPane.showMessageDialog(mainframe, "WARNING: Unsaved License Detected. Please save before closing");
		        	else {
		        		if(logTailer!=null)
			        		logTailer.close();
			        	
			        	if(scheduler!=null)
		        			scheduler.close();
			        	
		        		System.exit(0);
		        	}	  		
		        }
		    }
		};
		mainframe.addWindowListener(exitListener);
		mainframe.getContentPane().setLayout(new BorderLayout(0, 0));
		
		addMenuPanel();
		addLicensePanel();
		addLogAndStatusPanel();		
	}

	private void addMenuPanel() {
		JPanel menuPanel = new JPanel();
		mainframe.getContentPane().add(menuPanel, BorderLayout.NORTH);
		menuPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		menuPanel.add(menuBar);
		
		JMenu appMenu = new JMenu("App");
		menuBar.add(appMenu);
		
		JMenuItem forceQuit = new JMenuItem("Force Quit");
		forceQuit.addActionListener(e->{
			Logger.warn("Application was force quit on it's last run.");
			
			if(logTailer!=null) {
				logTailer.close();
			}		
        	
        	if(scheduler!=null) {
        		scheduler.close();
        	}

			System.exit(-1);
		});
		appMenu.add(forceQuit);
		
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(e->EventQueue.invokeLater(()->new AboutUI().setVisible(true)));
		helpMenu.add(about);
				
		JMenuItem openLogFolder = new JMenuItem("Open Log Folder");
		openLogFolder.addActionListener(e->{
			try {
				Desktop.getDesktop().open(new File("logs"));
			} catch (IOException e1) {
				Logger.error("Failed to open the logs folder"+e);
			}
		});
		helpMenu.add(openLogFolder);
		
		JMenu appearanceMenu = new JMenu("Appearance");
		menuBar.add(appearanceMenu);
		
		JRadioButtonMenuItem lightThemeBtn = new JRadioButtonMenuItem("Light Theme");
		buttonGroup.add(lightThemeBtn);
		appearanceMenu.add(lightThemeBtn);
		
		JRadioButtonMenuItem darkThemeBtn = new JRadioButtonMenuItem("Dark Theme");
		buttonGroup.add(darkThemeBtn);
		appearanceMenu.add(darkThemeBtn);
		
		lightThemeBtn.addActionListener(e-> {
			LightTheme.setup();
			SwingUtilities.updateComponentTreeUI(mainframe);
			ThemeManager.registerTheme(LightTheme.class.getCanonicalName());
		});
		
		darkThemeBtn.addActionListener(e-> {
			DarkTheme.setup();
			SwingUtilities.updateComponentTreeUI(mainframe);
			ThemeManager.registerTheme(DarkTheme.class.getCanonicalName());
		});
		
		// a call to this function notifies the radio buttons of the theme that was loaded using the UIManager when the application was first started
		// this function cannot be called when the LaF is being applied cause LaF is applied before the components are loaded
		// and this function needs to access the loaded components or it will throw a NullPointerException
		ThemeManager.notifyCurrentTheme(lightThemeBtn, darkThemeBtn);
		
	}

	private void addLogAndStatusPanel() {
		JPanel logAndStatusPanel = new JPanel();
		mainframe.getContentPane().add(logAndStatusPanel, BorderLayout.SOUTH);
		logAndStatusPanel.setPreferredSize(new Dimension(mainframe.getWidth(), mainframe.getHeight()/3));
		logAndStatusPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Logs and Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JTextArea logtextArea = new JTextArea();
		logtextArea.setEditable(false);
		logtextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane.setViewportView(logtextArea);	
		
		TailerListener tl = new LogListener(logtextArea);
		logTailer = Tailer.builder()
				.setFile(new File("logs/currentsession.log"))
				.setCharset(Charset.defaultCharset())
				.setTailerListener(tl)
				.setDelayDuration(Duration.ofSeconds(1))
				.setReOpen(true)
				.get();
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane tableScrollPane = new JScrollPane();
		statusPanel.add(tableScrollPane);
		
		JTable table = new JTable();
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setModel(new DefaultTableModel(5,2));
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		String[] tableHeading = { "Constraint", "Status" };
		for(int i=0 ; i<table.getModel().getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderValue(tableHeading[i]);
			table.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		
		String[] constraints = { "License In Memory", "Public Key Loaded", "Private Key Loaded", "License Requires Signing", "License Requires Saving" };
		for(int i=0 ; i<table.getModel().getRowCount(); i++) {
			table.getModel().setValueAt(constraints[i], i, 0);
		}
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableScrollPane.setViewportView(table);
		
		
		GroupLayout gl_logAndStatusPanel = new GroupLayout(logAndStatusPanel);
		gl_logAndStatusPanel.setHorizontalGroup(
			gl_logAndStatusPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_logAndStatusPanel.createSequentialGroup()
					.addGap(7)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(statusPanel, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_logAndStatusPanel.setVerticalGroup(
			gl_logAndStatusPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_logAndStatusPanel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_logAndStatusPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(statusPanel, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
					.addContainerGap())
		);
		logAndStatusPanel.setLayout(gl_logAndStatusPanel);
		
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(()->liveStatusReceiver(lg, table), 1, 1, TimeUnit.SECONDS);
	}

	private void addLicensePanel() {
		JPanel licensePanel = new JPanel();
		mainframe.getContentPane().add(licensePanel, BorderLayout.CENTER);
		licensePanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "License", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		licensePanel.setLayout(new BoxLayout(licensePanel, BoxLayout.X_AXIS));
		
		JPanel licenseFunctionPanel = new JPanel();
		licenseFunctionPanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		licenseFunctionPanel.setBorder(new TitledBorder("License Functions"));
		licensePanel.add(licenseFunctionPanel);
		licenseFunctionPanel.setLayout(new MigLayout("filly", "[][grow][grow]", "[][][][][][][][][][]"));
		
		JButton newLicenseBtn = new JButton("New License");
		licenseFunctionPanel.add(newLicenseBtn, "cell 0 0 3 1,growx");
		newLicenseBtn.addActionListener(e->new NewLicense(lg).execute());
		
		JButton loadLicenseBtn = new JButton("Load License");
		licenseFunctionPanel.add(loadLicenseBtn, "cell 0 1 2 1,growx,aligny center");
		
		JComboBox<IOFormat> loadedLicenseTypeComboBox = new JComboBox<>();
		loadedLicenseTypeComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		loadedLicenseTypeComboBox.setModel(new DefaultComboBoxModel<>(new IOFormat[] {IOFormat.BINARY, IOFormat.BASE64, IOFormat.STRING}));
		licenseFunctionPanel.add(loadedLicenseTypeComboBox, "cell 2 1,growx,aligny center");
		
		loadLicenseBtn.addActionListener(e->{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty(APP_LOCATION)));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			int option = fileChooser.showOpenDialog(mainframe);

			if(option == JFileChooser.APPROVE_OPTION){
				new LoadLicense(lg, (IOFormat) loadedLicenseTypeComboBox.getSelectedItem(), fileChooser.getSelectedFile()).execute();
			} 
		});
		
		JButton displayLicenseBtn = new JButton("Display License Information");
		licenseFunctionPanel.add(displayLicenseBtn, "cell 0 3 3 1,growx");
		displayLicenseBtn.addActionListener(e->new DisplayLicense(lg).execute());
		
		JButton signLicenseBtn = new JButton("Sign");
		licenseFunctionPanel.add(signLicenseBtn, "cell 0 5");
		signLicenseBtn.addActionListener(e->new SignLicense(lg).execute());
		
		JButton verifyLicenseBtn = new JButton("Verify");
		licenseFunctionPanel.add(verifyLicenseBtn, "cell 2 5,alignx right");
		verifyLicenseBtn.addActionListener(e->new VerifyLicense(lg).execute());
		
		JLabel licenseNameLabel = new JLabel("License Name");
		licenseNameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
		licenseFunctionPanel.add(licenseNameLabel, "cell 2 8,alignx center");
		
		JButton saveLicenseBtn = new JButton("Save License");
		licenseFunctionPanel.add(saveLicenseBtn, "cell 0 9,growx,aligny center");
		
		JComboBox<IOFormat> saveLicenseTypeComboBox = new JComboBox<>();
		saveLicenseTypeComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		saveLicenseTypeComboBox.setModel(new DefaultComboBoxModel<>(new IOFormat[] {IOFormat.BINARY, IOFormat.BASE64, IOFormat.STRING}));
		licenseFunctionPanel.add(saveLicenseTypeComboBox, "cell 1 9,growx,aligny center");
		
		JTextField licenseNameToSaveTf = new JTextField();
		licenseNameToSaveTf.setFont(new Font("Monospaced", Font.PLAIN, 11));
		licenseFunctionPanel.add(licenseNameToSaveTf, "cell 2 9,growx,aligny center");
		licenseNameToSaveTf.setColumns(10);
		
		saveLicenseBtn.addActionListener(e->new SaveLicense(lg, licenseNameToSaveTf.getText(), (IOFormat) saveLicenseTypeComboBox.getSelectedItem()).execute());
		
		JPanel featurePanel = new JPanel();
		featurePanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		featurePanel.setBorder(new TitledBorder("License Feature"));
		licensePanel.add(featurePanel);
		featurePanel.setLayout(new MigLayout("filly", "[][grow]", "[][][][][][][]"));
		
		JLabel featureNameLabel = new JLabel("F.Name");
		featurePanel.add(featureNameLabel, "cell 0 0");
		
		JTextField featureNameTf = new JTextField();
		featurePanel.add(featureNameTf, "cell 1 0,growx,aligny center");
		featureNameTf.setColumns(10);
		
		JLabel featureTypeLabel = new JLabel("F.Type");
		featurePanel.add(featureTypeLabel, "cell 0 1");
		
		JComboBox<String> featureTypeComboBox = new JComboBox<>();
		featureTypeComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		featureTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"STRING", "BINARY", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BIGINTEGER", "BIGDECIMAL", "DATE", "UUID"}));
		featurePanel.add(featureTypeComboBox, "cell 1 1,growx,aligny center");
		
		JLabel featureContentLabel = new JLabel("F.Content");
		featurePanel.add(featureContentLabel, "cell 0 2");
		
		JTextField featureContentTf = new JTextField();
		featurePanel.add(featureContentTf, "cell 1 2,growx,aligny center");
		featureContentTf.setColumns(10);
		
		JButton addFeatureBtn = new JButton("Add Feature");
		featurePanel.add(addFeatureBtn, "cell 0 3 2 1,growx");
		addFeatureBtn.addActionListener(e-> new AddFeatureToLicense(lg, featureNameTf.getText(), (String) featureTypeComboBox.getSelectedItem(), featureContentTf.getText()).execute());
		
		JLabel machineIdLabel = new JLabel("Machine ID");
		featurePanel.add(machineIdLabel, "cell 0 5,alignx trailing");
		
		JTextField machineIdTf =  new JTextField();
		machineIdTf.setFont(new Font("Monospaced", Font.PLAIN, 11));
		machineIdTf.setEditable(false);
		featurePanel.add(machineIdTf, "cell 1 5,growx");
		machineIdTf.setColumns(10);
		
		JButton addMachineIdBtn = new JButton("Add Machine ID to Feature");
		featurePanel.add(addMachineIdBtn, "cell 0 6 2 1,growx");
		addMachineIdBtn.addActionListener(e-> new AddFeatureToLicense(lg, "licenseId", "UUID", machineIdTf.getText()).execute());
		
		try {
			machineIdTf.setText(new HardwareBinder().getMachineIdString());
		} catch (NoSuchAlgorithmException | SocketException | UnknownHostException e) {
			machineIdTf.setText("N/A");
			Logger.error("MachineID could not be retrieved"+e);
			addMachineIdBtn.setEnabled(false);
		}
		
		JPanel keygenPanel = new JPanel();
		keygenPanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		keygenPanel.setBorder(new TitledBorder("Generate or Load keys"));
		licensePanel.add(keygenPanel);
		keygenPanel.setLayout(new MigLayout("filly", "[][grow,fill][grow,fill]", "[][][][][][][][][][]"));
		
		JLabel algoLabel = new JLabel("Algorithm");
		keygenPanel.add(algoLabel, "cell 0 0");
		
		JLabel algoSizeLabel = new JLabel("Size");
		keygenPanel.add(algoSizeLabel, "cell 1 0");
		
		JLabel keyFormatLabel = new JLabel("Format");
		keygenPanel.add(keyFormatLabel, "cell 2 0");
		
		JComboBox<String> algoComboBox = new JComboBox<>();
		algoComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"RSA", "ECB", "PKCS1Padding"}));
		algoComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		keygenPanel.add(algoComboBox, "cell 0 1,growx");
		
		JComboBox<String> algoSizeComboBox = new JComboBox<>();
		algoSizeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"2048", "1024"}));
		algoSizeComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		keygenPanel.add(algoSizeComboBox, "cell 1 1,growx");
		
		JComboBox<IOFormat> keyFormatComboBox = new JComboBox<>();
		keyFormatComboBox.setModel(new DefaultComboBoxModel<>(new IOFormat[] {IOFormat.BINARY, IOFormat.BASE64}));
		keyFormatComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		keygenPanel.add(keyFormatComboBox, "cell 2 1,growx");
		
		JLabel privateKeyLabel = new JLabel("Private Key Name");
		keygenPanel.add(privateKeyLabel, "cell 0 2,growx");
		
		JTextField privateKeyNameTf = new JTextField();
		keygenPanel.add(privateKeyNameTf, "cell 1 2 2 1,growx");
		privateKeyNameTf.setColumns(10);
		
		JLabel publicKeyLabel = new JLabel("Public Key Name");
		keygenPanel.add(publicKeyLabel, "cell 0 3,growx");
		
		JTextField publicKeyNameTf = new JTextField();
		keygenPanel.add(publicKeyNameTf, "cell 1 3 2 1,growx");
		publicKeyNameTf.setColumns(10);
		
		JButton generateKeyBtn = new JButton("Generate Keys");
		keygenPanel.add(generateKeyBtn, "cell 0 4,growx");
		generateKeyBtn.addActionListener(e-> new GenerateKeys(lg, (String)algoComboBox.getSelectedItem(), (String)algoSizeComboBox.getSelectedItem(), (IOFormat)keyFormatComboBox.getSelectedItem(), privateKeyNameTf.getText(), publicKeyNameTf.getText()).execute());
		
		JButton publicKeyDigestBtn = new JButton("Digest Public Key");
		keygenPanel.add(publicKeyDigestBtn, "cell 1 4 2 1,growx");
		publicKeyDigestBtn.addActionListener(e->new DigestPublicKey(lg).execute());
		
		JButton copyPublicKeyButton = new JButton("Copy Public Key");
		keygenPanel.add(copyPublicKeyButton, "cell 0 6 2 1,growx");
		
		JLabel copyLabel = new JLabel("");
		copyLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		keygenPanel.add(copyLabel, "cell 2 6");
		
		copyPublicKeyButton.addActionListener(e->{
			try {
				String publicKey = lg.digestPublicKey();
				if(!publicKey.isBlank() || !publicKey.isEmpty()) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(publicKey), null);
					copyLabel.setText("Copied.");
				}		
			} catch (HeadlessException | NoSuchAlgorithmException e1) {
				Logger.error(e);
			}
		});
		
		JButton loadPrivateKeyBtn = new JButton("Load Private Key");
		keygenPanel.add(loadPrivateKeyBtn, "cell 0 8,growx");
		
		JButton loadPublicKeyBtn = new JButton("Load Public Key");
		keygenPanel.add(loadPublicKeyBtn, "cell 1 8,growx");	
		
		JComboBox<IOFormat> loadedKeyTypeComboBox = new JComboBox<>();
		loadedKeyTypeComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		loadedKeyTypeComboBox.setModel(new DefaultComboBoxModel<>(new IOFormat[] {IOFormat.BINARY, IOFormat.BASE64}));
		keygenPanel.add(loadedKeyTypeComboBox, "cell 2 8,growx");

		loadPrivateKeyBtn.addActionListener(e->{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty(APP_LOCATION)));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			int option = fileChooser.showOpenDialog(mainframe);

			if(option == JFileChooser.APPROVE_OPTION){
				new LoadPrivateKey(lg, (IOFormat)loadedKeyTypeComboBox.getSelectedItem(), fileChooser.getSelectedFile()).execute();
			} 
		});
		
		loadPublicKeyBtn.addActionListener(e->{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty(APP_LOCATION)));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			int option = fileChooser.showOpenDialog(mainframe);

			if(option == JFileChooser.APPROVE_OPTION){
				new LoadPublicKey(lg, (IOFormat)loadedKeyTypeComboBox.getSelectedItem(), fileChooser.getSelectedFile()).execute();
			} 
		});	
	}
	
	private void liveStatusReceiver(LicenseGeneration lg, JTable table) {
		
		List<Boolean> statusReportList = List.of(lg.isLicenseLoaded(), lg.isPrivateKeyLoaded(), lg.isPublicKeyLoaded(), lg.licenseRequiresSigning(), lg.licenseRequiresSaving());
		SwingUtilities.invokeLater(()->{
			for(int i=0; i<table.getRowCount(); i++) {
				table.getModel().setValueAt(statusReportList.get(i), i, 1);
			}
		});
	}
}
