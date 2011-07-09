package org.projectx.zookeeper.election;

import java.util.NavigableSet;

import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.ZNodeUtils;

/**
 * A {@link LeaderElectionStrategy} implementation which follows the strategy
 * outlined by the Zookeeper <a href=
 * "http://zookeeper.apache.org/doc/current/recipes.html#sc_leaderElection"
 * >recipes page</a> in order to prevent client "herding" when a node
 * disappears.
 * <p>
 * In order for a node to find its leader it needs to watch for the node with a
 * lower sequence number. If no such node exists then that node must become the
 * leader. Hence, in this strategy every node is looking only at its subsequent
 * potential leader instead of all the nodes looking at the one active leader
 * and going to election all at the same time when the leader goes away.
 * 
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class AntiHerdingElectionStrategy implements LeaderElectionStrategy {

  @Override
  public SequentialZNode findNextLeader(final NavigableSet<SequentialZNode> nodes, final SequentialZNode node) {
    return ZNodeUtils.findNextLeader(nodes, node);
  }

  @Override
  public boolean isLeader(final NavigableSet<SequentialZNode> nodes, final SequentialZNode node) {
    final Integer smallestZNode = nodes.first().getSequence();
    return node.getSequence().equals(smallestZNode);
  }

}
