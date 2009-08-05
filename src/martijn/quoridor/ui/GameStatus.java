/*
 * Created on Aug 12, 2006 
 */
package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

/**
 * @author Martijn van Steenbergen
 */
public class GameStatus extends JPanel implements BoardListener, SetupListener {

	private PlayerStatus[] lines;

	private GameCard game;

	public GameStatus(GameCard game) {
		this.game = game;
		createGUI();
		getBoard().addBoardListener(this);
		getSetup().addSetupListener(this);
	}

	private Setup getSetup() {
		return game.getSetup();
	}

	private Board getBoard() {
		return game.getBoard();
	}

	private void createGUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 10, 0, 10);

		lines = new PlayerStatus[getBoard().getPlayers().length];
		for (int i = 0; i < getBoard().getPlayers().length; i++) {
			lines[i] = new PlayerStatus(game, i);
			lines[i].createGUI(this, gbc);
		}
	}

	public void moveExecuted(Move move) {
		update();
	}

	public void movesUndone(Move[] moves) {
		update();
	}

	public void newGame() {
		update();
	}

	public void setupChanged(int player) {
		update();
	}

	private void update() {
		for (PlayerStatus s : lines) {
			s.update();
		}
	}

}
