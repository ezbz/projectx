package org.projectx.zookeeper.election.quartz;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerElectionTargetTest {
  private SchedulerElectionTarget classUnderTest;
  @Mock
  private Scheduler scheduler;

  @Before
  public void before() {
    classUnderTest = new SchedulerElectionTarget(scheduler, false);
  }

  @Test
  public void testStart() throws SchedulerException {
    classUnderTest.start();
    verify(scheduler).start();
  }

  @Test
  public void testStart_withException() throws SchedulerException {
    doThrow(new SchedulerException()).when(scheduler).start();
    classUnderTest.start();
    verify(scheduler).start();
  }

  @Test
  public void testStop() throws SchedulerException {
    classUnderTest.stop();
    verify(scheduler).standby();
  }

  @Test
  public void testStop_withException() throws SchedulerException {
    doThrow(new SchedulerException()).when(scheduler).standby();
    classUnderTest.stop();
    verify(scheduler).standby();
  }

  @Test
  public void testIsRunning() throws SchedulerException {
    when(scheduler.isInStandbyMode()).thenReturn(false);
    final boolean running = classUnderTest.isRunning();
    verify(scheduler).isInStandbyMode();
    assertEquals("incorrect running result", true, running);
  }

  @Test(expected = IllegalStateException.class)
  public void testIsRunning_withException() throws SchedulerException {
    doThrow(new SchedulerException()).when(scheduler).isInStandbyMode();
    final boolean running = classUnderTest.isRunning();
    verify(scheduler).isInStandbyMode();
    assertEquals("incorrect running result", true, running);
  }

}
