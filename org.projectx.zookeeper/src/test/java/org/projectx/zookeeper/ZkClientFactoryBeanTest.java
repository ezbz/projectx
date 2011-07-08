package org.projectx.zookeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.InMemoryConnection;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZkClientFactoryBeanTest {
  private static final int CONNECTION_TIMEOUT = 100;
  private static final int SESSION_TIMEOUT = 4000;
  ZkClientFactoryBean classUnderTest;
  @Mock
  private ZkSerializer serializer;

  private final IZkConnection zkConnection = new InMemoryConnection();

  @Before
  public void before() {
    classUnderTest = new ZkClientFactoryBean();
  }

  @Test
  public void test_createBasicProperties() {
    classUnderTest.setZkConnection(zkConnection);
    assertEquals("Incorrect object type", ZkClient.class, classUnderTest.getObjectType());
    assertEquals("Incorrect singleton value", true, classUnderTest.isSingleton());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsufficientParameters() throws Exception {
    classUnderTest.afterPropertiesSet();
  }

  @Test
  public void test_createEnsemble() {
    classUnderTest.setZkConnection(zkConnection);
    classUnderTest.setEnsemble("127.0.0.1:21811");
    ZkClient zkClient = null;
    try {
      classUnderTest.afterPropertiesSet();
      zkClient = classUnderTest.getObject();
      assertNotNull("target object is null", zkClient);
      zkClient.close();
    } catch (final Exception e) {
      throw new IllegalStateException(e);
    } finally {
      if (null != zkClient) {
        zkClient.close();
      }
    }
  }

  @Test
  public void test_createZkConnection() throws Exception {
    classUnderTest.setZkConnection(zkConnection);
    classUnderTest.afterPropertiesSet();
    final ZkClient zkClient = classUnderTest.getObject();
    assertNotNull("target object is null", zkClient);
  }

  @Test
  public void test_createWithAllSetters() throws Exception {
    classUnderTest.setZkConnection(zkConnection);
    classUnderTest.setConnectionTimeout(CONNECTION_TIMEOUT);
    classUnderTest.setSessionTimeout(SESSION_TIMEOUT);
    classUnderTest.setSerializer(serializer);
    classUnderTest.afterPropertiesSet();
    final ZkClient zkClient = classUnderTest.getObject();
    assertNotNull("target object is null", zkClient);
  }
}
