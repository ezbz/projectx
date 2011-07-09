package org.projectx.zookeeper.election;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.EphemeralZNodeImpl;
import org.projectx.zookeeper.ZookeeperOperations;

@RunWith(MockitoJUnitRunner.class)
public class ElectionStateFactoryTest {
  private static final String NODE_ROOT = "/projectx/election";

  private final SequentialZNode NODE = new EphemeralZNodeImpl(NODE_ROOT, 1);
  private final SequentialZNode LEADER = new EphemeralZNodeImpl(NODE_ROOT, 0);
  private ElectionStateFactory classUnderTest;
  @Mock
  private ZookeeperOperations zkDao;

  @Before
  public void before() {
    classUnderTest = new ElectionStateFactory(zkDao, NODE_ROOT, getClass().getSimpleName());
  }

  @Test
  public void test_create() throws Exception {
    final String electionPath = NODE_ROOT + "/" + getClass().getSimpleName() + "/election";
    when(zkDao.nodeExists(electionPath)).thenReturn(false);
    when(zkDao.createEphemeralSequential(anyString(), anyObject())).thenReturn(NODE);
    when(zkDao.findChildren(electionPath)).thenReturn(new TreeSet<SequentialZNode>(Arrays.asList(LEADER)));

    final ElectionState state = classUnderTest.create();

    verify(zkDao).nodeExists(electionPath);
    verify(zkDao).createPersistent(electionPath);
    verify(zkDao).createEphemeralSequential(anyString(), anyObject());
    verify(zkDao).findChildren(electionPath);
    assertEquals("incorrect root", NODE, state.getNode());
    assertEquals("incorrect sequence", LEADER, state.getLeader());
  }

  @Test
  public void test_createNoPersistentNodesCreatedWhenRootNodeExists() throws Exception {
    final String electionPath = NODE_ROOT + "/" + getClass().getSimpleName() + "/election";
    when(zkDao.nodeExists(electionPath)).thenReturn(true);
    when(zkDao.createEphemeralSequential(anyString(), anyObject())).thenReturn(NODE);
    when(zkDao.findChildren(electionPath)).thenReturn(new TreeSet<SequentialZNode>(Arrays.asList(NODE)));

    classUnderTest.create();
    verify(zkDao, times(0)).createPersistent(electionPath);
    verify(zkDao).createEphemeralSequential(anyString(), anyObject());
    verify(zkDao).findChildren(electionPath);
  }
}
