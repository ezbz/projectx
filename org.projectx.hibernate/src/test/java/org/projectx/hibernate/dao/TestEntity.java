package org.projectx.hibernate.dao;

/**
 * An {@link Entity} mock implementation
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class TestEntity extends AbstractEntity implements Entity {
  private static final long serialVersionUID = 1L;

  public TestEntity(final Long id) {
    super(id);
  }

}
