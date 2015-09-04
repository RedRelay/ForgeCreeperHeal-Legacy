package fr.eyzox.sdd.graph;

import java.util.List;

public interface INode extends Iterable<INode>{
	public boolean hasNext();
	public List<INode> getNextNodes();
	public boolean add(INode node);
	public INode remove(int i);
	public boolean remove(Object o);
	public INode get(int i);
}
