package fr.eyzox.timeline;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomTimeline extends AbstractTimeline {

	private static final Random RDN = new Random();
	
	public RandomTimeline(Collection<? extends ITimelineElement> c) {
		super(c);
	}

	@Override
	public int getNextAvailableIndex(List<ITimelineElement> availables) {
		return RDN.nextInt(availables.size());
	}

}
