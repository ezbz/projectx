package org.projectx.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;

public interface LeaderElectionNodeListener extends IZkStateListener, IZkDataListener {

}
