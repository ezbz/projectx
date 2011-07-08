package org.projectx.selenium;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;

public class Util {

  public static String toUrl(final String mediaName) {
    return StringUtils.replace(mediaName, " ", "+");
  }

  public static void setTimeOut(final WebDriver webDriver) {
    webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

  }

}
