/*
 * Created on Aug 9, 2006 
 */
package martijn.quoridor.ui;

import java.util.logging.Level;

import javax.swing.JOptionPane;

import martijn.quoridor.Core;
import martijn.quoridor.brains.Brain;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;

/**
 * @author Martijn van Steenbergen
 */
public class BrainController extends Controller {

	private Brain brain;

	private Thread thinker;

	/**
	 * A number that identifies the current move for this controller. The
	 * thinker thread uses it to check if it is still its turn when
	 * {@link Brain#getMove(Board)} returns.
	 */
	private int controllerMove;

	private long minimumThinkTime;

	public BrainController(BoardCanvas canvas, Brain brain) {
		this(canvas, brain, 500);
	}

	public BrainController(BoardCanvas canvas, Brain brain,
			long minimumThinkTime) {
		super(canvas);
		this.brain = brain;
		this.minimumThinkTime = minimumThinkTime;
	}

	@Override
	protected synchronized void moveExpected() {
		controllerMove++;
		thinker = new Thread(new ThinkRunnable(controllerMove), brain.getName());
		thinker.setDaemon(true);
		thinker.setPriority(Thread.MIN_PRIORITY);
		thinker.start();
	}

	@Override
	protected synchronized void moveCancelled() {
		if (thinker != null) {
			controllerMove++;
			thinker.interrupt();
		}
	}

	@Override
	public boolean isHuman() {
		return false;
	}

	private class ThinkRunnable implements Runnable {

		private int thinkerMove;

		public ThinkRunnable(int thinkerMove) {
			this.thinkerMove = thinkerMove;
		}

		public void run() {
			try {
				// Record the current time.
				long time = System.currentTimeMillis();

				// Activate brain.
				Move move;
				try {
					move = brain.getMove(getBoard().clone());
				} catch (RuntimeException e) {
					Core.LOGGER.log(Level.WARNING, brain.getName()
							+ " threw an unchecked Exception", e);
					showError(brain.getName() + " threw an error",
							"An error occurred while " + brain.getName()
									+ " was computing its\nmove. "
									+ "Please select a different brain for "
									+ getBoard().getTurn() + ".");
					return;
				}

				// Check whether move is valid.
				if (move == null || !move.isLegal(getBoard())) {
					if (move == null) {
						Core.LOGGER.log(Level.WARNING, brain.getName()
								+ " returned a null move.");
					} else {
						Core.LOGGER.log(Level.WARNING, brain.getName()
								+ " returned illegal move " + move + ".");
					}
					showError(brain.getName() + " suggested illegal move",
							brain.getName()
									+ " suggested a move that is illegal in "
									+ "the current\nsituation. Please "
									+ "select a different brain for "
									+ getBoard().getTurn() + ".");
					return;
				}

				// Wait if necessary.
				long wait = minimumThinkTime
						- (System.currentTimeMillis() - time);
				if (wait > 0) {
					Thread.sleep(wait);
				}

				// Move if it is still our turn.
				synchronized (BrainController.this) {
					if (thinkerMove == controllerMove) {
						move(move);
					}
				}
			} catch (InterruptedException e) {
				// We were cancelled.
				return;
			} finally {
				synchronized (BrainController.this) {
					if (thinker == Thread.currentThread()) {
						thinker = null;
					}
				}
			}
		}
	}

	private void showError(String title, String message) {
		JOptionPane.showMessageDialog(getCanvas(), message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public String toString() {
		return brain.getName();
	}

}
