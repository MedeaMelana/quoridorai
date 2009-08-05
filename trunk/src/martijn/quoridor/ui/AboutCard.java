/*
 * Created on Aug 12, 2006 
 */
package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import martijn.quoridor.Core;
import martijn.quoridor.ui.actions.ShowCardAction;

/**
 * The About card in the application. Shows the contents of the about file,
 * including the build date.
 * 
 * @author Martijn van Steenbergen
 */
public class AboutCard extends JPanel {

	/** The ComboPane that holds the cards. */
	private ComboPane combo;

	/** Creates a new AboutCard. */
	public AboutCard(ComboPane combo) {
		this.combo = combo;
		createGUI();
	}

	/** Creates the GUI. */
	private void createGUI() {
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setText(getAboutText());
		area.setEditable(false);
		JScrollPane scroller = new JScrollPane(area);

		JButton ok = new JButton(new ShowCardAction(combo, ComboPane.GAME_CARD,
				"Back"));

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(Box.createHorizontalGlue());
		buttons.add(ok);
		buttons.add(Box.createHorizontalStrut(GameCard.HOR_STRUT));

		setLayout(new BorderLayout());
		add(scroller, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
	}

	/** Reads the about text from the about file. */
	public static String getAboutText() {
		try {
			String about = read(AboutCard.class.getResourceAsStream("about"));
			if (about == null) {
				Core.LOGGER.log(Level.WARNING, "Could not find about file.");
			}
			return about;
		} catch (IOException e) {
			Core.LOGGER.log(Level.WARNING, "Could not read about file.", e);
			return null;
		}
	}

	/**
	 * Returns the contents of the input stream as a String. Returns
	 * <code>null</code> if the <code>InputStream</code> was
	 * <code>null</code>.
	 */
	public static String read(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(in);
		char[] buf = new char[1024];
		int read = isr.read(buf);
		while (read != -1) {
			builder.append(buf, 0, read);
			read = isr.read(buf);
		}

		return builder.toString();
	}

}
