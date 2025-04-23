package app.utilities;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.commons.io.input.TailerListenerAdapter;

/**
 * This class extends `TailerListenerAdapter` to provide a custom listener
 * for the Apache Commons IO `Tailer`. It is designed to receive new lines of text
 * from a tailed file and append them to a `JTextArea` component. This allows for
 * real-time display of log file updates within a Swing application.
 */
public class LogListener extends TailerListenerAdapter {
	
	private JTextArea ta;
	
	public LogListener(JTextArea ta) {
		this.ta=ta;
	}
	
	@Override
	public void handle(String line) {
		SwingUtilities.invokeLater(()-> ta.append(line+System.lineSeparator()));
	}
	
	@Override
	public void handle(Exception e) {
		SwingUtilities.invokeLater(()-> ta.append(e.getMessage()));
	}
}
