package org.projectx.hibernate.dao;

/**
 * An extension of the {@link Persistable} interface used for persistent
 * database entities whose identifier is strictly a surrogate key expressed as a
 * {@link Long}.
 * <p>
 * Most ORM entities should implement this interface (or extend the
 * {@link AbstractEntity} abstract class).
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface Entity extends Persistable {
  /**
   * Get the primary key for this entity
   * 
   * @return a {@link Long} representation for the primary key
   */
  public Long getId();
}
