package fr.eyzox.timeline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WaitingElementContainer implements IDependencyChecker<Object>{

	/**
	 * Map key is the dependency for a {@link Set} of {@link ITimelineElement}
	 */
	private final Map<ITimelineElement, Set<ITimelineElement>> waitingMap = new HashMap<ITimelineElement, Set<ITimelineElement>>();

	/**
	 * Map key is a ITimelineElement which is contained into each {@link Set} from the linked {@link List} value
	 */
	private final Map<ITimelineElement, List<Set<ITimelineElement>>> refMap = new HashMap<ITimelineElement, List<Set<ITimelineElement>>>();

	/**Puts a new {@link ITimelineElement} with its dependency
	 * @param element
	 * @param dependency
	 */
	public void put(ITimelineElement element, ITimelineElement dependency) {

		final Set<ITimelineElement> waitingSet = getWaitingSet(dependency);

		if(waitingSet.add(element)) {
			final List<Set<ITimelineElement>> referencedWaitingSets = getReferencedWaitingSets(element);
			referencedWaitingSets.add(waitingSet);
		}

	}

	private Set<ITimelineElement> getWaitingSet(ITimelineElement dependency) {
		Set<ITimelineElement> set = waitingMap.get(dependency);
		if(set == null) {
			set = new HashSet<ITimelineElement>();
			waitingMap.put(dependency, set);
		}
		return set;
	}

	private List<Set<ITimelineElement>> getReferencedWaitingSets(ITimelineElement element) {
		List<Set<ITimelineElement>> waitingSets = refMap.get(element);
		if(waitingSets == null) {
			waitingSets = new LinkedList<Set<ITimelineElement>>();
			refMap.put(element, waitingSets);
		}
		return waitingSets;
	}

	/**
	 * Returns the {@link Set} of {@link ITimelineElement} which were waiting for this dependency
	 * @param dependency
	 * @return
	 */
	public Set<ITimelineElement> setDependencyAvailable(ITimelineElement dependency) {
		final Set<ITimelineElement> set = waitingMap.remove(dependency);

		if(set != null) {
			for(ITimelineElement element : set) {
				List<Set<ITimelineElement>> waitingSets = refMap.remove(element);
				for(Set<ITimelineElement> waitingSet : waitingSets) {
					if(waitingSet != set) {
						waitingSet.remove(element);
						if(waitingSet.isEmpty()) {
							waitingMap.values().remove(waitingSet);
						}
					}
				}
			}
		}

		return set;
	}

	@Override
	public boolean isStillRequired(Object key) {
		return refMap.containsKey(key);
	}

	@Override
	public String toString() {
		return "[\n\tWAITINGS:"+waitingMap+";\n\tREF:"+refMap+";\n]";
	}

}
