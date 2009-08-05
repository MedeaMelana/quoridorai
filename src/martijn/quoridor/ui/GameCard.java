/*
 * Created on Aug 12, 2006 
 */
package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import martijn.quoridor.brains.Brain;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.ui.actions.NewGameAction;
import martijn.quoridor.ui.actions.ShowCardAction;
import martijn.quoridor.ui.actions.UndoAction;

/**
 * @author Martijn van Steenbergen
 */
public class GameCard extends JPanel {

	public static final int HOR_STRUT = 20;

	private final Board board;

	private BoardCanvas canvas;

	private Controller[] controllers;

	private final Setup setup;

	public GameCard(ComboPane combo, BrainFactory factory) {
		board = new Board();
		canvas = new BoardCanvas(board);
		controllers = getControllers(factory, canvas);
		setup = new Setup(canvas, new Controller[] { controllers[0],
				controllers[2] });
		createGUI(combo);
		new SoundPlayer(board, setup);
	}

	private Controller[] getControllers(BrainFactory factory, BoardCanvas canvas) {
		List<Brain> brains = new ArrayList<Brain>();
		factory.addBrains(brains);

		Controller[] controllers = new Controller[brains.size() + 1];
		controllers[0] = new HumanController(canvas);
		for (int i = 0; i < brains.size(); i++) {
			controllers[i + 1] = new BrainController(canvas, brains.get(i));
		}
		return controllers;
	}

	private void createGUI(ComboPane combo) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(canvas, BorderLayout.CENTER);
		p.add(new GameStatus(this), BorderLayout.SOUTH);

		JPanel buttons = new JPanel();
		buttons.add(new JButton(new NewGameAction(board)));
		buttons.add(new JButton(new UndoAction(this)));
		buttons.add(new JButton(new ShowCardAction(combo, ComboPane.ABOUT_CARD,
				"About")));

		setLayout(new BorderLayout());
		add(p, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
	}

	public Board getBoard() {
		return board;
	}

	public Setup getSetup() {
		return setup;
	}

	public Controller[] getControllers() {
		return controllers;
	}

}
