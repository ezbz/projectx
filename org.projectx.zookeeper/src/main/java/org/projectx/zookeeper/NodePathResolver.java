package org.projectx.zookeeper;

/**
 * An interface for resolution of ZooKeeper node paths. Allows for customization
 * of how (or where) entities are represented in ZooKeeper.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface NodePathResolver {

  /**
   * Resolve the path for entity creation in ZooKeeper
   * 
   * @return a path used for the election process.
   */
  String resolve();
}
