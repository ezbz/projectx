package org.projectx.zookeeper.election.monitor;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.projectx.zookeeper.election.LeaderElector;

public class LeaderElectorMonitor implements LeaderElectorMonitorMBean {
  private static final Logger log = LoggerFactory.getLogger(LeaderElectorMonitor.class);
  private final LeaderElector elector;

  public LeaderElectorMonitor(final LeaderElector elector) {
    Assert.notNull(elector, "elector cannot be null");
    this.elector = elector;
  }

  @Override
  public boolean isConnected() {
    return elector.isConnected();
  }

  @Override
  public String getNode() {
    return elector.getNode().getFullPath();
  }

  @Override
  public String getLeader() {
    return elector.getLeader().getFullPath();
  }

  @Override
  public boolean isCurrentLeader() {
    return elector.isLeader();
  }

  @Override
  public void electNewLeader() {
    log.warn("electNewLeader was triggered by MBean");
    elector.electNewLeader();
  }

  @Override
  public DateTime getLastElectionTime() {
    return elector.getLastElectionTime();
  }
}
