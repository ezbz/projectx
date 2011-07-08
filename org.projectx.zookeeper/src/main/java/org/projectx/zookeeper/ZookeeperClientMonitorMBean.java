package org.projectx.zookeeper;

public interface ZookeeperClientMonitorMBean {

  long getDisconnectedCount();

  long getSyncConnectedCount();

  long getAuthFailedCount();

  long getExpiredCount();

}