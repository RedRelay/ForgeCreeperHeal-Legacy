package fr.eyzox.forgecreeperheal.healgraph;

import fr.eyzox.sdd.graph.INode;
import fr.eyzox.sdd.graph.Node;


public class BlockDataNode extends Node {
	
	@Override
	public boolean add(INode n) {
		if(n instanceof BlockData) {
			return this.add((BlockData)n);
		}
		return false;
	}
	
	public boolean add(BlockData n) {
		return super.add(n);
	}

	@Override
	public BlockData get(int index) {
		return (BlockData) super.get(index);
	}

	@Override
	public BlockData remove(int index) {
		return (BlockData) super.remove(index);
	}
}
