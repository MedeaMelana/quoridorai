/*
 * Created on Aug 9, 2006 
 */
package martijn.quoridor.ui;

import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

/**
 * A Controller controls a specific player.
 * 
 * @author Martijn van Steenbergen
 */
public abstract class Controller implements BoardListener {

	private BoardCanvas canvas;

	private List<Integer> controlling;

	private boolean expecting;

	/** Creates a new Controller. */
	public Controller(BoardCanvas canvas) {
		if (canvas == null) {
			throw new NullPointerException("Canvas is null.");
		}
		this.canvas = canvas;
		controlling = new LinkedList<Integer>();
		getBoard().addBoardListener(this);
	}

	public BoardCanvas getCanvas() {
		return canvas;
	}

	/** Returns the board. */
	public Board getBoard() {
		return canvas.getBoard();
	}

	/**
	 * Activates the controller. The controller will prepare to move immediately
	 * if it's its player's turn.
	 */
	public void startControlling(int player) {
		controlling.add(player);
		wake();
	}

	/** Deactivates the controller. */
	public void stopControlling(int player) {
		controlling.remove(new Integer(player));
		wake();
	}

	public void moveExecuted(Move move) {
		stopExpecting();
		wake();
	}

	public void movesUndone(Move[] moves) {
		stopExpecting();
		wake();
	}

	public void newGame() {
		stopExpecting();
		wake();
	}

	/** Calls {@link #moveExpected()} if it's {@link #getPlayer()}'s turn. */
	private void wake() {
		if (shouldExpect()) {
			startExpecting();
		} else {
			stopExpecting();
		}
	}

	public boolean shouldExpect() {
		return controlling.contains(getBoard().getTurnIndex())
				&& !getBoard().isGameOver();
	}

	/**
	 * Returns whether the controller is expecting. That is, whether it is the
	 * controller's player's turn, and the controller hasn't made a move yet.
	 */
	public boolean isExpecting() {
		return expecting;
	}

	private synchronized void startExpecting() {
		if (!expecting) {
			expecting = true;
			moveExpected();
		}
	}

	private synchronized void stopExpecting() {
		if (expecting) {
			expecting = false;
			moveCancelled();
		}
	}

	/**
	 * Notification that it has become this controller's player's turn and that
	 * the controller should make a move. The implementation should exit
	 * immediately. When the controller has decided which move to execute, it
	 * should call {@link #move(Move)} rather than {@link Board#move(Move)}
	 * directly.
	 */
	protected abstract void moveExpected();

	/**
	 * Notification that it's no longer the controller's turn. This will only be
	 * called if the controller was expected to execute a move.
	 */
	protected abstract void moveCancelled();

	protected void move(Move move) {
		expecting = false;
		getBoard().move(move);
	}

	/** Returns whether the controller is human. */
	public abstract boolean isHuman();

}
