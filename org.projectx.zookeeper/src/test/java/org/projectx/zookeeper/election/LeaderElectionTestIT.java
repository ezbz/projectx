package org.projectx.zookeeper.election;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.I0Itec.zkclient.ZkClient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.TestInMemoryConnection;
import org.projectx.zookeeper.ZookeeperOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This test assumes Zookeeper server has been started on localhost:2181 or a
 * {@link TestInMemoryConnection} is supplied to the client
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LeaderElectionTestIT {

  static {
    System.setProperty("zookeeper.entity.name", LeaderElectionTestIT.class.getSimpleName());
  }

  @Resource
  LeaderElector elector;

  @Resource
  ZookeeperOperations zkTemplate;

  @Resource
  ZkClient zkClient;

  @Test
  public void testElection() {
    assertTrue("state should be connected", elector.isConnected());
    assertTrue("should be leader", elector.isLeader());
    assertEquals("node and leader should be equals", elector.getNode(), elector.getLeader());

    elector.disconnected();

    assertFalse("state should be disconnected", elector.isConnected());

    final DateTime prevElection = elector.getLastElectionTime();

    elector.electNewLeader();

    assertTrue("incorrect last election time", elector.getLastElectionTime().isAfter(prevElection));
    assertTrue("state should be connected", elector.isConnected());
  }

  @Test
  public void testReElection() {
    final SequentialZNode startupNode = elector.getNode();
    final DateTime prevElection = elector.getLastElectionTime();
    final SequentialZNode newLeader = zkTemplate.createEphemeralSequential(startupNode.getPath());
    zkTemplate.delete(startupNode.getFullPath());
    assertFalse("leader should be deleted", zkClient.exists(startupNode.getFullPath()));

    // allow enough time for event thread to respond
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (final InterruptedException e) {
    }

    assertEquals("incorrect leader", newLeader, elector.getLeader());
    assertTrue("incorrect sequence", startupNode.getSequence() < elector.getNode().getSequence());
    assertTrue("incorrect sequence", newLeader.getSequence() < elector.getNode().getSequence());
    assertTrue("incorrect last election time", elector.getLastElectionTime().isAfter(prevElection));
    assertTrue("state should be connected", elector.isConnected());
  }
}
