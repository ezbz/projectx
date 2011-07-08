package org.projectx.zookeeper.election;

import java.util.NavigableSet;

import org.projectx.net.InetUtils;
import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.ZNodeUtils;
import org.projectx.zookeeper.ZooKeeperConstants;
import org.projectx.zookeeper.ZooKeeperOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ElectionStateFactory {
  private static final Logger log = LoggerFactory.getLogger(ElectionStateFactory.class);

  private final ZooKeeperOperations zkDao;
  private final String electionPath;

  private final String entityName;

  public ElectionStateFactory(final ZooKeeperOperations zooKeeperDao, final String electionPath,
      final String entityName) {
    Assert.notNull(zooKeeperDao, "zooKeeperDao cannot be null");
    Assert.hasText(electionPath, "nodeRoot cannot be empty");
    Assert.hasText(entityName, "entityName cannot be null");
    this.zkDao = zooKeeperDao;
    this.electionPath = electionPath;
    this.entityName = entityName;
  }

  public ElectionState create() {
    final String servicePath = constructServicePath();
    if (!zkDao.nodeExists(servicePath)) {
      log.info("Persistent root for leader election is missing, creating path: {}", servicePath);
      zkDao.createPersistent(servicePath);
    }

    final String address = InetUtils.getLocalHostname();
    final SequentialZNode node = zkDao.createEphemeralSequential(servicePath, address);
    log.info("Created ZooKeeper node {}", node);
    final NavigableSet<SequentialZNode> children = zkDao.findChildren(servicePath);
    final SequentialZNode leader = ZNodeUtils.findNextLeader(children, node);
    return ElectionState.valueOf(node, leader);

  }

  private String constructServicePath() {
    return new StringBuilder(electionPath).append(ZooKeeperConstants.PATH_SEPARATOR)
                                          .append(entityName)
                                          .append(ZooKeeperConstants.PATH_SEPARATOR)
                                          .append("election").toString();
  }
}
