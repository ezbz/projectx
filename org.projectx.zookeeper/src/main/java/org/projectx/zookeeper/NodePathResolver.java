package org.projectx.zookeeper;

/**
 * An interface for resolution of Zookeeper node paths. Allows for customization
 * of how (or where) entities are represented in Zookeeper.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface NodePathResolver {

  /**
   * Resolve the path for entity creation in Zookeeper
   * 
   * @return a path used for the election process.
   */
  String resolve();
}
