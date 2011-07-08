package org.projectx.zookeeper;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.I0Itec.zkclient.ZkServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ZkServerFactoryBean implements FactoryBean<ZkServer>, InitializingBean, DisposableBean {

  private ZkServer zkServer;
  private int port = ZkServer.DEFAULT_PORT;
  public int tickTime = ZkServer.DEFAULT_TICK_TIME;
  public int minSessionTimeout = ZkServer.DEFAULT_MIN_SESSION_TIMEOUT;
  private IDefaultNameSpace defaultNameSpace = new DefaultNamespaceCreator("/");
  private String dataDirectory = ".";
  private String logDirectory = ".";

  public void setPort(final int port) {
    this.port = port;
  }

  public void setDefaultNameSpace(final IDefaultNameSpace defaultNameSpace) {
    this.defaultNameSpace = defaultNameSpace;
  }

  public void setTickTime(final int tickTime) {
    this.tickTime = tickTime;
  }

  public void setMinSessionTimeout(final int minSessionTimeout) {
    this.minSessionTimeout = minSessionTimeout;
  }

  public void setDataDirectory(final String dataDirectory) {
    this.dataDirectory = dataDirectory;
  }

  public void setLogDirectory(final String logDirectory) {
    this.logDirectory = logDirectory;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    zkServer = new ZkServer(dataDirectory, logDirectory, defaultNameSpace, port, tickTime,
        minSessionTimeout);
    zkServer.start();

  }

  @Override
  public ZkServer getObject() throws Exception {
    return zkServer;
  }

  @Override
  public Class<ZkServer> getObjectType() {
    return ZkServer.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void destroy() throws Exception {
    zkServer.shutdown();
  }
}
