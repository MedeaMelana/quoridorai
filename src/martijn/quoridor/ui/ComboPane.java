/*
 * Created on Aug 12, 2006 
 */
package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import martijn.quoridor.brains.BrainFactory;

/**
 * @author Martijn van Steenbergen
 */
public class ComboPane extends JPanel {

	public static final String GAME_CARD = "game";

	public static final String ABOUT_CARD = "about";

	private JPanel cards;

	public ComboPane(BrainFactory factory) {
		createGUI(factory);
	}

	private void createGUI(BrainFactory factory) {
		CardLayout cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		cards.add(new GameCard(this, factory), GAME_CARD);
		cards.add(new AboutCard(this), ABOUT_CARD);

		JLabel copyright = new JLabel("(CC) 2006 Martijn van Steenbergen");
		copyright.setHorizontalAlignment(SwingConstants.CENTER);
		copyright.setBorder(new EmptyBorder(0, 0, 5, 0));

		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);
		add(copyright, BorderLayout.SOUTH);
	}

	public JPanel getCards() {
		return cards;
	}

}
