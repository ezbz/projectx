package org.projectx.zookeeper.election.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.projectx.zookeeper.election.LeaderElectionTarget;

public class LeaderElectionTargetMonitor implements LeaderElectionTargetMonitorMBean {
  private static final Logger log = LoggerFactory.getLogger(LeaderElectionTargetMonitor.class);
  private final LeaderElectionTarget target;

  public LeaderElectionTargetMonitor(final LeaderElectionTarget target) {
    Assert.notNull(target, "target cannot be null");
    this.target = target;
  }

  @Override
  public void start() {
    log.warn("Start was triggered by MBean");
    target.start();
  }

  @Override
  public void stop() {
    log.warn("Stop was triggered by MBean");
    target.stop();
  }

  @Override
  public boolean isRunning() {
    return target.isRunning();
  }
}
