package org.projectx.zookeeper.election;

import org.springframework.util.Assert;

import org.projectx.zookeeper.SequentialZNode;

/**
 * Class for maintaining the state of election for a single node.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ElectionState {
  /**
   * The current node
   */
  private final SequentialZNode node;
  /**
   * The current node's leader
   */
  private final SequentialZNode leader;

  private ElectionState(final SequentialZNode node, final SequentialZNode leader) {
    Assert.notNull(node, "node cannot be null");
    this.node = node;
    this.leader = leader;
  }

  public static ElectionState valueOf(final SequentialZNode node, final SequentialZNode leader) {
    return new ElectionState(node, leader);
  }

  public ElectionState valueOf(final SequentialZNode leader) {
    return new ElectionState(getNode(), leader);
  }

  public SequentialZNode getNode() {
    return node;
  }

  public SequentialZNode getLeader() {
    return leader;
  }
}
