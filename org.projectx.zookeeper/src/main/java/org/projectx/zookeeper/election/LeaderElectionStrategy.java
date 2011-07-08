package org.projectx.zookeeper.election;

import java.util.NavigableSet;

import org.projectx.zookeeper.SequentialZNode;

/**
 * An interface representing a strategy for electing a new leader
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface LeaderElectionStrategy {

  public SequentialZNode findNextLeader(NavigableSet<SequentialZNode> nodes, SequentialZNode node);

  public boolean isLeader(NavigableSet<SequentialZNode> nodes, SequentialZNode node);
}
