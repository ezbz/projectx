package org.projectx.zookeeper.election;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LeaderElectionTargetInterceptorTest {

  @Mock
  private MethodInvocation invocation;

  LeaderElectionTargetInterceptor classUnderTest;

  @Before
  public void before() {
    classUnderTest = new LeaderElectionTargetInterceptor();
  }

  @Test
  public void test_initState() {
    assertFalse("Should be disabled", classUnderTest.isRunning());
  }

  @Test
  public void test_enabled() throws Throwable {
    classUnderTest.start();
    final Object object = new Object();
    when(invocation.proceed()).thenReturn(object);
    final Object returnValue = classUnderTest.invoke(invocation);

    verify(invocation).proceed();
    assertTrue("Should be running", classUnderTest.isRunning());
    assertEquals("return value incorrect", object, returnValue);
  }

  @Test
  public void test_disabled() throws Throwable {
    classUnderTest.stop();

    final Object returnValue = classUnderTest.invoke(invocation);
    verify(invocation, times(0)).proceed();
    assertFalse("Should be disabled", classUnderTest.isRunning());
    assertNull("return value should be null", returnValue);
  }
}
