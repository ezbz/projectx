package org.projectx.zookeeper;

/**
 * An object formatter for data stored in Zookeeper
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public interface ObjectFormatter {
  byte[] format(Object object);
}
