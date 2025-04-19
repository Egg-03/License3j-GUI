package app.ui.primary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class App {

	private JFrame frmLicensejGui;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frmLicensejGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
		frmLicensejGui = new JFrame();
		frmLicensejGui.setTitle("License3J GUI");
		frmLicensejGui.setBounds(100, 100, 801, 463);
		frmLicensejGui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frmLicensejGui.getContentPane().setLayout(new BorderLayout(0, 0));
		
		addMenu();
		addLicensePanel();
		addLogPanel();		
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		frmLicensejGui.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		JMenu mnNewMenu_1 = new JMenu("New menu");
		menuBar.add(mnNewMenu_1);
	}

	private void addLogPanel() {
		JPanel logPanel = new JPanel();
		frmLicensejGui.getContentPane().add(logPanel, BorderLayout.SOUTH);
		logPanel.setPreferredSize(new Dimension(frmLicensejGui.getWidth(), frmLicensejGui.getHeight()/3));
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
		frmLicensejGui.getContentPane().add(licensePanel, BorderLayout.CENTER);
		licensePanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "License", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		licensePanel.setLayout(new BoxLayout(licensePanel, BoxLayout.X_AXIS));
		
		JPanel licenseFunctionPanel = new JPanel();
		licenseFunctionPanel.setBorder(new TitledBorder("License Functions"));
		licensePanel.add(licenseFunctionPanel);
		licenseFunctionPanel.setLayout(new GridLayout(4, 2, 0, 0));
		
		
		JPanel featureAndKeysPanel = new JPanel();
		featureAndKeysPanel.setBorder(new TitledBorder("License Feature and Sign Keys"));
		licensePanel.add(featureAndKeysPanel);
		
		JPanel signAndVerifyPanel = new JPanel();
		signAndVerifyPanel.setBorder(new TitledBorder("Sign and Verify"));
		licensePanel.add(signAndVerifyPanel);
		
	}
}
