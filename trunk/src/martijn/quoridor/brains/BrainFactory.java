/*
 * Created on Aug 17, 2006 
 */
package martijn.quoridor.brains;

import java.util.List;

/**
 * A {@code BrainFactory} creates the brains used in the Quoridor application.
 * 
 * @author Martijn van Steenbergen
 */
public interface BrainFactory {

	/**
	 * Creates and appends this factory's brains to the list.
	 * 
	 * @param brains
	 *            the list to append the created brains to.
	 */
	public void addBrains(List<Brain> brains);

}
