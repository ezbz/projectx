package org.projectx.zookeeper.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingLeaderElectionTarget implements LeaderElectionTarget {

  private static final Logger logger = LoggerFactory.getLogger(LoggingLeaderElectionTarget.class);

  @Override
  public void start() {
    logger.info("Starting...");

  }

  @Override
  public void stop() {
    logger.info("Stop...");
  }

  @Override
  public boolean isRunning() {
    return true;
  }

}
