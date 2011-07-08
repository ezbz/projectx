package org.projectx.zookeeper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * A {@link FactoryBean} implementation for {@link ZkClient} objects.
 * <p>
 * This class uses either {@link #setZkConnection(ZkConnection) zkConnection} or
 * {@link #setEnsemble(String) ensemble} to construct a {@link ZkClient} (in
 * that order of preference). If none is provided an exception will be thrown
 * durinig initialization.
 * <p>
 * The {@link #setZkConnection(ZkConnection)} method allows for provisioning a
 * different {@link IZkConnection} implementation (possibly for testing
 * purposes).
 * <p>
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ZkClientFactoryBean implements FactoryBean<ZkClient>, InitializingBean, DisposableBean {

  private String ensemble;
  private int connectionTimeout = 20000; // 20 seconds time default to connect
  private int sessionTimeout = Integer.MAX_VALUE;
  private ZkSerializer serializer = new BytesPushThroughSerializer();
  private ZkClient zkClient;
  private IZkConnection zkConnection;

  private List<IZkStateListener> stateListeners;
  private Map<String, IZkChildListener> childListeners;
  private Map<String, IZkDataListener> dataListeners;

  public void setEnsemble(final String ensemble) {
    Assert.hasText(ensemble, "ensemble must be in the form host:port");
    this.ensemble = ensemble;
  }

  /**
   * The connection timeout is used only when {@link #setEnsemble(String)} is
   * used, it is ignored when {@link #setZkConnection(ZkConnection)} is
   * provided.
   * 
   * @param connectionTimeout
   */
  public void setConnectionTimeout(final int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setSessionTimeout(final int sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
  }

  public void setSerializer(final ZkSerializer serializer) {
    this.serializer = serializer;
  }

  /**
   * An optional property, when provided this will override the value provided
   * by the {@link #setEnsemble(String)} property.
   * 
   * @param zkConnection
   */
  public void setZkConnection(final IZkConnection zkConnection) {
    this.zkConnection = zkConnection;
  }

  /**
   * Provide a collection of {@link IZkStateListener} to handle global state
   * changes in Zookeeper. These listeners will be registered after
   * {@link ZkClient} creation
   * 
   * @param stateListeners
   *          a collection of {@link IZkStateListener}
   */
  public void setStateListeners(final List<IZkStateListener> stateListeners) {
    this.stateListeners = stateListeners;
  }

  /**
   * Provide a map of child paths wiht associated {@link IZkChildListener} to
   * handle child changes in Zookeeper. These listeners will be registered after
   * {@link ZkClient} creation.
   * 
   * Note that child listeners can be set up on paths that do not exist yet in
   * Zookeeper as "future" path watches, theses will be invoked upon the
   * creation of the designated path in Zookeper
   * 
   * @param childListeners
   *          a map of path to {@link IZkChildListener}
   */
  public void setChildListeners(final Map<String, IZkChildListener> childListeners) {
    this.childListeners = childListeners;
  }

  /**
   * Provide a map of child paths wiht associated {@link IZkDataListener} to
   * handle data changes in Zookeeper. These listeners will be registered after
   * {@link ZkClient} creation.
   * 
   * Note that data listeners can be set up on paths that do not exist yet in
   * Zookeeper as "future" path watches, theses will be invoked upon the
   * creation of the designated path in Zookeper
   * 
   * @param dataListeners
   *          a map of path to {@link IZkDataListener}
   */
  public void setDataListeners(final Map<String, IZkDataListener> dataListeners) {
    this.dataListeners = dataListeners;
  }

  @Override
  public ZkClient getObject() throws Exception {
    return zkClient;
  }

  @Override
  public Class<ZkClient> getObjectType() {
    return ZkClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

    if (null != zkConnection) {
      zkClient = new ZkClient(zkConnection, connectionTimeout, serializer);
    } else if (null != ensemble) {
      zkClient = new ZkClient(ensemble, sessionTimeout, connectionTimeout, serializer);
    } else {
      throw new IllegalArgumentException(
          "Either zkConnection or ensemble must be provided to create a ZkClient instance.");
    }

    registerListeners(zkClient);
  }

  /**
   * Register all supplied listeners if any.
   * 
   * @param zkClient
   */
  protected void registerListeners(final ZkClient zkClient) {
    if (null != stateListeners) {
      for (final IZkStateListener stateListener : stateListeners) {
        zkClient.subscribeStateChanges(stateListener);

      }
    }

    if (null != childListeners) {
      for (final Entry<String, IZkChildListener> entry : childListeners.entrySet()) {
        zkClient.subscribeChildChanges(entry.getKey(), entry.getValue());
      }
    }

    if (null != dataListeners) {
      for (final Entry<String, IZkDataListener> entry : dataListeners.entrySet()) {
        zkClient.subscribeDataChanges(entry.getKey(), entry.getValue());
      }
    }

  }

  @Override
  public void destroy() throws Exception {
    zkClient.close();
  }
}
