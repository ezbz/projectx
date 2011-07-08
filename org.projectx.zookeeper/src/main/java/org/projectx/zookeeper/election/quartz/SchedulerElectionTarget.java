package org.projectx.zookeeper.election.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.projectx.zookeeper.election.LeaderElectionTarget;

/**
 * An implementation of the {@link LeaderElectionTarget} for implementing leader
 * elections with services relying on Quartz {@link Scheduler} for their ongoing
 * execution.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class SchedulerElectionTarget implements LeaderElectionTarget {
  private static final Logger log = LoggerFactory.getLogger(SchedulerElectionTarget.class);
  private final Scheduler scheduler;
  private final boolean disabled;

  public SchedulerElectionTarget(final Scheduler scheduler, final boolean disabled) {
    Assert.notNull(scheduler, "scheduler cannot be null");
    this.scheduler = scheduler;
    this.disabled = disabled;
  }

  @Override
  public void start() {
    if (!disabled) {
      try {
        log.info("Start was invoked, starting quartz scheduler");
        scheduler.start();
      } catch (final SchedulerException e) {
        log.error("SchedulerException while calling start", e);
      }
    }
  }

  @Override
  public void stop() {
    if (!disabled) {
      try {
        log.info("Stop was invoked, putting quartz scheduler in standby");
        scheduler.standby();
      } catch (final SchedulerException e) {
        log.error("SchedulerException while calling standby", e);
      }
    }
  }

  @Override
  public boolean isRunning() {
    try {
      return !disabled && !scheduler.isInStandbyMode();
    } catch (final SchedulerException e) {
      throw new IllegalStateException("SchedulerException while calling isInStandbyMode", e);
    }
  }

}
