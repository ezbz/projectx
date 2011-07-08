package org.projectx.zookeeper;

public interface ZooKeeperOperations extends ZooKeeperPersistentOperations, ZooKeeperEphemeralOperations,
    ZooKeeperEphemeralSequentialOperations {

  /**
   * Check if a node exists within ZooKeeper
   * 
   * @param path
   *          the path to the requested node
   * @return true if the node exits
   */
  boolean nodeExists(String path);

  /**
   * Register a listener for data/state changes within ZooKeeper
   * 
   * @param path
   *          the path to listen for
   * @param listener
   *          the listener implementation
   */
  void registerListener(String path, LeaderElectionNodeListener listener);

  /**
   * Delete a node with a given path
   * 
   * @param path
   *          Zookeeper path to delete
   * @return true if the node was deleted
   */
  boolean delete(String path);

  /**
   * Delete a node and all it's children
   * 
   * @param path
   *          Zookeeper path to delete
   * @return true if the node was deleted
   */
  boolean deleteRecursive(String path);
}
