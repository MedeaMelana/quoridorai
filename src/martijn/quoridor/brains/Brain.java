/*
 * Created on Aug 8, 2006 
 */
package martijn.quoridor.brains;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;

/**
 * A brain determines the best move in a given board.
 * 
 * @author Martijn van Steenbergen
 */
public abstract class Brain {

	private String name;

	private String description;

	private String author;

	private String version;

	/** Creates a new Brain with the implementing class' simple name as name. */
	public Brain() {
		this(null);
	}

	/** Creates a new Brain with the specified name. */
	public Brain(String name) {
		if (name == null) {
			name = getClass().getSimpleName();
		}
		name = name.trim();
		assertIsOneLine(name);
		if (name.equals("")) {
			throw new IllegalArgumentException("Name is empty.");
		}
		this.name = name;
	}

	/** Returns this brain's name. */
	public String getName() {
		return name;
	}

	/** Returns the brain's description. */
	public String getDescription() {
		return description;
	}

	/** Sets the brain's description. */
	protected void setDescription(String description) {
		this.description = description;
	}

	/** Returns the brain's author. */
	public String getAuthor() {
		return author;
	}

	/** Sets the brain's author. */
	protected void setAuthor(String author) {
		assertIsOneLine(author);
		this.author = author;
	}

	/** Returns the brain's version. */
	public String getVersion() {
		return version;
	}

	/** Sets the brain's version. */
	protected void setVersion(String version) {
		assertIsOneLine(version);
		this.version = version;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if the string consists of more than one line.
	 */
	private void assertIsOneLine(String s) {
		if (s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0) {
			throw new IllegalArgumentException("String must be only one line.");
		}
	}

	/**
	 * Returns the move this brain thinks is best in the specified game
	 * situation. The implementor is allowed to manipulate the board.
	 * {@code getMove} should always return a non-null, legal move.
	 * <p>
	 * The brain should occasionally check {@link Thread#interrupted()} to see
	 * if it was cancelled. If {@code interrupted()} returns {@code true}, the
	 * brain should throw an {@link InterruptedException} as soon as possible.
	 */
	public abstract Move getMove(Board board) throws InterruptedException;

}
