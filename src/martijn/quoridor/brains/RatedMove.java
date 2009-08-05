/*
 * Created on Aug 31, 2006 
 */
package martijn.quoridor.brains;

import martijn.quoridor.model.Move;

/**
 * @author Martijn van Steenbergen
 */
public class RatedMove implements Comparable {

	private Move move;

	private int rating;

	public RatedMove(Move move, int rating) {
		super();
		this.move = move;
		this.rating = rating;
	}

	public Move getMove() {
		return move;
	}

	public int getRating() {
		return rating;
	}

	public boolean equals(Object o) {
		if (!(o instanceof RatedMove)) {
			return false;
		}
		RatedMove rm = (RatedMove) o;
		return move.equals(rm.move) && rating == rm.rating;
	}

	public int compareTo(Object o) {
		RatedMove rm = (RatedMove) o;
		return rm.rating - rating;
	}

	public String toString() {
		return move + " @ " + rating;
	}

}
