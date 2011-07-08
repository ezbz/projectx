package org.projectx.core;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class CoreIntegrationTestIT {
  private static final Logger log = LoggerFactory.getLogger(CoreIntegrationTestIT.class);
  @Resource
  private DateTime someBean;

  @Before
  public void before() {
    log.info("Date: {} ", someBean);
  }

  @Test
  public void test_something() {
    assertNotNull("someBean should not be null", someBean);
  }

}
