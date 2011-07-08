package org.projectx.zookeeper;

import java.util.concurrent.atomic.AtomicInteger;

import org.I0Itec.zkclient.InMemoryConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

public class TestInMemoryConnection extends InMemoryConnection {

  private final AtomicInteger sequence = new AtomicInteger(0);

  @Override
  public String create(final String path, final byte[] data, final CreateMode mode)
      throws KeeperException, InterruptedException {
    String createdPath = path;
    if (mode == CreateMode.EPHEMERAL_SEQUENTIAL) {
      final int newSequence = sequence.incrementAndGet();
      createdPath = createdPath + StringUtils.leftPad(Integer.toString(newSequence), 10, "0");
    }

    return super.create(createdPath, (null == data) ? new byte[0] : data, mode);
  }
}
