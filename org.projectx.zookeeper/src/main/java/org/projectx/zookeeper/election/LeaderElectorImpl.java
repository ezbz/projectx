package org.projectx.zookeeper.election;

import java.util.NavigableSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.projectx.zookeeper.LeaderElectionNodeListener;
import org.projectx.zookeeper.SequentialZNode;
import org.projectx.zookeeper.ZookeeperOperations;

/**
 * An implementation of the {@link LeaderElector} interface based on a Zookeeper
 * DAO. Maintains the state of affairs by holding an atomic reference of the
 * current {@link ElectionState}. Notifies the execution target of changes in
 * the state of leadership via the provided {@link LeaderElectionTarget}
 * instance.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class LeaderElectorImpl implements LeaderElector, LeaderElectionNodeListener {

  private static final Logger log = LoggerFactory.getLogger(LeaderElectorImpl.class);

  private final ZookeeperOperations zkDao;
  private final LeaderElectionTarget leaderElectionTarget;
  private LeaderElectionStrategy electionStrategy = new AntiHerdingElectionStrategy();

  private final ElectionStateFactory electionStateFactory;

  private final AtomicReference<ElectionState> electionState = new AtomicReference<ElectionState>();

  private DateTime lastElection;

  private final AtomicBoolean connected = new AtomicBoolean(false);

  private LeaderElectionListener electionListener = new LoggingLeaderElectionListener();

  private final boolean disabled;

  public LeaderElectorImpl(final ZookeeperOperations zkDao,
      final LeaderElectionTarget leaderElectionTarget,
      final ElectionStateFactory electionStateFactory, final boolean disabled) {
    Assert.notNull(zkDao, "zkDao cannot be null");
    Assert.notNull(leaderElectionTarget, "leaderElectionTarget cannot be null");
    Assert.notNull(electionStateFactory, "electionStateFactory cannot be null");
    this.zkDao = zkDao;
    this.leaderElectionTarget = leaderElectionTarget;
    this.electionStateFactory = electionStateFactory;
    this.disabled = disabled;
  }

  @Override
  public void setElectionStrategy(final LeaderElectionStrategy electionStrategy) {
    this.electionStrategy = electionStrategy;
  }

  public void setLeaderElectionListener(final LeaderElectionListener electionListener) {
    this.electionListener = electionListener;
  }

  /**
   * Checks that the state of nodes in zookeeper is valid, then checks that the
   * leader is alive if it isn't alive checks if this instance is the current
   * leader and if it is starts the execution target.
   */
  @Override
  public void electNewLeader() {
    if (disabled) {
      return;
    }

    final NavigableSet<SequentialZNode> nodes = getValidChildren();
    final boolean leaderAlive = zkDao.nodeExists(getLeader().getFullPath());
    if (!leaderAlive) {
      SequentialZNode leader = getLeader();
      log.info("Leader node {} is not alive!", getLeader());
      leader = electionStrategy.findNextLeader(nodes, getNode());

      electionState.set(getState().valueOf(leader));

      log.info("Registering a listener for leader node {}", leader);
      registerLeaderListener(leader.getFullPath());
    }

    if (electionStrategy.isLeader(nodes, getNode()) && !leaderElectionTarget.isRunning()) {
      log.info("My node {}, is the elected leader", getNode());
      leaderElectionTarget.start();
      electionListener.handleNewLeader(getLeader());
    }

    log.info("Local node is: {}, leader node: {}", getNode(), getLeader());
    lastElection = new DateTime();
    connected.set(true);
  }

  /**
   * Gets a list of child nodes and verifies that the list is correct state
   * (i.e., list is not empty and self node exists in the list). If the list is
   * in illegal state, stops the election target to be safe and re-initializes
   * the self node state.
   * 
   * @return
   */
  private NavigableSet<SequentialZNode> getValidChildren() {
    final NavigableSet<SequentialZNode> nodes = getChildren();
    if (null == nodes || !nodes.contains(getNode())) {
      log.warn("Found a null list of child nodes or self node doesn't exist in node list, recreating self node.");
      leaderElectionTarget.stop();
      initialize();
      return getChildren();
    }

    return nodes;
  }

  /**
   * Get a list of child nodes from zookeeper
   * 
   * @return
   */
  private NavigableSet<SequentialZNode> getChildren() {
    final String rootPath = getNode().getPath();
    final NavigableSet<SequentialZNode> nodes = zkDao.findChildren(rootPath);
    log.info("Election Zookeeper nodes: {}", StringUtils.collectionToCommaDelimitedString(nodes));
    return nodes;
  }

  @Override
  public void disconnected() {
    leaderElectionTarget.stop();
    connected.set(false);
  }

  @Override
  public void initialize() {
    if (disabled) {
      return;
    }

    final ElectionState initialState = electionStateFactory.create();
    electionState.set(initialState);
    registerLeaderListener(initialState.getLeader().getFullPath());
  }

  private void registerLeaderListener(final String leaderPath) {
    zkDao.registerListener(leaderPath, this);
  }

  @Override
  public ElectionState getState() {
    return electionState.get();
  }

  @Override
  public boolean isConnected() {
    return connected.get();
  }

  @Override
  public boolean isLeader() {
    final ElectionState state = getState();
    return state.getLeader().equals(state.getNode());
  }

  @Override
  public SequentialZNode getNode() {
    return getState().getNode();
  }

  @Override
  public SequentialZNode getLeader() {
    return getState().getLeader();
  }

  @Override
  public DateTime getLastElectionTime() {
    return lastElection;
  }

  /**
   * An {@link IZkStateListener} implementation used to notify the leader
   * election framework about disconnects and reconnects from the Zookeeper
   * ensemble. The ZkClient handles reconnects on its own. However, when a
   * session has expired or disconnected the
   * {@link LeaderElectorListenerCallback} must be notified in order for work to
   * be stopped (or any other action the callback wishes to implement for this
   * scenario).
   * 
   * 
   * @author Erez Mazor (erezmazor@gmail.com)   * 
   */
  @Override
  public void handleStateChanged(final KeeperState state) throws Exception {
    switch (state) {
    case Disconnected:
      log.info("Zookeeper session Disconnected, this will cause the target execution to stop.");
      disconnected();
      break;
    case Expired:
      log.info("Zookeeper session Expired, this will cause the target execution to stop, and possibly for new nodes to be created.");
      disconnected();
      break;
    case SyncConnected:
      log.info("Zookeeper session SyncConnected. checking with leaderElector if a new election is required.");
      if (!isConnected()) {
        connected.set(true);
        electNewLeader();
      }
      break;

    }
  }

  @Override
  public void handleNewSession() throws Exception {
    log.warn("Zookeeper signalled a new session, this means that all ephemeral nodes have been removed, re-initializing session.");
    initialize();
  }

  @Override
  public void handleDataChange(final String dataPath, final Object data) throws Exception {

  }

  /**
   * {@link IZkDataListener} implementation intended to notify the Leader
   * Election framework about node deletions. This represents the primary entry
   * point trigger for a new election since a deletion of a leader node means
   * the next follower in line must assume leadership.
   * <p>
   * 
   */
  @Override
  public void handleDataDeleted(final String dataPath) throws Exception {
    final SequentialZNode leader = getLeader();

    if (dataPath.equals(leader.getFullPath())) {
      log.info("Detected leader node deletion at: {}", dataPath);
      electNewLeader();
    }

  }
}
