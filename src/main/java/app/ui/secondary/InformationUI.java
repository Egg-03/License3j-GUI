package app.ui.secondary;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class InformationUI extends JFrame {
	private static final long serialVersionUID = -5429418908213999779L;

	public InformationUI(String infoLabel) {
		super("Information");
		setAlwaysOnTop(true);
		initialize(infoLabel);
	}
	
	private void initialize(String infoLabel) {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(InformationUI.class.getResource("/icons/icon_main.png")));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 140);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Information",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 464, 79);
		getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnOk = new JButton("Dismiss");
		btnOk.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		btnOk.setBounds(180, 53, 105, 17);
		btnOk.addActionListener(e -> this.dispose());
		panel.add(btnOk);

		JTextField informationLabel = new JTextField(infoLabel);
		informationLabel.setEditable(false);
		informationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		informationLabel.setBounds(10, 18, 444, 24);
		panel.add(informationLabel);
	}

}
