package org.projectx.zookeeper.presence;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ServiceMetaDataProvider implements MetaDataProvider {
  private static final Logger log = LoggerFactory.getLogger(ServiceMetaDataProvider.class);
  private final String entityName;

  public ServiceMetaDataProvider(final String entityName) {

    Assert.notNull(entityName, "entityName cannot be null");
    this.entityName = entityName;
  }

  @Override
  public Object getMetaData() {
    final StringBuilder sb = new StringBuilder();
    final RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
    final DateTime start = new DateTime(mx.getStartTime());
    appendLine(sb, "start-date", start.toString("yyyy-MM-dd HH:mm:ss"));

    return sb.toString();
  }

  private void appendLine(final StringBuilder sb, final String key, final String value) {
    sb.append("[").append(key).append(": ").append(value).append("] \n");
  }

}
