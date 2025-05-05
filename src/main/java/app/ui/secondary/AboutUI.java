package app.ui.secondary;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;

import org.tinylog.Logger;

import app.ui.primary.App;
import app.utilities.MarkdownToHtml;
import app.utilities.VersionAndOtherInfo;

public class AboutUI extends JFrame {
	
	@Serial
	private static final long serialVersionUID = -5925195455864099479L;

    /**
	 * Create the frame.
	 */
	public AboutUI() {
		setTitle("About License3J GUI");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(App.class.getResource("/icons/logo.png")));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 620, 420);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel logoPanel = new JPanel();
		contentPane.add(logoPanel, BorderLayout.WEST);
		logoPanel.setLayout(new GridLayout(1, 0, 0, 0));
		logoPanel.setBorder(new TitledBorder("Logo"));
		
		try {
			BufferedImage wPic = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/icons/logo.png")));
			JLabel logo = new JLabel(new ImageIcon(wPic));
			logoPanel.add(logo);
		} catch (IOException e) {
			Logger.error(e);
		}
		
		
		JPanel aboutPanel = new JPanel();
		contentPane.add(aboutPanel, BorderLayout.CENTER);
		aboutPanel.setBorder(new TitledBorder("About"));
		aboutPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel versionPanel = new JPanel();
		aboutPanel.add(versionPanel, BorderLayout.NORTH);
		versionPanel.setLayout(new GridLayout(1, 0, 0, 0));
		versionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JLabel appVersionLabel = new JLabel("App Version: "+VersionAndOtherInfo.APP_VERSION);
		versionPanel.add(appVersionLabel);
		
		JLabel licenseThreeJVersionLabel = new JLabel("License3J Version: "+VersionAndOtherInfo.LICENSE3J_VERSION);
		versionPanel.add(licenseThreeJVersionLabel);
		
		JPanel descriptionPanel = new JPanel();
		aboutPanel.add(descriptionPanel, BorderLayout.CENTER);
		descriptionPanel.setLayout(new GridLayout(1, 0, 0, 0));
		descriptionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JScrollPane scrollPane = new JScrollPane();
		descriptionPanel.add(scrollPane);
		
		JEditorPane descriptionPane = new JEditorPane();
		scrollPane.setViewportView(descriptionPane);
		descriptionPane.setContentType("text/html");
		descriptionPane.setText(MarkdownToHtml.parse(VersionAndOtherInfo.ABOUT));
		descriptionPane.setCaretPosition(0);
		descriptionPane.setEditable(false);
		descriptionPane.addHyperlinkListener(e->{
			if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
				try {
					Desktop.getDesktop().browse(e.getURL().toURI());
				} catch (IOException | URISyntaxException e1) {
					Logger.error(e);
				}
			}
		});
	}

}
