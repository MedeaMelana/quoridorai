/*
 * Created on Aug 9, 2006 
 */
package martijn.quoridor.brains;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;

/**
 * DumbBrain always tries to move towards the nearest goal. It never places a
 * wall.
 * 
 * @author Martijn van Steenbergen
 */
public class DumbBrain extends Brain {

	@Override
	public Move getMove(Board board) {
		return new Jump(board.getTurn().stepToGoal());
	}

}
