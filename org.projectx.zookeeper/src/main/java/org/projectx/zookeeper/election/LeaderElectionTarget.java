package org.projectx.zookeeper.election;

/**
 * Interface for classes that wish to be managed by a ZooKeeper based leader
 * election scheme (e.g., in a leader election scenario ZooKeeper will use this
 * interface to indicate to the target weather it is the leader or not).
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface LeaderElectionTarget {
  /**
   * Called when this entity has been elected the leader in an election process.
   */
  void start();

  /**
   * Called when this entity has been de-elected due to inability to determine
   * the current leader state
   */
  void stop();

  /**
   * True if the target is currently running
   * 
   * @return
   */
  boolean isRunning();
}
