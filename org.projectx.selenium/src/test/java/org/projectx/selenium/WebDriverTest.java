package org.projectx.selenium;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverTest {
  private WebDriver driver;

  private final Map<String, Integer> failedMap = new HashMap<String, Integer>();

  @Before
  public void before() {

    driver = new FirefoxDriver();
  }

  @After
  public void after() {
    driver.close();
    System.out.println("++++++++++++++++++++++ SUMMARY ++++++++++++++++++++++");
    for (final Map.Entry<String, Integer> entry : failedMap.entrySet()) {
      System.out.println(entry.getKey() + ":" + entry.getValue());
    }
  }

  private static final String BASE_URL = "http://ci1.nydc1.projectx.com:8111";

  @Test
  public void testTC() throws Exception {

    driver.get(BASE_URL + "/login.html");
    final WebElement username = driver.findElement(By.id("username"));
    username.sendKeys("erez");
    final WebElement password = driver.findElement(By.id("password"));
    password.sendKeys("Tornado9");

    final WebElement login = driver.findElement(By.name("submitLogin"));
    login.click();

    for (int i = 1; i < 6; i++) {
      driver.get(BASE_URL
          + "/searchResults.html?query=status%3A%22Tests%20failed%22%20and%20configuration%3ATrunker%20-Cancelled&buildTypeId=bt10&byTime=true0&page="
          + i);

      final List<WebElement> testElements = driver.findElements(By.xpath("//a[@title='View build results']"));

      final List<Link> testLinks = LinkBuilder.createBuilder(testElements).getLinks(
          new ElementFilter() {

            @Override
            public boolean matches(final WebElement element) {
              return element.getText().startsWith("#");
            }
          });

      for (final Link testLink : testLinks) {
        getBuildStats(testLink.getText(), testLink.getHref());
      }
    }
  }

  private void getBuildStats(final String buildNum, final String uri) {
    driver.get(BASE_URL + uri);

    final WebElement buildStats = driver.findElement(By.id("buildDataDetails_buildResultsDiv"));

    final WebElement testResult = driver.findElement(By.id("failedTestsDl"));

    final List<WebElement> failedTests = testResult.findElements(By.tagName("strike"));

    for (final WebElement failedTest : failedTests) {
      final String key = failedTest.getText();
      System.out.println(buildNum + "|" + key + "|" + buildStats.getText());
      if (failedMap.containsKey(key)) {
        failedMap.put(key, failedMap.get(key).intValue() + 1);
      } else {
        failedMap.put(key, 1);
      }
    }

  }
}
