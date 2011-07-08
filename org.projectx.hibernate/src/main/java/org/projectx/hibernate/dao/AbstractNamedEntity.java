package org.projectx.hibernate.dao;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An abstract implementation of the {@link NamedEntity} interface which extends
 * {@link AbstractEntity} and adds a get database entity.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public abstract class AbstractNamedEntity extends AbstractEntity implements NamedEntity {
  private static final long serialVersionUID = 1L;

  private String name;

  /**
   * Empty constructor, required by most ORM implementations
   */
  public AbstractNamedEntity() {
  }

  /**
   * Default constructor
   * 
   * @param id
   *          the Long value representing the identity of this entity, cannot be
   *          null
   * @param name
   *          a {@link String} representation of the entity name
   */
  public AbstractNamedEntity(final Long id, final String name) {
    super(id);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final ToStringBuilder tsb = new ToStringBuilder(this);
    tsb.appendSuper(super.toString());
    tsb.append("name", name);
    return tsb.toString();
  }
}
