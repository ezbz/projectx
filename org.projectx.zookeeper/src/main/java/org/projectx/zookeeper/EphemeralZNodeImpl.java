package org.projectx.zookeeper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents an ephemeral sequential node within Zookeeper.
 * <p>
 * <b>Note</b>: instance of this class should not be collected or hashed across
 * domains (paths). Zookeeper guarantees that sequences of ephemeral nodes are
 * unique for a given path. For this reason {@link #equals(Object)}
 * {@link #hashCode()} and {@link #compareTo(EphemeralZNodeImpl)} all rely on
 * the sequence number solely and ignore the path.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class EphemeralZNodeImpl implements SequentialZNode, Comparable<SequentialZNode> {
  private final ZNode zNode;
  private final Integer sequence;

  public EphemeralZNodeImpl(final String path, final Integer sequence) {
    this.zNode = new ZNodeImpl(path);
    this.sequence = sequence;
  }

  public EphemeralZNodeImpl(final String path, final Object data, final Integer sequence) {
    this.zNode = new ZNodeImpl(path, data);
    this.sequence = sequence;
  }

  @Override
  public Integer getSequence() {
    return sequence;
  }

  @Override
  public String getFullPath() {
    return EphemeralNodeNameUtils.constructNodeName(getPath(), sequence);
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof EphemeralZNodeImpl)) {
      return false;
    }

    final SequentialZNode o = (SequentialZNode) other;
    final EqualsBuilder eb = new EqualsBuilder();
    eb.append(zNode.getPath(), o.getPath());
    eb.append(getSequence(), o.getSequence());
    return eb.isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(getSequence()).toHashCode();
  }

  @Override
  public String toString() {
    final ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    tsb.append("path", getFullPath());
    return tsb.toString();
  }

  @Override
  public String getPath() {
    return zNode.getPath();
  }

  @Override
  public Object getData() {
    return zNode.getData();
  }

  @Override
  public int compareTo(final SequentialZNode o) {
    return sequence.compareTo(o.getSequence());
  }
}
