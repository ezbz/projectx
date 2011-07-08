package org.projectx.zookeeper.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.projectx.zookeeper.SequentialZNode;

public class LoggingLeaderElectionListener implements LeaderElectionListener {
  private static final Logger log = LoggerFactory.getLogger(LoggingLeaderElectionListener.class);

  @Override
  public void handleNewLeader(final SequentialZNode leader) {
    log.info("New leader elected: {}", leader);

  }

}
