package org.projectx.zookeeper;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for manipulating ephemeral node names, paths and sequences
 * managed by Zookeeper
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class EphemeralNodeNameUtils {

  /**
   * Parses the sequence values from an ephemeral/sequential node name. Assumes
   * a conventional sequence separator for the ephemeral node name as defined by
   * {@link ZookeeperConstants#SEQUENCE_SEAPARTOR}.
   * <p>
   * <b>Example</b>: the expected Zookeeper node name is in the form of
   * /aaa/bbb/ccc/n_0000000015, this method will return 15.
   * 
   * @param path
   *          an ephemeral/sequential node name
   * @return the sequence value of the node name
   */
  public static int parseSequenceFromName(final String path) {
    if (StringUtils.isEmpty(path)) {
      throw new IllegalArgumentException("path value cannot be empty");
    }
    final int idx = path.lastIndexOf(ZookeeperConstants.SEQUENCE_SEAPARTOR);
    final String sequence = path.substring(idx + 1, path.length());
    return Integer.valueOf(sequence);
  }

  /**
   * This method does the inverse of what {@link #parseSequenceFromName(String)}
   * does. Given an ephemeral node root (i.e. /aaa/bbb/ccc/) and a sequence
   * number it will return a string representing the ephemeral/sequential full
   * node name (e.g., /aaa/bbb/ccc/n_15)
   * 
   * @param ephemeralNodeRoot
   *          a root node for ephemeral/sequential (e.g., /aaa/bbb/ccc)
   * @param sequenceNumber
   *          the sequence number of the ephemeral/sequential
   * @return the full path to the ephemeral/sequential node (e.g.,
   *         /aaa/bbb/ccc/n_15)
   */
  public static String constructNodeName(final String nodeRoot, final int sequenceNumber) {
    final String paddedSequence = StringUtils.leftPad(Integer.toString(sequenceNumber),
        ZookeeperConstants.SEQUENCE_PADDING_LENGTH, ZookeeperConstants.SEQUENCE_PADDING_CHARACTER);
    final StringBuilder sb = new StringBuilder();
    sb.append(nodeRoot);
    sb.append(ZookeeperConstants.PATH_SEPARATOR);
    sb.append(ZookeeperConstants.EPHERMAL_PREFIX);
    sb.append(paddedSequence);
    return sb.toString();
  }
}
