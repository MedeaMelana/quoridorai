/*
 * Created on Aug 6, 2006 
 */
package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;
import martijn.quoridor.ui.GameCard;
import martijn.quoridor.ui.SetupListener;

/**
 * @author Martijn van Steenbergen
 */
public class UndoAction extends AbstractAction implements BoardListener,
		SetupListener {

	private GameCard game;

	public UndoAction(GameCard game) {
		super("Undo");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Z);
		this.game = game;
		update();
		game.getBoard().addBoardListener(this);
		game.getSetup().addSetupListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		game.getBoard().undo(getUndoLevel());
	}

	public int getUndoLevel() {
		Board board = game.getBoard();
		int number = 0;
		int turn = board.getTurnIndex();
		do {
			if (number > board.getHistory().size()) {
				return -1;
			}
			number++;
			turn--;
			if (turn < 0) {
				turn += board.getPlayers().length;
			}
		} while (!game.getSetup().getController(turn).isHuman());
		return number;
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

	private void update() {
		int n = getUndoLevel();
		setEnabled(n >= 0 && n <= game.getBoard().getHistory().size());
	}

	public void setupChanged(int player) {
		update();
	}

}
