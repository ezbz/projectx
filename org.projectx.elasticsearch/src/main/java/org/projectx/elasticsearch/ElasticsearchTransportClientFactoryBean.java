package org.projectx.elasticsearch;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A {@link FactoryBean} implementation used to create a {@link TransportClient}
 * element which connects remotely to a cluster.
 * <p>
 * The lifecycle of the underlying {@link TransportClient} instance is tied to
 * the lifecycle of the bean via the {@link #destroy()} method which calls
 * {@link TransportClient#close()}
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ElasticsearchTransportClientFactoryBean implements FactoryBean<TransportClient>,
    InitializingBean, DisposableBean {

  protected final Log logger = LogFactory.getLog(getClass());

  private TransportClient transportClient;

  private Map<String, Integer> transportAddresses;

  public void setTransportAddresses(final Map<String, Integer> transportAddresses) {
    this.transportAddresses = transportAddresses;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    internalCreateTransportClient();
  }

  private void internalCreateTransportClient() {
    final TransportClient client = new TransportClient();

    if (null != transportAddresses) {
      for (final Entry<String, Integer> address : transportAddresses.entrySet()) {
        if (logger.isInfoEnabled()) {
          logger.info("Adding transport address: " + address.getKey() + " port: "
              + address.getValue());
        }
        client.addTransportAddress(new InetSocketTransportAddress(address.getKey(),
            address.getValue()));
      }
    }

  }

  @Override
  public void destroy() throws Exception {
    transportClient.close();
  }

  @Override
  public TransportClient getObject() throws Exception {
    return transportClient;
  }

  @Override
  public Class<TransportClient> getObjectType() {
    return TransportClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

}
