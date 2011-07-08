package org.projectx.hibernate.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An abstract implementation of the {@link Entity} interface which provides the
 * necessary functionality to represent the most basic part of a persistent
 * database entity.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public abstract class AbstractEntity implements Entity {

  private static final long serialVersionUID = 1L;

  private Long id;

  /**
   * Empty constructor, required by most ORM implementations
   */
  public AbstractEntity() {
  }

  /**
   * Default constructor
   * 
   * @param id
   *          the Long value representing the identity of this entity, cannot be
   *          null
   */
  public AbstractEntity(final Long id) {
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  @Override
  public boolean isPersistent() {
    return null != getPrimaryKey();
  }

  @Override
  public Serializable getPrimaryKey() {
    return id;
  }

  @Override
  public String toString() {
    final ToStringBuilder tsb = new ToStringBuilder(this);
    tsb.append("id", id);
    return tsb.toString();
  }
}
