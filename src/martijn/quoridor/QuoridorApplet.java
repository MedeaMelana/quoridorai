/*
 * Created on Aug 31, 2006 
 */
package martijn.quoridor;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JApplet;
import javax.swing.border.LineBorder;

import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.ui.ComboPane;

/**
 * @author Martijn van Steenbergen
 */
public class QuoridorApplet extends JApplet {

	public void init() {
		setLayout(new BorderLayout());
		ComboPane cp = new ComboPane(new DefaultBrainFactory());
		cp.setBorder(new LineBorder(Color.BLACK, 1));
		add(cp, BorderLayout.CENTER);
	}

}
