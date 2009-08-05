/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * A player participating in a game of Quoridor.
 * 
 * @author Martijn van Steenbergen
 */
public class Player {

	private Board board;

	private Orientation orientation;

	private Position position;

	private String name;

	private int nwalls;

	private Color color;

	/**
	 * Creates a new player.
	 * 
	 * @param board
	 *            The board this player is part of.
	 * @param orientation
	 *            The player's orientation. See {@link #getOrientation()}.
	 * @param name
	 *            The player's name.
	 * @param nwalls
	 *            The number of walls this player owns initially.
	 * @param color
	 *            The player's color.
	 */
	public Player(Board board, Orientation orientation, String name,
			int nwalls, Color color) {
		if (board == null) {
			throw new NullPointerException("Board is null.");
		}
		this.board = board;
		this.orientation = orientation;
		this.name = name;
		this.nwalls = nwalls;
		this.color = color;
		this.position = getInitialPosition(board, orientation);
	}

	public static Position getInitialPosition(Board board, Orientation o) {
		int x = board.getWidth() / 2;
		int y = board.getHeight() / 2;
		switch (o) {
		case NORTH:
			return new Position(x, board.getHeight() - 1);
		case EAST:
			return new Position(board.getWidth() - 1, y);
		case SOUTH:
			return new Position(x, 0);
		case WEST:
			return new Position(0, y);
		default:
			throw new InternalError();
		}
	}

	/**
	 * Creates a player on the specified board that is a clone of the specified
	 * player.
	 */
	public Player(Board board, Player player) {
		this.board = board;
		this.color = player.color;
		this.name = player.name;
		this.nwalls = player.nwalls;
		this.orientation = player.orientation;
		this.position = player.position;
	}

	/**
	 * Returns this player's orientation on the board. The orientation
	 * determines the player's starting position and goal positions.
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/** Returns the board this player participates in. */
	public Board getBoard() {
		return board;
	}

	/** Returns a number i such that {@code getBoard().getPlayers()[i] == this}. */
	public int getIndex() {
		for (int i = 0; i < board.getPlayers().length; i++) {
			if (board.getPlayers()[i] == this) {
				return i;
			}
		}
		throw new IllegalStateException(
				"This player is not participating in this player's board.");
	}

	/** Returns the number of walls this player owns. */
	public int getWallCount() {
		return nwalls;
	}

	/** Takes a wall from this player. */
	public void takeWall() {
		if (nwalls == 0) {
			throw new IllegalStateException("Player has no walls left.");
		}
		nwalls--;
	}

	/** Gives a wall to this player. */
	public void giveWall() {
		nwalls++;
	}

	/** Returns whether the player wins if it reaches the specified position. */
	public boolean isGoal(Position p) {
		switch (orientation) {
		case NORTH:
			return p.getY() == 0;
		case EAST:
			return p.getX() == 0;
		case SOUTH:
			return p.getY() == board.getHeight() - 1;
		case WEST:
			return p.getX() == board.getWidth() - 1;
		default:
			throw new InternalError();
		}
	}

	/** Returns whether this player has won. */
	public boolean isWinner() {
		return isGoal(getPosition());
	}

	/** Returns whether it's this player's turn. */
	public boolean isTurn() {
		return board.getTurn() == this;
	}

	/** Returns the player's current position. */
	public Position getPosition() {
		return position;
	}

	/** Sets the player's position. */
	public void setPosition(Position position) {
		this.position = position;
	}

	/** Returns this player's color. */
	public Color getColor() {
		return color;
	}

	/**
	 * Finds a goal closest to the player's current position and returns a
	 * shortest path to it. The path does not take into account what positions
	 * are blocked by other players.
	 */
	public Orientation[] findGoal() {
		return new GoalSeeker(this).getPath();
	}

	/**
	 * Returns a position one jump away from the player's current position,
	 * closer to a goal cell.
	 */
	public Position stepToGoal() {
		Position step = null;
		int best = Integer.MAX_VALUE;
		Position old = getPosition();
		for (Position pos : getJumpPositions()) {
			setPosition(pos);
			int d = findGoal().length;
			if (d < best) {
				best = d;
				step = pos;
			}
		}
		setPosition(old);
		return step;
	}

	/**
	 * Returns all positions this player can jump to given the board's current
	 * state.
	 */
	public Set<Position> getJumpPositions() {
		// The set that is going to contain all legal new positions.
		Set<Position> legal = new TreeSet<Position>();

		// A blacklist of positions that we won't visit again.
		Set<Position> illegal = new TreeSet<Position>();

		// The queue containing the positions we're about to visit.
		Queue<Position> front = new LinkedList<Position>();
		front.add(getPosition());

		while (!front.isEmpty()) {
			// Get position from queue.
			Position pos = front.remove();

			if (board.isTaken(pos)) {
				// This position is taken by a player. Spread 1 unit in all
				// unblocked directions.

				// Never visit this position again.
				illegal.add(pos);

				for (Orientation o : Orientation.values()) {
					if (!board.isBlocked(pos, o)) {
						Position p2 = pos.move(o);
						if (!illegal.contains(p2)) {
							// Visit this position soon.
							front.add(p2);
						}
					}
				}
			} else {
				// This is a legal move. Don't spread.
				legal.add(pos);
			}
		}
		return legal;
	}

	/** Returns this player's name. */
	public String getName() {
		return name;
	}

	/** Sets this player's name. */
	public void setName(String name) {
		this.name = name;
	}

	/** Returns this player's name. */
	public String toString() {
		return getName();
	}

}
