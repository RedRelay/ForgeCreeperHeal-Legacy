package fr.eyzox.forgecreeperheal.healgraph;

import java.util.Collection;
import java.util.Random;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;
import fr.eyzox.sdd.graph.INode;

public class HealGraph extends BlockDataGraph {
	private Random rdn = new Random();
	private int tickLeftBeforeNextHeal;

	public HealGraph(Collection<BlockData> nodes) {
		super(nodes);
		this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal() + ForgeCreeperHeal.getConfig().getRandomTickVar();
	}
	
	private BlockData advance(int i) {
		BlockData n = (BlockData) getNextNodes().remove(i);
		if(n.hasNext()) {
			for(INode newNode : n.getNextNodes()) {
				this.add((BlockData)newNode);
			}
		}
		return n;
	}
	
	public void onTick(WorldHealer worldHealer) {
		tickLeftBeforeNextHeal--;
		if(tickLeftBeforeNextHeal < 0) {
			worldHealer.heal(this.advance(rdn.nextInt(getNextNodes().size())));
			this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getRandomTickVar();
		}
	}

}
