/*
 * Created on Aug 11, 2006 
 */
package martijn.quoridor.model;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Martijn van Steenbergen
 */
public class GoalSeeker implements Comparator<Position> {

	private Player player;

	private Orientation[][] from;

	private int[][] distance;

	private Orientation[] path;

	public GoalSeeker(Player player) {
		if (player == null) {
			throw new NullPointerException("Player is null.");
		}
		this.player = player;

		if (player.isWinner()) {
			// Special case: we're already at a goal position.
			path = new Orientation[0];
			return;
		}

		int w = getBoard().getWidth();
		int h = getBoard().getHeight();
		from = new Orientation[w][h];
		distance = new int[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				distance[x][y] = Integer.MAX_VALUE;
			}
		}

		Queue<Position> front = new PriorityQueue<Position>(8, this);
		Position pos = player.getPosition();
		distance[pos.getX()][pos.getY()] = 0;
		front.add(player.getPosition());

		int counter = 0;

		while (!front.isEmpty()) {
			counter++;
			pos = front.remove();
			int x = pos.getX();
			int y = pos.getY();
			for (Orientation o : Orientation.values()) {
				if (getBoard().isBlocked(pos, o)) {
					continue;
				}
				Position pos2 = pos.move(o);
				int x2 = pos2.getX();
				int y2 = pos2.getY();
				if (distance[x2][y2] > distance[x][y] + 1) {
					distance[x2][y2] = distance[x][y] + 1;
					from[x2][y2] = o.opposite();
					front.add(pos2);

					if (player.isGoal(pos2)) {
						// We've found a goal position. Build and return path.
						path = buildPath(pos2);
						return;
					}
				}
			}
		}
	}

	private Orientation[] buildPath(Position to) {
		Orientation[] path = new Orientation[distance[to.getX()][to.getY()]];
		int i = path.length;
		while (!to.equals(player.getPosition())) {
			path[--i] = to.visit(from).opposite();
			to = to.move(to.visit(from));
		}
		return path;
	}

	public Orientation[] getPath() {
		return path;
	}

	public Board getBoard() {
		return player.getBoard();
	}

	private int f(Position pos) {
		return g(pos) + h(pos);
	}

	private int g(Position pos) {
		return distance[pos.getX()][pos.getY()];
	}

	private int h(Position pos) {
		Position goal;
		switch (player.getOrientation()) {
		case NORTH:
			goal = new Position(pos.getX(), 0);
			break;
		case EAST:
			goal = new Position(0, pos.getY());
			break;
		case SOUTH:
			goal = new Position(pos.getX(), getBoard().getHeight() - 1);
			break;
		case WEST:
			goal = new Position(getBoard().getWidth() - 1, pos.getY());
			break;
		default:
			throw new InternalError();
		}
		return pos.manhattan(goal);
	}

	// private int h(Position pos) {
	// switch (player.getOrientation()) {
	// case NORTH:
	// return pos.getY() - 1;
	// case EAST:
	// return pos.getX() - 1;
	// case SOUTH:
	// return getBoard().getHeight() - pos.getY() - 1;
	// case WEST:
	// return getBoard().getWidth() - pos.getX() - 1;
	// default:
	// throw new InternalError();
	// }
	// }

	public int compare(Position p1, Position p2) {
		return f(p1) - f(p2);
	}

}
