/*
 * Created on Aug 8, 2006 
 */
package martijn.quoridor.model;

/**
 * A BoardListener registered on a Board is notified of any state changes the
 * Board goes through.
 * 
 * @author Martijn van Steenbergen
 */
public interface BoardListener {

	/**
	 * Notification that a move has been executed and the state has been
	 * changed.
	 */
	public void moveExecuted(Move move);

	/**
	 * Notification that moves have been undone. The moves in the array are in
	 * the order in which they were undone.
	 */
	public void movesUndone(Move[] moves);

	/** Notification that a new game has started. */
	public void newGame();

}
