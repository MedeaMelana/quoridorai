/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor.model;

/**
 * A move in Quoridor. After a move is done, the turn is increased. The move
 * does not remember what player performed the move, or on what board the move
 * was performed.
 * 
 * @author Martijn van Steenbergen
 */
public interface Move {

	/** Executes the move. */
	public abstract void execute(Board board);

	/** Undoes the move. */
	public abstract void undo(Board board);

	/** Returns whether this move is legal in the specified state. */
	public abstract boolean isLegal(Board board);

}
