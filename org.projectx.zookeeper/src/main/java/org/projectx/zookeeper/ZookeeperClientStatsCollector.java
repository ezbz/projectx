package org.projectx.zookeeper;

import java.util.concurrent.atomic.AtomicLong;

import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClientStatsCollector implements IZkStateListener {
  private static final Logger log = LoggerFactory.getLogger(ZookeeperClientStatsCollector.class);

  private final AtomicLong disconnectedCount = new AtomicLong();
  private final AtomicLong syncConnectedCount = new AtomicLong();
  private final AtomicLong authFailedCount = new AtomicLong();
  private final AtomicLong expiredCount = new AtomicLong();

  @Override
  public void handleStateChanged(final KeeperState state) throws Exception {
    switch (state) {
    case Disconnected:
      disconnectedCount.incrementAndGet();
      break;
    case SyncConnected:
      syncConnectedCount.incrementAndGet();
      break;
    case Expired:
      expiredCount.incrementAndGet();
      break;
    case AuthFailed:
      authFailedCount.incrementAndGet();
      break;
    default:
      log.warn("Unrecognized keeper state: {}", state);
      break;
    }
  }

  /**
   * do not handle this explicity will be handled via
   * {@link #handleStateChanged(KeeperState)} {@link KeeperState#Expired}
   */
  @Override
  public void handleNewSession() throws Exception {
  }

  public long getDisconnectedCount() {
    return disconnectedCount.get();
  }

  public long getSyncConnectedCount() {
    return syncConnectedCount.get();
  }

  public long getAuthFailedCount() {
    return authFailedCount.get();
  }

  public long getExpiredCount() {
    return expiredCount.get();
  }
}
