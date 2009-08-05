/*
 * Created on Aug 9, 2006 
 */
package martijn.quoridor.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import martijn.quoridor.Core;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Position;
import martijn.quoridor.model.PutWall;
import martijn.quoridor.model.Wall;

/**
 * @author Martijn van Steenbergen
 */
public class HumanController extends Controller {

	private MouseMotionListener hoverListener;

	private MouseListener clickListener;

	public HumanController(BoardCanvas canvas) {
		super(canvas);
		hoverListener = new HoverListener();
		clickListener = new ClickListener();
	}

	@Override
	protected void moveExpected() {
		// Start user interaction.
		getCanvas().addMouseMotionListener(hoverListener);
		getCanvas().addMouseListener(clickListener);
	}

	@Override
	protected void moveCancelled() {
		removeListeners();
	}

	private void removeListeners() {
		getCanvas().removeMouseMotionListener(hoverListener);
		getCanvas().removeMouseListener(clickListener);
	}

	@Override
	public boolean isHuman() {
		return true;
	}

	/**
	 * Converts from pixel coordinates to board coordinates. Returns
	 * {@code null} if something went wrong.
	 */
	private Point2D toBoardCoordinates(Point mouseLocation) {
		if (mouseLocation == null) {
			return null;
		}
		Point2D.Double p = new Point2D.Double();
		AffineTransform transform = getCanvas().getPaintTransformation();
		if (transform == null) {
			return null;
		}
		try {
			transform.inverseTransform(mouseLocation, p);
			return p;
		} catch (NoninvertibleTransformException e) {
			Core.LOGGER.log(Level.WARNING,
					"Could not convert mouse location to board location.", e);
			return null;
		}
	}

	/**
	 * Updates the shadow based on the current mouse position.
	 * 
	 * @param mousePosition
	 *            the mouse position in board coordinates.
	 */
	@SuppressWarnings("unchecked")
	private void updateShadow(Point2D mousePosition) {
		if (mousePosition == null) {
			getCanvas().setShadow(null);
			return;
		}

		Board board = getBoard();

		if (board.isGameOver()) {
			return;
		}

		List<Move> moves = new LinkedList<Move>();

		// Are we trying to jump?
		int x = (int) Math.round(mousePosition.getX() * 2);
		int y = (int) Math.round(mousePosition.getY() * 2);
		boolean jump = x % 2 == 1 && y % 2 == 1;
		if (jump) {
			Position pos = new Position(x / 2, y / 2);
			Move move = new Jump(pos);
			if (move.isLegal(board)) {
				getCanvas().setShadow(move);
			}
			return;
		}

		// Determine whether we're trying to place a horizontal or a vertical
		// wall. Thanks to John Farrell (friendless.farrell@gmail.com), 18
		// January 2007.
		double rx = (mousePosition.getX() + .5) % 1;
		double ry = (mousePosition.getY() + .5) % 1;
		Wall wall = (ry < rx) ^ (ry < 1 - rx) ? Wall.HORIZONTAL : Wall.VERTICAL;

		x = (int) Math.round(mousePosition.getX()) - 1;
		y = (int) Math.round(mousePosition.getY()) - 1;
		Position pos = new Position(x, y);

		moves.add(new PutWall(pos, wall));
		moves.add(new PutWall(pos, wall.flip()));

		for (Move move : moves) {
			if (move.isLegal(board)) {
				getCanvas().setShadow(move);
				return;
			}
		}

		// No valid move found for the current mouse position.
		getCanvas().setShadow(null);
	}

	/** Applies the shadow and ends the turn, if possible. */
	private void applyShadow() {
		if (getCanvas().isShadowLegal()) {
			removeListeners();
			move(getCanvas().getShadow());
		}
	}

	/** Listens to mouse movements. */
	private class HoverListener implements MouseMotionListener {

		public void mouseMoved(MouseEvent e) {
			updateShadow(toBoardCoordinates(e.getPoint()));
		}

		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
		}

	}

	/** Listens to mouse clicks. */
	private class ClickListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			getCanvas().requestFocus();
			if (e.getButton() == MouseEvent.BUTTON1 && !e.isControlDown()) {
				applyShadow();
			}
			updateShadow(toBoardCoordinates(e.getPoint()));
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			getCanvas().setShadow(null);
		}

	}

	public String toString() {
		return "Human";
	}

}
