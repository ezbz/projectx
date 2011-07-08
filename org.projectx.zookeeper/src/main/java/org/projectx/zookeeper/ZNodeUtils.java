package org.projectx.zookeeper;

import java.util.NavigableSet;

public class ZNodeUtils {
  /**
   * Responsible for detecting a follower {@link SequentialZNode} leader by
   * looking at an ordered (!) list of {@link SequentialZNode candidate nodes}
   * and determining the next sequence number the node's sequence.
   * <p>
   * Given a list of size <code>n</code> and a follower node positioned at place
   * <code>i</code> in the list and with a sequence value of <code>s</code> in
   * this method will return the value of the element at index <code>i-1</code>
   * iff <code>i>0</code> and <code>n>1</code>
   * <p>
   * <b>Example</b>: if a the list contains nodes with sequences 3,5,8,10 and
   * the follower sequence is 8 return value is going to be the node with a
   * sequence of 5. Given the same list and a follower node with a sequence of 3
   * the return value is going to be the node with the sequence 3 (hence the
   * follower sequence is identical to the leader sequence).
   * <p>
   * If a list of size 1 is passed then the returned node will be the only
   * element in the list
   * <p>
   * If the sequence number of the follower node is already the smallest (i.e.,
   * it is the first node in the list), then that node is returned/
   * 
   * @param candidates
   *          a non-empty list of ordered candidate {@link SequentialZNode
   *          nodes}
   * @param node
   *          the follower node looking for a leader
   * @throws IllegalArgumentException
   *           if an empty list was passed for candidates. This is an illegal
   *           case since there are no nodes and no leader
   * @return the {@link SequentialZNode} representing the new leader for the
   *         follower node or the node itself if the list has only one element
   */
  public static SequentialZNode findNextLeader(final NavigableSet<SequentialZNode> candidates,
      final SequentialZNode node) {
    if (null == candidates || candidates.isEmpty()) {
      throw new IllegalArgumentException("Cannot use an empty or null list to find a leader");
    }

    if (candidates.size() > 1) {
      final SequentialZNode leader = candidates.lower(node);
      if (null != leader) {
        return leader;
      }
    }
    return candidates.first();
  }
}
