package org.projectx.zookeeper;

/**
 * A representation of a ZooKeeper ZNode
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface ZNode {
  String getPath();

  String getFullPath();

  Object getData();

}
