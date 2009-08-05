/*
 * Created on Aug 19, 2006 
 */
package martijn.quoridor;

import martijn.quoridor.ui.ComboPane;
import martijn.quoridor.ui.actions.ShowCardAction;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.ApplicationListener;

/**
 * @author Martijn van Steenbergen
 */
public class OSXAdapter implements ApplicationListener {

	private ComboPane combo;

	public OSXAdapter(ComboPane combo) {
		this.combo = combo;
	}

	public void handleAbout(ApplicationEvent e) {
		new ShowCardAction(combo, ComboPane.ABOUT_CARD, "About").run();
		e.setHandled(true);
	}

	public void handleOpenApplication(ApplicationEvent e) {
	}

	public void handleOpenFile(ApplicationEvent e) {
	}

	public void handlePreferences(ApplicationEvent e) {
	}

	public void handlePrintFile(ApplicationEvent e) {
	}

	public void handleQuit(ApplicationEvent e) {
		System.exit(0);
	}

	public void handleReOpenApplication(ApplicationEvent e) {
	}

	public static void register(ComboPane combo) {
		Application a = Application.getApplication();
		a.addApplicationListener(new OSXAdapter(combo));
	}

}
