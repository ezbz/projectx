package org.projectx.zookeeper.election;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.EphemeralZNodeImpl;
import org.projectx.zookeeper.ZooKeeperOperations;

@RunWith(MockitoJUnitRunner.class)
public class LeaderElectorTest {
  private static final String NODE_ROOT = "/projectx/election";

  private final SequentialZNode NODE = new EphemeralZNodeImpl(NODE_ROOT, 1);
  private final SequentialZNode LEADER = new EphemeralZNodeImpl(NODE_ROOT, 0);

  private LeaderElectorImpl classUnderTest;

  @Mock
  private ZooKeeperOperations zkDao;

  @Mock
  private LeaderElectionTarget target;

  @Mock
  private LeaderElectionStrategy strategy;

  @Mock
  private ElectionStateFactory electionStateFactory;

  @Before
  public void before() {
    classUnderTest = new LeaderElectorImpl(zkDao, target, electionStateFactory, false);
    classUnderTest.setElectionStrategy(strategy);

    final ElectionState state = ElectionState.valueOf(NODE, LEADER);
    when(electionStateFactory.create()).thenReturn(state);
    when(zkDao.findChildren(NODE_ROOT)).thenReturn(new TreeSet<SequentialZNode>(Arrays.asList(LEADER)));
    classUnderTest.initialize();

    verify(electionStateFactory).create();
  }

  @Test
  public void test_electNewLeaderSingleNode() {
    when(zkDao.findChildren(NODE_ROOT)).thenReturn(new TreeSet<SequentialZNode>(Arrays.asList(LEADER)));
    when(zkDao.nodeExists(LEADER.getFullPath())).thenReturn(true);

    classUnderTest.electNewLeader();

    verify(zkDao, times(2)).findChildren(NODE_ROOT);
    verify(zkDao).nodeExists(LEADER.getFullPath());

    final ElectionState state = classUnderTest.getState();
    assertEquals("incorrect leader", 0, state.getLeader().getSequence().intValue());
  }

  @Test
  public void test_electNewLeaderMultipleNodes() {
    when(zkDao.findChildren(NODE_ROOT)).thenReturn(
        new TreeSet<SequentialZNode>(Arrays.asList(NODE, LEADER)));
    when(zkDao.nodeExists(LEADER.getFullPath())).thenReturn(true);

    classUnderTest.electNewLeader();

    verify(zkDao).findChildren(NODE_ROOT);
    verify(zkDao).nodeExists(LEADER.getFullPath());
    verify(target, times(0)).start();

    final ElectionState state = classUnderTest.getState();
    assertEquals("incorrect leader", LEADER, state.getLeader());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void test_electNewLeaderNoNodes() {
    when(zkDao.findChildren(NODE_ROOT)).thenReturn(null);

    final ElectionState state = ElectionState.valueOf(NODE, LEADER);
    when(electionStateFactory.create()).thenReturn(state);
    when(strategy.findNextLeader(any(NavigableSet.class), any(SequentialZNode.class))).thenReturn(NODE);

    classUnderTest.electNewLeader();

    verify(electionStateFactory, times(2)).create();
    verify(zkDao, times(2)).findChildren(NODE_ROOT);
  }

  @Test
  public void test_electNewLeaderMultipleNodes_LeaderDied() {
    validateLeaderElectionWhenLeaderDies(true);
  }

  private void validateLeaderElectionWhenLeaderDies(final boolean connectState) {
    final TreeSet<SequentialZNode> nodes = new TreeSet<SequentialZNode>(Arrays.asList(NODE));
    when(zkDao.findChildren(NODE_ROOT)).thenReturn(nodes);
    when(zkDao.nodeExists(LEADER.getFullPath())).thenReturn(false);
    when(strategy.findNextLeader(nodes, NODE)).thenReturn(NODE);
    when(strategy.isLeader(nodes, NODE)).thenReturn(true);
    when(target.isRunning()).thenReturn(false);

    classUnderTest.electNewLeader();

    verify(zkDao).findChildren(NODE_ROOT);
    verify(zkDao).nodeExists(LEADER.getFullPath());
    verify(strategy).findNextLeader(nodes, NODE);
    verify(strategy).isLeader(nodes, NODE);
    verify(target).isRunning();
    verify(target).start();

    final ElectionState state = classUnderTest.getState();
    assertEquals("incorrect leader, didn't change when leader died", 1, state.getLeader()
                                                                             .getSequence()
                                                                             .intValue());
  }

  @Test
  public void test_electNewLeaderMultipleNodes_AlreadyRunning() {
    final TreeSet<SequentialZNode> nodes = new TreeSet<SequentialZNode>(Arrays.asList(NODE));
    when(zkDao.findChildren(NODE_ROOT)).thenReturn(nodes);
    when(zkDao.nodeExists(LEADER.getFullPath())).thenReturn(false);
    when(strategy.findNextLeader(nodes, NODE)).thenReturn(NODE);
    when(strategy.isLeader(nodes, NODE)).thenReturn(true);
    when(target.isRunning()).thenReturn(true);

    classUnderTest.electNewLeader();

    verify(zkDao).findChildren(NODE_ROOT);
    verify(zkDao).nodeExists(LEADER.getFullPath());
    verify(strategy).findNextLeader(nodes, NODE);
    verify(strategy).isLeader(nodes, NODE);
    verify(target).isRunning();
    verify(target, times(0)).start();
  }

  @Test
  public void test_disconnected() {
    classUnderTest.disconnected();
    verify(target).stop();
  }

  @Test
  public void test_isConnected() {
    final boolean connected = classUnderTest.isConnected();
    assertEquals("incorrect connected value", false, connected);
  }

  @Test
  public void test_isLeader() {
    final boolean leader = classUnderTest.isLeader();
    assertEquals("incorrect leader value", false, leader);
  }

  @Test
  public void test_getLeaderNode() {
    final SequentialZNode leader = classUnderTest.getLeader();
    assertEquals("incorrect leader", LEADER, leader);
  }

  @Test
  public void test_handleDataDeleted_matchingNode() throws Exception {
    validateLeaderElectionWhenLeaderDies(true);
  }

  @Test
  public void test_handleStateChanged_KeeperDisconnected() throws Exception {
    classUnderTest.handleStateChanged(KeeperState.Disconnected);

    verify(target).stop();
  }

  @Test
  public void test_handleStateChanged_KeeperSessionExpired() throws Exception {
    classUnderTest.handleStateChanged(KeeperState.Expired);

    verify(target).stop();
    validateLeaderElectionWhenLeaderDies(true);
  }
}
