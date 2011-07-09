package org.projectx.zookeeper;

import java.util.NavigableSet;

/**
 * A data-access object for working with Zookeeper nodes. Zookeeper nodes are
 * structured in a hierarchical manner as described in the <a href=
 * "http://zookeeper.apache.org/doc/r3.3.3/zookeeperOver.html#sc_dataModelNameSpace"
 * >Zookeeper manual</a>.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface ZookeeperEphemeralSequentialOperations {
  /**
   * Find all the child ephemeral sequential {@link SequentialZNode nodes} for a
   * given path
   * 
   * @param path
   *          a path representing the root of the ephemeral nodes collection
   * @return a {@link NavigableSet} of ordered {@link SequentialZNode}
   */
  NavigableSet<SequentialZNode> findChildren(final String path);

  /**
   * Create an ephemeral sequential node
   * 
   * @param path
   *          the root of the ephemeral sequential node collection (pass in the
   *          root not the full node name, Zookeeper will append the sequence,
   *          e.g., if you pass in /election/n_ for the path the first time the
   *          create node will have a path of /election/n_0000000000)
   * @return the {@link SequentialZNode} representing the created node
   */
  SequentialZNode createEphemeralSequential(String path);

  /**
   * Create an ephemeral sequential node with data
   * 
   * @param path
   *          the root of the ephemeral sequential node collection (pass in the
   *          root not the full node name, Zookeeper will append the sequence,
   *          e.g., if you pass in /election/n_ for the path the first time the
   *          create node will have a path of /election/n_0000000000)
   * @param data
   *          the data to store on the node
   * @return the {@link SequentialZNode} representing the created node
   */
  SequentialZNode createEphemeralSequential(String path, Object data);

}
