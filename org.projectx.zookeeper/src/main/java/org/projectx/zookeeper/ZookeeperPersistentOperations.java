package org.projectx.zookeeper;

/**
 * A data-access object for working with Zookeeper nodes. Zookeeper nodes are
 * structured in a hierarchical manner as described in the <a href=
 * "http://zookeeper.apache.org/doc/r3.3.3/zookeeperOver.html#sc_dataModelNameSpace"
 * >Zookeeper manual</a>.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface ZookeeperPersistentOperations {

  /**
   * Create a persistent path recursively within Zookeeper
   * 
   * @param path
   *          an absolute path (e.g., passing /domain/election will first create
   *          /doamin then create /domain/election).
   */
  ZNode createPersistent(String path);

}
