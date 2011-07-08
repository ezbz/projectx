package org.projectx.zookeeper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZooKeeperDaoTest {
  private static final String projectx_PATH = "/projectx";
  private static final String ROOT_PATH = projectx_PATH + "/election";
  private static final String EPHEMERAL_PATH = ROOT_PATH + "/n_";
  private static final String EPHEMERAL_NODE = EPHEMERAL_PATH + "0000000000";
  private ZooKeeperOperations classUnderTest;

  @Mock
  private ZkClient zkClient;

  @Before
  public void before() {
    classUnderTest = new ZooKeeperTemplate(zkClient);
    ((ZooKeeperTemplate) classUnderTest).setNodeCreationTimeoutSeconds(5);
  }

  @Test
  public void test_findChildren() {
    when(zkClient.getChildren(ROOT_PATH)).thenReturn(Collections.singletonList(EPHEMERAL_NODE));
    final NavigableSet<SequentialZNode> children = classUnderTest.findChildren(ROOT_PATH);

    verify(zkClient).getChildren(ROOT_PATH);
    assertEquals("incorrect number of results", 1, children.size());
    assertEquals("Incorrect full ROOT_PATH", EPHEMERAL_NODE, children.first().getFullPath());
  }

  @Test
  public void test_exists() {
    when(zkClient.exists(ROOT_PATH)).thenReturn(true);
    final boolean exists = classUnderTest.nodeExists(ROOT_PATH);

    verify(zkClient).exists(ROOT_PATH);
    assertEquals("incorrect result", true, exists);
  }

  @Test
  public void test_createEphemeralSequential() {
    when(zkClient.createEphemeralSequential(EPHEMERAL_PATH, null)).thenReturn(EPHEMERAL_NODE);
    when(zkClient.waitUntilExists(EPHEMERAL_NODE, TimeUnit.SECONDS, 5)).thenReturn(true);

    final SequentialZNode node = classUnderTest.createEphemeralSequential(ROOT_PATH);

    verify(zkClient).createEphemeralSequential(EPHEMERAL_PATH, null);
    verify(zkClient).waitUntilExists(EPHEMERAL_NODE, TimeUnit.SECONDS, 5);

    assertEquals("incorrect sequence", 0, node.getSequence().intValue());
    assertEquals("incorrect path", ROOT_PATH, node.getPath());
    assertEquals("incorrect full path", EPHEMERAL_NODE, node.getFullPath());
  }

  @Test(expected = ZkNoNodeException.class)
  public void test_createEphemeralSequentialFailure() {
    when(zkClient.waitUntilExists(EPHEMERAL_NODE, TimeUnit.SECONDS, 5)).thenReturn(false);

    classUnderTest.createEphemeralSequential(ROOT_PATH);
  }

  @Test
  public void test_createPersistentRootMissing() {
    when(zkClient.exists(projectx_PATH)).thenReturn(false);
    when(zkClient.exists(ROOT_PATH)).thenReturn(false);
    when(zkClient.waitUntilExists(projectx_PATH, TimeUnit.SECONDS, 5)).thenReturn(true);
    when(zkClient.waitUntilExists(ROOT_PATH, TimeUnit.SECONDS, 5)).thenReturn(true);

    classUnderTest.createPersistent(ROOT_PATH);

    verify(zkClient).exists(projectx_PATH);
    verify(zkClient).exists(ROOT_PATH);
    verify(zkClient).createPersistent(projectx_PATH);
    verify(zkClient).createPersistent(ROOT_PATH);
    verify(zkClient).waitUntilExists(projectx_PATH, TimeUnit.SECONDS, 5);
    verify(zkClient).waitUntilExists(ROOT_PATH, TimeUnit.SECONDS, 5);
  }

  @Test
  public void test_createPersistentRootExists() {
    when(zkClient.exists(projectx_PATH)).thenReturn(true);
    when(zkClient.exists(ROOT_PATH)).thenReturn(true);
    when(zkClient.waitUntilExists(projectx_PATH, TimeUnit.SECONDS, 5)).thenReturn(true);
    when(zkClient.waitUntilExists(ROOT_PATH, TimeUnit.SECONDS, 5)).thenReturn(true);

    classUnderTest.createPersistent(ROOT_PATH);

    verify(zkClient).exists(projectx_PATH);
    verify(zkClient).exists(ROOT_PATH);
    verify(zkClient, times(0)).createPersistent(projectx_PATH);
    verify(zkClient, times(0)).createPersistent(ROOT_PATH);
    verify(zkClient, times(0)).waitUntilExists(ROOT_PATH, TimeUnit.SECONDS, 5);
  }

  @Test(expected = ZkNoNodeException.class)
  public void test_createPersistentRootMissingFailure() {
    when(zkClient.exists(projectx_PATH)).thenReturn(false);
    when(zkClient.waitUntilExists(projectx_PATH, TimeUnit.SECONDS, 5)).thenReturn(false);

    classUnderTest.createPersistent(ROOT_PATH);
  }

  @Test
  public void test_createEphemeral() {
    final ZNode node = classUnderTest.createEphemeral(ROOT_PATH);

    verify(zkClient).createEphemeral(ROOT_PATH);

    assertEquals("incorrect path", ROOT_PATH, node.getPath());
  }

  @Test
  public void test_createEphemeralWithData() throws UnsupportedEncodingException {
    final Object data = new Object();
    final ZNode node = classUnderTest.createEphemeral(ROOT_PATH, data);

    final byte[] dataBytes = data.toString().getBytes("UTF-8");
    verify(zkClient).createEphemeral(ROOT_PATH, dataBytes);

    assertEquals("incorrect path", ROOT_PATH, node.getPath());
    assertEquals("incorrect data", data, node.getData());
  }
}
