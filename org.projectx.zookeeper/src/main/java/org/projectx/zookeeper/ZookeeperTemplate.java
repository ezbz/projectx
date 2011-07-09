package org.projectx.zookeeper;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * An implementation of the {@link ZookeeperEphemeralSequentialOperations} interface
 * based on {@link ZkClient} instance.
 * <p>
 * <b>Note</b>: this implementation implicitly waits for nodes to be visible
 * within Zookeeper before returning. The timeout for waiting on nodes can be
 * configured globally via {@link #setNodeCreationTimeoutSeconds(int)}. If the
 * timeout is exceeded and the node is still not visible an exception is thrown.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ZookeeperTemplate implements ZookeeperOperations {
  private final ZkClient zkClient;
  private int nodeCreationTimeoutSeconds = 5;
  private final ObjectFormatter formatter = new EncodedStringObjectFormatter();

  public ZookeeperTemplate(final ZkClient zkClient) {
    Assert.notNull(zkClient, "zkClient cannot be null");
    this.zkClient = zkClient;
  }

  public void setNodeCreationTimeoutSeconds(final int nodeCreationTimeoutSeconds) {
    this.nodeCreationTimeoutSeconds = nodeCreationTimeoutSeconds;
  }

  @Override
  public NavigableSet<SequentialZNode> findChildren(final String path) {
    final List<String> children = zkClient.getChildren(path);
    final NavigableSet<SequentialZNode> ZNodes = createNodes(children, path);
    return ZNodes;
  }

  @Override
  public boolean nodeExists(final String path) {
    return zkClient.exists(path);
  }

  private NavigableSet<SequentialZNode> createNodes(final List<String> children, final String path) {
    final NavigableSet<SequentialZNode> ZNodes = new TreeSet<SequentialZNode>();
    for (final String child : children) {
      final String fullPath = path + ZookeeperConstants.PATH_SEPARATOR + child;
      ZNodes.add(createNode(path, fullPath, zkClient.readData(fullPath)));
    }
    return ZNodes;
  }

  private SequentialZNode createNode(final String path, final String child, final Object data) {
    return new EphemeralZNodeImpl(path, data, EphemeralNodeNameUtils.parseSequenceFromName(child));
  }

  @Override
  public SequentialZNode createEphemeralSequential(final String path, final Object data) {
    final String ephermalPath = path + ZookeeperConstants.PATH_SEPARATOR
        + ZookeeperConstants.EPHERMAL_PREFIX;
    final String nodeName = zkClient.createEphemeralSequential(ephermalPath, formatData(data));
    final boolean created = zkClient.waitUntilExists(nodeName, TimeUnit.SECONDS,
        nodeCreationTimeoutSeconds);
    if (!created) {
      throw new ZkNoNodeException("Ephemeral node not created after " + nodeCreationTimeoutSeconds
          + " seconds, cannot proceed");
    }
    return createNode(path, nodeName, data);
  }

  @Override
  public SequentialZNode createEphemeralSequential(final String path) {
    return createEphemeralSequential(path, null);
  }

  @Override
  public ZNode createPersistent(final String path) {
    final String[] parts = StringUtils.split(path, ZookeeperConstants.PATH_SEPARATOR);
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < parts.length; i++) {
      sb.append(ZookeeperConstants.PATH_SEPARATOR).append(parts[i]);
      final String pathPart = sb.toString();
      if (!zkClient.exists(pathPart)) {
        zkClient.createPersistent(pathPart);
        final boolean created = zkClient.waitUntilExists(pathPart, TimeUnit.SECONDS,
            nodeCreationTimeoutSeconds);
        if (!created) {
          throw new ZkNoNodeException("Persistent root node not created after "
              + nodeCreationTimeoutSeconds + " seconds, cannot proceed");
        }
      }
    }
    return new ZNodeImpl(path);
  }

  @Override
  public ZNode createEphemeral(final String path) {
    zkClient.createEphemeral(path);
    return new ZNodeImpl(path);
  }

  @Override
  public ZNode createEphemeral(final String path, final Object data) {
    zkClient.createEphemeral(path, formatData(data));
    return new ZNodeImpl(path, data);
  }

  @Override
  public void registerListener(final String path, final LeaderElectionNodeListener listener) {
    zkClient.subscribeDataChanges(path, listener);
    zkClient.subscribeStateChanges(listener);
  }

  private Object formatData(final Object data) {
    return formatter.format(data);
  }

  @Override
  public boolean delete(final String path) {
    return zkClient.delete(path);
  }

  @Override
  public boolean deleteRecursive(final String path) {
    return zkClient.deleteRecursive(path);
  }
}