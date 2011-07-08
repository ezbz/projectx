package org.projectx.selenium;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.springframework.util.Assert;

public class LinkBuilder {

  private final List<WebElement> elements;

  private LinkBuilder(final List<WebElement> elements) {
    Assert.notEmpty(elements, "elements cannot be empty");
    this.elements = elements;
  }

  public static LinkBuilder createBuilder(final List<WebElement> elements) {
    return new LinkBuilder(elements);
  }

  public static LinkBuilder createBuilder(final SearchContext context,
      final String... partialMatches) {
    return new LinkBuilder(findElementsBy(context, partialMatches));
  }

  public static LinkBuilder createBuilder(final SearchContext context) {
    final List<WebElement> linkElements = context.findElements(By.tagName("a"));

    return new LinkBuilder(linkElements);
  }

  public List<Link> getLinks() {
    return getLinks(new ElementFilter() {

      @Override
      public boolean matches(final WebElement element) {
        return true;
      }
    });
  }

  public List<Link> getLinks(final ElementFilter elementFilter) {
    final List<Link> result = new LinkedList<Link>();
    for (final WebElement webElement : elements) {
      if (elementFilter.matches(webElement)) {
        final String linkText = webElement.getText();
        final String href = webElement.getAttribute("href");
        final String title = webElement.getAttribute("title");
        final Link link = new Link(href, linkText, title);
        result.add(link);
      }
    }
    return result;
  }

  public List<Link> findLinksBy(final SearchContext context, final ElementFilter filter,
      final String... partialMatch) {
    final List<WebElement> elements = findElementsBy(context, partialMatch);
    final List<Link> result = new LinkedList<Link>();
    for (final WebElement webElement : elements) {
      if (filter.matches(webElement)) {
        final String linkText = webElement.getText();
        final String href = webElement.getAttribute("href");
        final String title = webElement.getAttribute("title");
        final Link link = new Link(href, linkText, title);
        result.add(link);
      }
    }
    return result;
  }

  public List<Link> findLinksBy(final SearchContext context, final String... partialMatch) {
    return findLinksBy(context, new ElementFilter() {

      @Override
      public boolean matches(final WebElement element) {
        return true;
      }
    }, partialMatch);
  }

  private static List<WebElement> findElementsBy(final SearchContext context,
      final String... partialMatches) {
    final StringBuilder sb = new StringBuilder();
    sb.append("//a[");
    for (int i = 0; i < partialMatches.length; i++) {
      sb.append("contains(@href,'" + partialMatches[i] + "')");
      if (i < partialMatches.length - 1) {
        sb.append(" and ");
      }
    }
    sb.append("]");
    return context.findElements(By.xpath(sb.toString()));
  }

}
