package org.projectx.zookeeper;

/**
 * An interface used by zookeeper to determine the name of the currently running
 * entity in order to generate a path for a node. An entity is typically a
 * web-application or a java executable.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface EntityNameResolver {

  /**
   * Resolve the entity name
   * 
   * @return the name of the entity.
   */
  String resolve();
}
