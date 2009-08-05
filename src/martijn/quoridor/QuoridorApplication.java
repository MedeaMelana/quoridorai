/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import javax.swing.JFrame;

import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.ui.ComboPane;

/**
 * The application's main entry point.
 * 
 * @author Martijn van Steenbergen
 */
public class QuoridorApplication {

	/** Launches Quoridor with a {@link DefaultBrainFactory}. */
	public static void launch() {
		launch(new DefaultBrainFactory());
	}

	/** Launches Quoridor with the brains created by the specified factory. */
	public static void launch(BrainFactory factory) {
		JFrame f = new JFrame("Quoridor");
		ComboPane combo = new ComboPane(factory);
		f.setContentPane(combo);
		f.setSize(400, 500);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (Core.isMac()) {
			registerAdapter(combo);
		}
	}

	/** Launches Quoridor. */
	public static void main(String[] args) {
		launch();
	}

	private static void registerAdapter(ComboPane combo) {
		try {
			Class adapter = Class.forName("martijn.quoridor.OSXAdapter");
			Method method = adapter.getMethod("register", ComboPane.class);
			method.invoke(null, combo);
		} catch (SecurityException e) {
			Core.LOGGER.log(Level.WARNING, "Failed to register OSXAdapter.", e);
		} catch (IllegalArgumentException e) {
			Core.LOGGER.log(Level.WARNING, "Failed to register OSXAdapter.", e);
		} catch (ClassNotFoundException e) {
			Core.LOGGER.log(Level.WARNING, "Failed to register OSXAdapter.", e);
		} catch (NoSuchMethodException e) {
			Core.LOGGER.log(Level.WARNING, "Failed to register OSXAdapter.", e);
		} catch (IllegalAccessException e) {
			Core.LOGGER.log(Level.WARNING, "Failed to register OSXAdapter.", e);
		} catch (InvocationTargetException e) {
			Core.LOGGER.log(Level.WARNING, "Failed to register OSXAdapter.", e);
		}
	}

}
