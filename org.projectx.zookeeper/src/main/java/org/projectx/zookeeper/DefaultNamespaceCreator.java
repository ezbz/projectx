package org.projectx.zookeeper;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.I0Itec.zkclient.ZkClient;

public class DefaultNamespaceCreator implements IDefaultNameSpace {
  private final String rootPath;

  public DefaultNamespaceCreator(final String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public void createDefaultNameSpace(final ZkClient zkClient) {
    if (!zkClient.exists(rootPath)) {
      zkClient.createPersistent(rootPath);
    }
  }

}
