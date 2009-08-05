/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor;

import java.awt.Color;
import java.util.logging.Logger;

/**
 * The Core provides the Logger and some utility methods.
 * 
 * @author Martijn van Steenbergen
 */
public class Core {

	/** The Logger used for logging errors and warnings. */
	public static final Logger LOGGER = Logger.getLogger(Core.class.getName());

	/** Returns the same color but with 50% transparency. */
	public static Color transparent(Color c) {
		return transparent(c, 0x7f);
	}

	/**
	 * Returns the same color but with the specified alpha value.
	 * 
	 * @param c
	 *            the color whose RGB components are used.
	 * @param alpha
	 *            the alpha value of the created color, between 0 and 255
	 *            inclusive.
	 */
	public static Color transparent(Color c, int alpha) {
		if (alpha < 0 || alpha >= 256) {
			throw new IllegalArgumentException("Illegal alpha value " + alpha
					+ ". Must be between 0 and 255 inclusive.");
		}
		int rgb = c.getRGB() & 0x00ffffff;
		int a = alpha << 24;
		return new Color(rgb | a, true);
	}

	/** Returns whether we're on the Macintosh platform. */
	public static boolean isMac() {
		// http://java.sun.com/developer/technicalArticles/JavaLP/JavaToMac2/
		return System.getProperty("mrj.version") != null;
	}

}
