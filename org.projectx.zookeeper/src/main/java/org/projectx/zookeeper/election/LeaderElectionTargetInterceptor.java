package org.projectx.zookeeper.election;

import java.util.concurrent.atomic.AtomicBoolean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link LeaderElectionTarget} implementation which also implements the
 * {@link MethodInterceptor} interface, allowing the leader election framework
 * to determine weather a method of a wrapped bean is executed or not.
 * <p>
 * <b>Note</b>: when the leader election framework has invoked the
 * {@link #stop()} method invocation is cancelled altogether and
 * <code>null</code> is returned from the {@link #invoke(MethodInvocation)}.
 * Ensure that consumers of the target bean are capable of handling
 * <code>null</code>s
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class LeaderElectionTargetInterceptor implements MethodInterceptor, LeaderElectionTarget {
  private static final Logger log = LoggerFactory.getLogger(LeaderElectionTargetInterceptor.class);
  private final AtomicBoolean enabled = new AtomicBoolean(false);
  private boolean disabled = false;

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    if (!disabled && enabled.get()) {
      return invocation.proceed();
    }
    log.debug("Target disabled, invocation deferred.");
    return null;
  }

  /**
   * Setting this value to true will cause the interceptor to be disabled
   * regardless of the dynamic flag indicated by {@link LeaderElectionTarget}.
   * It is typically used to provide a property based overriding mechanism to
   * the dynamic leader election process.
   * 
   * @param disabled
   *          a flag indicating weather this interceptor disables execution of
   *          the target altogether
   */
  public void setDisabled(final boolean disabled) {
    this.disabled = disabled;
  }

  @Override
  public void start() {
    log.info("Start was invoked, enabling intercepted invocation on target");
    enabled.set(true);
  }

  @Override
  public void stop() {
    log.info("Stop was invoked, disabling intercepted invocation on target");
    enabled.set(false);

  }

  @Override
  public boolean isRunning() {
    return enabled.get();
  }

}
