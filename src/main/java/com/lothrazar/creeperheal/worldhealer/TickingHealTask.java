package com.lothrazar.creeperheal.worldhealer;

import com.lothrazar.creeperheal.data.TickingLinkedList;
import com.lothrazar.creeperheal.data.BlockStatePosWrapper;
import com.lothrazar.creeperheal.data.TickContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class TickingHealTask extends TickingLinkedList<Collection<BlockStatePosWrapper>> {

  @Override
  protected Collection<BlockStatePosWrapper> merge(Collection<BlockStatePosWrapper> o1, Collection<BlockStatePosWrapper> o2) {
    o2.addAll(o1);
    return o2;
  }

  protected LinkedList<TickContainer<Collection<BlockStatePosWrapper>>> getLinkedList() {
    return list;
  }

  public void add(int tick, BlockStatePosWrapper data) {
    Collection<BlockStatePosWrapper> c = new ArrayList<BlockStatePosWrapper>(1);
    c.add(data);
    add(tick, c);
  }
}
