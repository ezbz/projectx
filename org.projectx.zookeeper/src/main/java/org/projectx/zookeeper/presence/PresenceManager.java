package org.projectx.zookeeper.presence;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.projectx.zookeeper.ZNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class PresenceManager implements IZkStateListener, InitializingBean {
  private static final Logger log = LoggerFactory.getLogger(PresenceManager.class);
  private final PresenceNodeFactory presenceNodeFactory;
  private ZNode presenceNode;

  private boolean disabled = false;

  public PresenceManager(final PresenceNodeFactory presenceNodeFactory) {
    Assert.notNull(presenceNodeFactory, "presenceNodeFactory cannot be null");
    this.presenceNodeFactory = presenceNodeFactory;
  }

  public void setDisabled(final boolean disabled) {
    this.disabled = disabled;
  }

  @Override
  public void handleStateChanged(final KeeperState state) throws Exception {

  }

  @Override
  public void handleNewSession() throws Exception {
    initialize();
  }

  private void initialize() {
    if (disabled) {
      return;
    }

    try {
      this.presenceNode = presenceNodeFactory.createPresenceNode();
    } catch (final ZkNodeExistsException e) {
      log.warn("No need to create presence node after zookeeper session expiration, node exists");
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    handleNewSession();
  }

  public ZNode getPresenceNode() {
    return presenceNode;
  }

}
