package org.projectx.zookeeper.presence;

import org.projectx.net.InetUtils;
import org.projectx.zookeeper.ZNode;
import org.projectx.zookeeper.ZookeeperConstants;
import org.projectx.zookeeper.ZookeeperOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class PresenceNodeFactory {
  private static final Logger log = LoggerFactory.getLogger(PresenceNodeFactory.class);
  private final ZookeeperOperations zkDao;
  private final String presencePath;
  private final String entityName;
  private final MetaDataProvider metaDataProvider;

  public PresenceNodeFactory(final ZookeeperOperations zkDao, final String presencePath,
      final String entityName, final MetaDataProvider metaDataProvider) {
    Assert.notNull(zkDao, "zkDao cannot be null");
    Assert.hasText(presencePath, "presencePathRoot cannot be empty");
    Assert.hasText(entityName, "entityName cannot be empty");
    Assert.notNull(metaDataProvider, "metaDataProvider cannot be null");
    this.zkDao = zkDao;
    this.presencePath = presencePath;
    this.entityName = entityName;
    this.metaDataProvider = metaDataProvider;
  }

  public ZNode createPresenceNode() {

    final String servicePath = presencePath + ZookeeperConstants.PATH_SEPARATOR + entityName
        + ZookeeperConstants.PATH_SEPARATOR + "presence";
    if (!zkDao.nodeExists(servicePath)) {
      zkDao.createPersistent(servicePath);
    } else {
      log.info("Persistent node for service path {} already exists, skipping creation.",
          presencePath);
    }

    final String presenceNodePath = servicePath + "/" + InetUtils.getLocalHostname();
    return zkDao.createEphemeral(presenceNodePath, metaDataProvider.getMetaData());
  }

}
