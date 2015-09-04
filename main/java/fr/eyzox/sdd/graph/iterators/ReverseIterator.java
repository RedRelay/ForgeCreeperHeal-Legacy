package fr.eyzox.sdd.graph.iterators;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import fr.eyzox.sdd.graph.INode;

public class ReverseIterator implements Iterator<INode> {

	private Deque<Iterator<INode>> itStack;
	private Deque<INode> nodeStack = new LinkedList<INode>();
	
	public ReverseIterator(INode node) {
		if(node.hasNext()) {
			itStack = new LinkedList<Iterator<INode>>();
			itStack.push(node.getNextNodes().iterator());
		}
		nodeStack.add(node);
	}

	@Override
	public boolean hasNext() {
		return !nodeStack.isEmpty();
	}

	@Override
	public INode next() {
		if(itStack == null || itStack.isEmpty()) return nodeStack.poll();
		
		while(itStack.peek().hasNext()) {
			nodeStack.push(itStack.peek().next());
			if(nodeStack.peek().hasNext()) {
				itStack.push(nodeStack.peek().getNextNodes().iterator());
			}else {
				break;
			}
		}
		
		if(!itStack.peek().hasNext()) {
			itStack.poll();
		}
		
		return nodeStack.poll();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

}
