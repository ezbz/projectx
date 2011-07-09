package org.projectx.zookeeper.election.monitor;

import org.joda.time.DateTime;

/**
 * Manage and query the leader elector
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface LeaderElectorMonitorMBean {
  /**
   * Get the path of the current node
   * 
   * @return a string representing the path and sequence of the current node
   */
  String getNode();

  /**
   * Get the path of the leader node (if the is the same value of
   * {@link #getNode()} then this is the current leader)
   * 
   * @return a string representing the path and sequence of the leader node
   */
  String getLeader();

  /**
   * Get the connection state to the Zookeeper server
   * 
   * @return true if currently connected to Zookeeper
   */
  boolean isConnected();

  /**
   * Is this node the current leader (this means that the values of
   * {@link #getNode()} and {@link #getLeader()} should be the same
   * 
   * @return true if this is the current leader
   */
  boolean isCurrentLeader();

  /**
   * Perform a new leader election, <b>DO NOT</b> activate this method unless
   * you know what you're doing
   */
  void electNewLeader();

  /**
   * Get the {@link DateTime} last execution of leader election
   * 
   * @return the date of last leader election
   */

  DateTime getLastElectionTime();
}
