package org.projectx.zookeeper.election;

import org.joda.time.DateTime;

import org.projectx.zookeeper.SequentialZNode;

public interface LeaderElector {

  void disconnected();

  boolean isConnected();

  boolean isLeader();

  SequentialZNode getNode();

  SequentialZNode getLeader();

  void electNewLeader();

  ElectionState getState();

  DateTime getLastElectionTime();

  void setElectionStrategy(final LeaderElectionStrategy electionStrategy);

  void initialize();
}
