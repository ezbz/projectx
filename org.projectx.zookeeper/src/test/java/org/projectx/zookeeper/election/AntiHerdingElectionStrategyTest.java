package org.projectx.zookeeper.election;

import static org.junit.Assert.assertEquals;

import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.EphemeralZNodeImpl;

@RunWith(MockitoJUnitRunner.class)
public class AntiHerdingElectionStrategyTest {

  private static final String NODE_ROOT = "/projectx/election";
  private final SequentialZNode LEADER = new EphemeralZNodeImpl(NODE_ROOT, 0);
  private final SequentialZNode FOLLOWER = new EphemeralZNodeImpl(NODE_ROOT, 1);

  private LeaderElectionStrategy classUnderTest;
  private NavigableSet<SequentialZNode> nodes;

  @Before
  public void before() {
    classUnderTest = new AntiHerdingElectionStrategy();
    nodes = new TreeSet<SequentialZNode>();
    nodes.add(LEADER);
    nodes.add(FOLLOWER);
  }

  @Test
  public void test_findNextLeader() {
    final SequentialZNode nextLeader = classUnderTest.findNextLeader(nodes, FOLLOWER);

    assertEquals("incorrect leader", LEADER, nextLeader);
  }

  @Test
  public void test_isLeader() {
    final boolean isLeader = classUnderTest.isLeader(nodes, FOLLOWER);

    assertEquals("incorrect leader", false, isLeader);
  }
}