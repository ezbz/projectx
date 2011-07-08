package org.projectx.selenium;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Link {
  String href;
  String text;
  String title;

  public Link(final String href, final String text, final String title) {
    this.href = href;
    this.text = text;
    this.title = title;
  }

  public String getHref() {
    return href;
  }

  public String getText() {
    return text;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }

}
