/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import martijn.quoridor.Core;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Position;
import martijn.quoridor.model.PutWall;
import martijn.quoridor.model.Wall;

/**
 * A JPanel that draws a Quoridor state and a shadow move.
 * 
 * @author Martijn van Steenbergen
 */
public class BoardCanvas extends JPanel implements BoardListener {

	private static final float WALL_THICKNESS = .1f;

	/** The board this canvas visualizes. */
	private Board board;

	/** The current shadow move. */
	private Move shadow;

	/** The transformation used to transform from pixel to board coordinates. */
	private AffineTransform transform;

	/** Creates a new QuoridorCanvas. */
	public BoardCanvas(Board board) {
		this.board = board;
		board.addBoardListener(this);
	}

	// Painting.

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Transform from pixel coordinates to board coordinates.
		transform = new AffineTransform();
		double scale = Math.min((double) getWidth() / board.getWidth(),
				(double) getHeight() / board.getHeight());
		scale *= .95;
		double boardWidth = board.getWidth() * scale;
		double boardHeight = board.getHeight() * scale;
		transform.translate((getWidth() - boardWidth) / 2,
				-(getHeight() - boardHeight) / 2);
		transform.translate(0, getHeight());
		transform.scale(1, -1);
		transform.scale(scale, scale);

		g2.transform(transform);
		paintState(g2, (float) (1 / scale));
	}

	/** Paints the current state. */
	private void paintState(Graphics2D g, float hairline) {
		g.setColor(Color.GRAY);

		// Draw cells.
		g.setStroke(new BasicStroke(hairline));
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				g.draw(new Rectangle2D.Double(x + WALL_THICKNESS / 2, y
						+ WALL_THICKNESS / 2, 1 - WALL_THICKNESS,
						1 - WALL_THICKNESS));
			}
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw walls.
		for (int x = 0; x < board.getWidth() - 1; x++) {
			for (int y = 0; y < board.getHeight() - 1; y++) {
				Wall wall = board.getWall(new Position(x, y));
				if (wall != null) {
					drawWall(g, hairline, wall, x, y, false);
				}
			}
		}

		// Draw players.
		for (Player p : board.getPlayers()) {
			drawPlayer(g, hairline, p, p.getPosition(), false);
		}

		// Draw shadow.
		if (isShadowLegal()) {
			if (shadow instanceof PutWall) {
				PutWall pw = (PutWall) shadow;
				drawWall(g, hairline, pw.getWall(), pw.getPosition().getX(), pw
						.getPosition().getY(), true);
			} else if (shadow instanceof Jump) {
				Jump j = (Jump) shadow;
				drawPlayer(g, hairline, board.getTurn(), j.getNewPosition(),
						true);
			}
		}
	}

	/** Draws a single player. */
	private void drawPlayer(Graphics2D g, float hairline, Player p,
			Position pos, boolean shadow) {

		// Remember the old transformation.
		AffineTransform at = g.getTransform();

		// The shape that represents the player.
		Shape s = new Ellipse2D.Double(-.5, -.5, 1, 1);

		// Transform so that the shape is drawn at the correct position.
		g.translate(pos.getX() + .5, pos.getY() + .5);
		g.scale(.75, .75);

		// Draw fill.
		Color c = p.getColor();
		if (shadow) {
			c = Core.transparent(c);
		}
		g.setColor(c);
		g.fill(s);

		// Draw outline.
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(hairline * 1.5f));
		g.draw(s);

		// Revert to old transformation.
		g.setTransform(at);
	}

	/** Draws a single wall. */
	private void drawWall(Graphics2D g, float hairline, Wall wall, int x,
			int y, boolean shadow) {
		g.setStroke(new BasicStroke(WALL_THICKNESS));
		g.setColor(shadow ? new Color(0x7f000000, true) : Color.BLACK);
		Shape line;
		switch (wall) {
		case HORIZONTAL:
			line = new Line2D.Double(x + WALL_THICKNESS, y + 1, x + 2
					- WALL_THICKNESS, y + 1);
			break;
		case VERTICAL:
			line = new Line2D.Double(x + 1, y + WALL_THICKNESS, x + 1, y + 2
					- WALL_THICKNESS);
			break;
		default:
			throw new InternalError();
		}
		g.draw(line);
	}

	/**
	 * Returns the transformation that was used at the last paint operation to
	 * transform from board coordinates to pixel coordinates. Returns null if
	 * there has never been a paint operation performed yet.
	 */
	public AffineTransform getPaintTransformation() {
		return transform;
	}

	// Getters and setters.

	/** Returns the board this canvas visualizes. */
	public Board getBoard() {
		return board;
	}

	/** Returns the current shadow move. */
	public Move getShadow() {
		return shadow;
	}

	/** Sets the current shadow move. */
	public void setShadow(Move shadow) {
		this.shadow = shadow;
		repaint();
	}

	/** Returns whether the shadow is non-null and legal. */
	public boolean isShadowLegal() {
		return shadow != null && shadow.isLegal(getBoard());
	}

	public void applyShadow() {
		board.move(shadow);
		setShadow(null);
	}

	public void moveExecuted(Move move) {
		setShadow(null);
		repaint();
	}

	public void movesUndone(Move[] moves) {
		setShadow(null);
		repaint();
	}

	public void newGame() {
		setShadow(null);
		repaint();
	}

}
