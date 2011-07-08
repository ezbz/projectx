package org.projectx.zookeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.ServerSocket;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkServer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test for the {@link ZkServerFactoryBean} class.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class ZkServerFactoryBeanTest {

  private int port;
  private static final String TMPDIR = System.getProperty("java.io.tmpdir");
  ZkServerFactoryBean classUnderTest;
  @Mock
  private IDefaultNameSpace mockNamesapce;

  @Before
  public void before() throws Exception {
    port = getFreePort();
    classUnderTest = new ZkServerFactoryBean();
    classUnderTest.setDefaultNameSpace(mockNamesapce);
    classUnderTest.setDataDirectory(TMPDIR);
    classUnderTest.setLogDirectory(TMPDIR);
    classUnderTest.setMinSessionTimeout(2000);
    classUnderTest.setTickTime(5);
    classUnderTest.setPort(port);
  }

  private int getFreePort() throws IOException {
    final ServerSocket socket = new ServerSocket(0);
    final int localPort = socket.getLocalPort();
    socket.close();
    return localPort;
  }

  @Test
  @Ignore
  public void test_createBasicProperties() {
    assertEquals("Incorrect object type", ZkServer.class, classUnderTest.getObjectType());
    assertEquals("Incorrect singleton value", true, classUnderTest.isSingleton());
  }

  @Test
  @Ignore
  public void test_createServer() throws Exception {
    ZkServer zkServer = null;
    try {
      classUnderTest.afterPropertiesSet();
      zkServer = classUnderTest.getObject();
      assertNotNull("target object is null", zkServer);
      assertNotNull("client is null", zkServer.getZkClient());
      assertEquals("incorrect port", port, zkServer.getPort());
    } finally {
      final ZkClient zkClient = classUnderTest.getObject().getZkClient();
      if (null != zkClient) {
        classUnderTest.destroy();
      }
    }
  }
}
