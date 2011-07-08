package org.projectx.selenium;

import org.openqa.selenium.WebElement;

public interface ElementFilter {
  boolean matches(WebElement element);
}
