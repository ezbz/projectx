package org.projectx.zookeeper;

import org.springframework.util.Assert;

public class ZookeeperClientMonitor implements ZookeeperClientMonitorMBean {

  private final ZooKeeperClientStatsCollector statsCollector;

  public ZookeeperClientMonitor(final ZooKeeperClientStatsCollector statsCollector) {
    Assert.notNull(statsCollector, "statsCollector cannot be null");
    this.statsCollector = statsCollector;
  }

  @Override
  public long getDisconnectedCount() {
    return statsCollector.getDisconnectedCount();
  }

  @Override
  public long getSyncConnectedCount() {
    return statsCollector.getSyncConnectedCount();
  }

  @Override
  public long getAuthFailedCount() {
    return statsCollector.getAuthFailedCount();
  }

  @Override
  public long getExpiredCount() {
    return statsCollector.getExpiredCount();
  }

}
