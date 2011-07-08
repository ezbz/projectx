package org.projectx.webapp.welcome;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeLogger {
  private static final Logger log = LoggerFactory.getLogger(TimeLogger.class);

  public void logTime() {
    log.warn(":-) The time is now: " + new Date().toString());
  }
}
