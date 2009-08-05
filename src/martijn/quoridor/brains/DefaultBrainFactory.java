/*
 * Created on Aug 17, 2006 
 */
package martijn.quoridor.brains;

import java.util.List;

/**
 * The DefaultBrainFactory provides the default brains used in the Quoridor
 * application.
 * 
 * @author Martijn van Steenbergen
 */
public class DefaultBrainFactory implements BrainFactory {

	public void addBrains(List<Brain> brains) {
		brains.add(new DumbBrain());
		brains.add(createSmartBrain(2));
		brains.add(createSmartBrain(3));
		brains.add(createSmartBrain(4));
	}

	private Brain createSmartBrain(int i) {
		NegamaxBrain b = new SmartBrain(i);
		b.setDeterministic(false);
		return b;
	}

}
