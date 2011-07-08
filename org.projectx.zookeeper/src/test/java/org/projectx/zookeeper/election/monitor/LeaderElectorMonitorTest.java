package org.projectx.zookeeper.election.monitor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.EphemeralZNodeImpl;
import org.projectx.zookeeper.election.LeaderElector;

@RunWith(MockitoJUnitRunner.class)
public class LeaderElectorMonitorTest {

  private static final String NODE_ROOT = "/projectx/election";
  private final SequentialZNode NODE = new EphemeralZNodeImpl(NODE_ROOT, 0);

  private LeaderElectorMonitor classUnderTest;
  @Mock
  private LeaderElector elector;

  @Before
  public void before() {
    classUnderTest = new LeaderElectorMonitor(elector);
  }

  @Test
  public void testGetLeader() {
    when(elector.getLeader()).thenReturn(NODE);
    final String leader = classUnderTest.getLeader();
    assertEquals("incorrect leader", NODE.getFullPath(), leader);
    verify(elector).getLeader();
  }

  @Test
  public void testIsCurrentLeader() {
    when(elector.isLeader()).thenReturn(true);
    final boolean leader = classUnderTest.isCurrentLeader();
    assertEquals("incorrect leader", true, leader);
    verify(elector).isLeader();
  }

  @Test
  public void testIsConnected() {
    when(elector.isConnected()).thenReturn(true);
    final boolean connected = classUnderTest.isConnected();
    assertEquals("incorrect connected value", true, connected);
    verify(elector).isConnected();
  }
}
