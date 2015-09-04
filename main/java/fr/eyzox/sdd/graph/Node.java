package fr.eyzox.sdd.graph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Node implements INode{
	private List<INode> nextNodes;
	
	@Override
	public boolean hasNext() {
		return !(nextNodes == null || nextNodes.isEmpty());
	}

	@Override
	public List<INode> getNextNodes() {
		return nextNodes;
	}

	public boolean add(INode e) {
		if(nextNodes == null) nextNodes = new ArrayList<INode>();
		return nextNodes.add(e);
	}

	public INode remove(int index) {
		return nextNodes.remove(index);
	}

	public boolean remove(Object o) {
		return nextNodes.remove(o);
	}

	@Override
	public INode get(int i) {
		return nextNodes.get(i);
	}

	@Override
	public Iterator<INode> iterator() {
		return new NodeIterator();
	}

	protected class NodeIterator implements Iterator<INode> {

		private Deque<Iterator<INode>> stack = new LinkedList<Iterator<INode>>();
		private Iterator<INode> cursor;
		
		public NodeIterator() {
			if(Node.this.hasNext()) {
				cursor = Node.this.nextNodes.iterator();
			}
		}
		
		@Override
		public boolean hasNext() {
			return cursor != null && cursor.hasNext();
		}

		@Override
		public INode next() {
			INode res = cursor.next();
			if(res.hasNext()) {
				if(cursor.hasNext()) {
					stack.push(cursor);
				}
				cursor = res.getNextNodes().iterator();
			}else if(!cursor.hasNext() && !stack.isEmpty()) {
				cursor = stack.poll();
			}
			
			return res;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
