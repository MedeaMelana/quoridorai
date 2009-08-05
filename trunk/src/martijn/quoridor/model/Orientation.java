/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor.model;

/**
 * @author Martijn van Steenbergen
 */
public enum Orientation {

	NORTH, EAST, SOUTH, WEST;

	public Orientation opposite() {
		return values()[(ordinal() + 2) % 4];
	}

}
