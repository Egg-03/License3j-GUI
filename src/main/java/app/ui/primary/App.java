package app.ui.primary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

public class App {

	private JFrame mainframe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		mainframe.setTitle("License3J GUI");
		mainframe.setBounds(100, 100, 945, 495);
		mainframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainframe.getContentPane().setLayout(new BorderLayout(0, 0));
		
		addMenu();
		addLicensePanel();
		addLogPanel();		
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		mainframe.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Help");
		menuBar.add(mnNewMenu);
		
		JMenu mnNewMenu_1 = new JMenu("Logs");
		menuBar.add(mnNewMenu_1);
	}

	private void addLogPanel() {
		JPanel logPanel = new JPanel();
		mainframe.getContentPane().add(logPanel, BorderLayout.SOUTH);
		logPanel.setPreferredSize(new Dimension(mainframe.getWidth(), mainframe.getHeight()/3));
		logPanel.setLayout(new GridLayout(0, 1, 0, 0));
		logPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JScrollPane scrollPane = new JScrollPane();
		logPanel.add(scrollPane);
		
		JTextArea logtextArea = new JTextArea();
		logtextArea.setEditable(false);
		logtextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane.setViewportView(logtextArea);	
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
		licenseFunctionPanel.setLayout(new MigLayout("filly", "[][grow]", "[][][][][][][]"));
		
		JButton newLicenseBtn = new JButton("New License");
		licenseFunctionPanel.add(newLicenseBtn, "cell 0 0 2 1,growx");
		
		JButton loadLicenseBtn = new JButton("Load License");
		licenseFunctionPanel.add(loadLicenseBtn, "cell 0 1,growy");
		
		JTextField loadedLicenseTf = new JTextField();
		loadedLicenseTf.setEditable(false);
		licenseFunctionPanel.add(loadedLicenseTf, "cell 1 1,grow");
		loadedLicenseTf.setColumns(10);
		
		JButton displayLicenseBtn = new JButton("Display License Information");
		licenseFunctionPanel.add(displayLicenseBtn, "cell 0 3 2 1,growx");
		
		JButton signLicenseBtn = new JButton("Sign");
		licenseFunctionPanel.add(signLicenseBtn, "cell 0 5");
		
		JButton verifyLicenseBtn = new JButton("Verify");
		licenseFunctionPanel.add(verifyLicenseBtn, "cell 1 5,alignx right");
		
		JButton saveLicenseBtn = new JButton("Save License");
		licenseFunctionPanel.add(saveLicenseBtn, "cell 0 7 2 1,growx");
		
		JPanel featurePanel = new JPanel();
		featurePanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		featurePanel.setBorder(new TitledBorder("License Feature"));
		licensePanel.add(featurePanel);
		featurePanel.setLayout(new MigLayout("filly", "[][grow]", "[][][][][][][]"));
		
		JLabel featureNameLabel = new JLabel("F.Name");
		featurePanel.add(featureNameLabel, "cell 0 0");
		
		JTextField featureNameTf = new JTextField();
		featurePanel.add(featureNameTf, "cell 1 0,grow");
		featureNameTf.setColumns(10);
		
		JLabel featureTypeLabel = new JLabel("F.Type");
		featurePanel.add(featureTypeLabel, "cell 0 1");
		
		JComboBox<String> featureTypeComboBox = new JComboBox<>();
		featurePanel.add(featureTypeComboBox, "cell 1 1,growx,aligny center");
		
		JLabel featureContentLabel = new JLabel("F.Content");
		featurePanel.add(featureContentLabel, "cell 0 2");
		
		JTextField featureContentTf = new JTextField();
		featurePanel.add(featureContentTf, "cell 1 2,grow");
		featureContentTf.setColumns(10);
		
		JButton addFeatureBtn = new JButton("Add Feature");
		featurePanel.add(addFeatureBtn, "cell 0 3 2 1,growx");
		
		JLabel machineIdLabel = new JLabel("Machine ID");
		featurePanel.add(machineIdLabel, "cell 0 5,alignx trailing");
		
		JTextField machineIdTf = new JTextField();
		machineIdTf.setEditable(false);
		featurePanel.add(machineIdTf, "cell 1 5,growx");
		machineIdTf.setColumns(10);
		
		JButton addMachineIdBtn = new JButton("Add Machine ID to Feature");
		featurePanel.add(addMachineIdBtn, "cell 0 6 2 1,growx");
		
		JPanel keygenPanel = new JPanel();
		keygenPanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		keygenPanel.setBorder(new TitledBorder("Generate or Load keys"));
		licensePanel.add(keygenPanel);
		keygenPanel.setLayout(new MigLayout("filly", "[][grow, fill][grow, fill]", "[][][][][][][][]"));
		
		JLabel algoLabel = new JLabel("Algorithm");
		keygenPanel.add(algoLabel, "cell 0 0");
		
		JLabel algoSizeLabel = new JLabel("Size");
		keygenPanel.add(algoSizeLabel, "cell 1 0");
		
		JLabel keyFormatLabel = new JLabel("Format");
		keygenPanel.add(keyFormatLabel, "cell 2 0");
		
		JComboBox<String> algoComboBox = new JComboBox<>();
		algoComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		keygenPanel.add(algoComboBox, "cell 0 1,growx");
		
		JComboBox<String> algoSizeComboBox = new JComboBox<>();
		algoSizeComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		keygenPanel.add(algoSizeComboBox, "cell 1 1,growx");
		
		JComboBox<String> keyFormatComboBox = new JComboBox<>();
		keyFormatComboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		keygenPanel.add(keyFormatComboBox, "cell 2 1,growx");
		
		JLabel privateKeyLabel = new JLabel("Private Key Name");
		keygenPanel.add(privateKeyLabel, "cell 0 2");
		
		JTextField privateKeyNameTf = new JTextField();
		keygenPanel.add(privateKeyNameTf, "cell 1 2 2 1,growx");
		privateKeyNameTf.setColumns(10);
		
		JLabel publicKeyLabel = new JLabel("Public Key Name");
		keygenPanel.add(publicKeyLabel, "cell 0 3");
		
		JTextField publicKeyNameTf = new JTextField();
		keygenPanel.add(publicKeyNameTf, "cell 1 3 2 1,growx");
		publicKeyNameTf.setColumns(10);
		
		JButton generateKeyBtn = new JButton("Generate Keys");
		keygenPanel.add(generateKeyBtn, "cell 0 4 3 1,growx");
		
		JButton loadPrivateKeyBtn = new JButton("Load Private Key");
		keygenPanel.add(loadPrivateKeyBtn, "cell 0 6,growx,aligny center");
		
		JTextField loadedPrivateKeyNameTf = new JTextField();
		loadedPrivateKeyNameTf.setEditable(false);
		keygenPanel.add(loadedPrivateKeyNameTf, "cell 1 6 2 1,grow");
		loadedPrivateKeyNameTf.setColumns(10);
		
		JButton loadPublicKeyBtn = new JButton("Load Public Key");
		keygenPanel.add(loadPublicKeyBtn, "cell 0 7,growx,aligny center");
		
		JTextField loadedPublicKeyNameTf = new JTextField();
		loadedPublicKeyNameTf.setEditable(false);
		keygenPanel.add(loadedPublicKeyNameTf, "cell 1 7 2 1,grow");
		loadedPublicKeyNameTf.setColumns(10);
	}
}
