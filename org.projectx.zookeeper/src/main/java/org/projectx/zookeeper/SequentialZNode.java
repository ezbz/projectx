package org.projectx.zookeeper;

/**
 * Represents a Sequential (possibly ephemeral) ZNode in Zookeeper
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface SequentialZNode extends ZNode {
  /**
   * Get the sequence number of the {@link SequentialZNode} instance
   * 
   * @return An integer representation of the sequence portion of the ZNode
   */
  Integer getSequence();
}
