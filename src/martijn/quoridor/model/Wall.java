/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor.model;

/**
 * @author Martijn van Steenbergen
 */
public enum Wall {

	HORIZONTAL, VERTICAL;

	public Wall flip() {
		return values()[(ordinal() + 1) % 2];
	}

}
