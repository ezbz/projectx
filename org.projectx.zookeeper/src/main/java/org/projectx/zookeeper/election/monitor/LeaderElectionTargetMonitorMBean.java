package org.projectx.zookeeper.election.monitor;

public interface LeaderElectionTargetMonitorMBean {
  /**
   * Start the target manually, <b>DO NOT</b> activate this method unless you
   * know what you're doing
   */
  void start();

  /**
   * Stop the target manually, <b>DO NOT</b> activate this method unless you
   * know what you're doing
   */
  void stop();

  /**
   * Check if the target of execution is running or not
   * 
   * @return true if the target is running
   */
  boolean isRunning();
}
