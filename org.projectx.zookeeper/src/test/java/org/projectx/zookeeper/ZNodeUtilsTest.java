package org.projectx.zookeeper;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZNodeUtilsTest {

  private static final String NODE_ROOT = "/projectx/election";

  private final SequentialZNode LEADER = new EphemeralZNodeImpl(NODE_ROOT, 0);
  private final SequentialZNode FOLLOWER1 = new EphemeralZNodeImpl(NODE_ROOT, 1);
  private final SequentialZNode FOLLOWER2 = new EphemeralZNodeImpl(NODE_ROOT, 2);

  private final NavigableSet<SequentialZNode> nodes = new TreeSet<SequentialZNode>();

  @Before
  public void before() {
    Collections.addAll(nodes, LEADER, FOLLOWER1, FOLLOWER2);
  }

  @After
  public void after() {
    nodes.clear();
  }

  @Test
  public void test_findNextLeader_Follower1() {
    final SequentialZNode leader = ZNodeUtils.findNextLeader(nodes, FOLLOWER1);
    assertEquals("incorrect leader", leader, LEADER);
  }

  @Test
  public void test_findNextLeader_Follower2() {
    final SequentialZNode leader = ZNodeUtils.findNextLeader(nodes, FOLLOWER2);
    assertEquals("incorrect leader", leader, FOLLOWER1);
  }

  @Test
  public void test_findNextLeader_Leader() {
    final SequentialZNode leader = ZNodeUtils.findNextLeader(nodes, LEADER);
    assertEquals("incorrect leader", leader, LEADER);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_findNextLeader_EmptyList() {
    ZNodeUtils.findNextLeader(new TreeSet<SequentialZNode>(), LEADER);
  }
}
