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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.Window.Type;

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
		frmLicensejGui.setResizable(false);
		frmLicensejGui.setTitle("License3J GUI");
		frmLicensejGui.setBounds(100, 100, 900, 480);
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
		licenseFunctionPanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		licenseFunctionPanel.setBorder(new TitledBorder("License Functions"));
		licensePanel.add(licenseFunctionPanel);
		
		JButton btnNewButton = new JButton("New License");
		
		JButton btnNewButton_1 = new JButton("Load License");
		
		JTextField textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("Save License");
		
		JButton btnNewButton_3 = new JButton("Print License");
		GroupLayout gl_licenseFunctionPanel = new GroupLayout(licenseFunctionPanel);
		gl_licenseFunctionPanel.setHorizontalGroup(
			gl_licenseFunctionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_licenseFunctionPanel.createSequentialGroup()
					.addGroup(gl_licenseFunctionPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
						.addGroup(gl_licenseFunctionPanel.createSequentialGroup()
							.addComponent(btnNewButton_1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
						.addComponent(btnNewButton_2, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
						.addComponent(btnNewButton_3, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_licenseFunctionPanel.setVerticalGroup(
			gl_licenseFunctionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_licenseFunctionPanel.createSequentialGroup()
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_licenseFunctionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton_1)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton_2)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton_3)
					.addContainerGap(79, Short.MAX_VALUE))
		);
		licenseFunctionPanel.setLayout(gl_licenseFunctionPanel);
		
		
		JPanel featurePanel = new JPanel();
		featurePanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		featurePanel.setBorder(new TitledBorder("License Feature"));
		licensePanel.add(featurePanel);
		
		JLabel lblNewLabel = new JLabel("F.Name");
		JTextField textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("F.Type");
		JComboBox<String> comboBox = new JComboBox<>();
		
		JLabel lblNewLabel_2 = new JLabel("F.Content");
		JTextField textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("Add Feature");
		
		JLabel lblNewLabel_3 = new JLabel("MachineID");
		JTextField textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		
		JButton btnNewButton_5 = new JButton("Add MachineID to Feature");
		
		GroupLayout gl_featureAndKeysPanel = new GroupLayout(featurePanel);
		gl_featureAndKeysPanel.setHorizontalGroup(
			gl_featureAndKeysPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_featureAndKeysPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_5, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
						.addGroup(gl_featureAndKeysPanel.createSequentialGroup()
							.addComponent(lblNewLabel_3)
							.addGap(18)
							.addComponent(textField_3, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
						.addComponent(btnNewButton_4, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
						.addGroup(gl_featureAndKeysPanel.createSequentialGroup()
							.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_2)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addGap(18)
							.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, Alignment.TRAILING, 0, 164, Short.MAX_VALUE)
								.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
								.addComponent(textField_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_featureAndKeysPanel.setVerticalGroup(
			gl_featureAndKeysPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_featureAndKeysPanel.createSequentialGroup()
					.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_2)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_4)
					.addGap(18)
					.addGroup(gl_featureAndKeysPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_5)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		featurePanel.setLayout(gl_featureAndKeysPanel);
		
		JPanel keyGenAndSignPanel = new JPanel();
		keyGenAndSignPanel.setPreferredSize(new Dimension(licensePanel.getWidth()/3, licensePanel.getHeight()));
		keyGenAndSignPanel.setBorder(new TitledBorder("Generate Sign Keys, Sign and Verify License"));
		licensePanel.add(keyGenAndSignPanel);
		
	}
}
