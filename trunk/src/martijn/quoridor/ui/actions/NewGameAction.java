/*
 * Created on Aug 8, 2006 
 */
package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

/**
 * @author Martijn van Steenbergen
 */
public class NewGameAction extends AbstractAction implements BoardListener {

	private Board board;

	public NewGameAction(Board board) {
		super("New Game");
		this.board = board;
		update();
		board.addBoardListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		board.newGame();
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
		setEnabled(!board.getHistory().isEmpty());
	}

}
