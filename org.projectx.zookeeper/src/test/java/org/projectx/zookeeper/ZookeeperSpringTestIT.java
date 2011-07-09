package org.projectx.zookeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.projectx.zookeeper.presence.PresenceManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ZookeeperSpringTestIT {
  private static final String TEST_PATH = "/projectx/test/"
      + ZookeeperSpringTestIT.class.getSimpleName();

  static {
    System.setProperty("zookeeper.entity.name", ZookeeperSpringTestIT.class.getSimpleName());
  }

  @Resource
  private ZkClient zkClient;

  @Resource
  ZookeeperOperations zkTemplate;

  @Resource
  PresenceManager presenceManager;

  @Before
  @After
  public void cleanup() {
    zkClient.deleteRecursive(TEST_PATH);
  }

  @Test
  public void testZkClient() {
    zkClient.createPersistent(TEST_PATH);
    assertTrue("root node missing", zkClient.exists(TEST_PATH));
    zkClient.delete(TEST_PATH);
    assertFalse("root node exists", zkClient.exists(TEST_PATH));
  }

  @Test
  public void testZkTemplate() {
    final ZNode eph = zkTemplate.createEphemeral(TEST_PATH);
    assertNotNull(eph);
    assertEquals(TEST_PATH, eph.getPath());
  }

  @Test
  public void testPresence() throws Exception {
    final ZNode presenceNode = presenceManager.getPresenceNode();

    assertNotNull(presenceNode);

    presenceManager.handleStateChanged(KeeperState.Disconnected);

    assertTrue("presence node missing", zkClient.exists(presenceNode.getFullPath()));

    presenceManager.handleNewSession();

    assertTrue("presence node missing", zkClient.exists(presenceNode.getFullPath()));
  }
}
