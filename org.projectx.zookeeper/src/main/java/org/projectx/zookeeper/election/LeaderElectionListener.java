package org.projectx.zookeeper.election;

import org.projectx.zookeeper.SequentialZNode;

/**
 * A listener interface for election of a new leader
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface LeaderElectionListener {
  /**
   * Notify the election of a new leader
   * 
   * @param leader
   */
  void handleNewLeader(SequentialZNode leader);
}
