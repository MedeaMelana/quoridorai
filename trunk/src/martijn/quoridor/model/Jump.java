/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor.model;

/**
 * A jump move.
 * 
 * @author Martijn van Steenbergen
 */
public class Jump implements Move {

	private Position oldPosition;

	private Position newPosition;

	/**
	 * Creates a move representing the active player's jump to
	 * {@code newPosition}.
	 */
	public Jump(Position newPosition) {
		this.newPosition = newPosition;
	}

	/** Returns the player's position after the jump. */
	public Position getNewPosition() {
		return newPosition;
	}

	/**
	 * Returns the player's position before the jump. Returns null if the move
	 * has never been executed yet.
	 */
	public Position getOldPosition() {
		return oldPosition;
	}

	public void execute(Board board) {
		Player p = board.getTurn();
		oldPosition = p.getPosition();
		p.setPosition(newPosition);
	}

	public void undo(Board board) {
		board.getTurn().setPosition(oldPosition);
	}

	public boolean isLegal(Board board) {
		return !board.isGameOver()
				&& board.getTurn().getJumpPositions().contains(newPosition);
	}

	public String toString() {
		return "Jump to " + getNewPosition();
	}

}
