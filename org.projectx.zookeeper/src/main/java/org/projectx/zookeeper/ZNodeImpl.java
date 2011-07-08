package org.projectx.zookeeper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

public class ZNodeImpl implements ZNode, Comparable<ZNode> {

  private final String path;
  private final Object data;

  public ZNodeImpl(final String path) {
    this(path, new Object());
  }

  public ZNodeImpl(final String path, final Object data) {
    Assert.hasText(path, "path cannot be empty");
    this.path = path;
    this.data = data;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public Object getData() {
    return data;
  }

  @Override
  public String getFullPath() {
    return path;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof EphemeralZNodeImpl)) {
      return false;
    }

    final EphemeralZNodeImpl o = (EphemeralZNodeImpl) other;
    final EqualsBuilder eb = new EqualsBuilder();
    eb.append(getPath(), o.getPath());
    return eb.isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getPath()).toHashCode();
  }

  @Override
  public String toString() {
    final ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    tsb.append("path", getPath());
    return tsb.toString();
  }

  @Override
  public int compareTo(final ZNode o) {
    return path.compareTo(o.getPath());
  }

}
