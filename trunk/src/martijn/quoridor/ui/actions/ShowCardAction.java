/*
 * Created on Aug 12, 2006 
 */
package martijn.quoridor.ui.actions;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import martijn.quoridor.ui.ComboPane;

/**
 * @author Martijn van Steenbergen
 */
public class ShowCardAction extends AbstractAction {

	private ComboPane combo;

	private String card;

	public ShowCardAction(ComboPane combo, String card, String name) {
		super(name);
		this.combo = combo;
		this.card = card;
	}

	public void actionPerformed(ActionEvent e) {
		run();
	}

	public void run() {
		JPanel cards = combo.getCards();
		CardLayout layout = (CardLayout) cards.getLayout();
		layout.show(cards, card);
	}

}
