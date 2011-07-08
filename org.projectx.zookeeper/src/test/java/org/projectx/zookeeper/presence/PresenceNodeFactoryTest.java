package org.projectx.zookeeper.presence;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.EphemeralZNodeImpl;
import org.projectx.zookeeper.ZNode;
import org.projectx.zookeeper.ZooKeeperOperations;

@RunWith(MockitoJUnitRunner.class)
public class PresenceNodeFactoryTest {

  private static final String PATH = "/projectx/services/dev";
  private final SequentialZNode NODE = new EphemeralZNodeImpl(PATH + "testService", 1);
  private PresenceNodeFactory classUnderTest;
  @Mock
  private ZooKeeperOperations zkDao;

  @Mock
  private MetaDataProvider metaDataProvider;

  @Before
  public void before() {
    classUnderTest = new PresenceNodeFactory(zkDao, PATH, getClass().getSimpleName(),
        metaDataProvider);
  }

  @Test
  public void test_createPresenceNode() throws Exception {
    when(zkDao.createEphemeral(anyString(), anyObject())).thenReturn(NODE);
    final ZNode presenceNode = classUnderTest.createPresenceNode();

    assertEquals("presence node incorrect", NODE, presenceNode);
  }
}
