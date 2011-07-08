package org.projectx.zookeeper;

/**
 * A data-access object for working with ZooKeeper nodes. ZooKeeper nodes are
 * structured in a hierarchical manner as described in the <a href=
 * "http://zookeeper.apache.org/doc/r3.3.3/zookeeperOver.html#sc_dataModelNameSpace"
 * >ZooKeeper manual</a>.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface ZooKeeperEphemeralOperations {
  /**
   * Create an ephemeral node
   * 
   * @param path
   *          the root of the ephemeral node collection
   * @return the {@link ZNode} representing the created node
   */
  ZNode createEphemeral(String path);

  /**
   * Create an ephemeral node with data
   * 
   * @param path
   *          the path of the ephemeral node to create
   * @param data
   *          the data to set on the node
   * @return the {@link ZNode} representing the created node
   */
  ZNode createEphemeral(String path, Object data);
}
