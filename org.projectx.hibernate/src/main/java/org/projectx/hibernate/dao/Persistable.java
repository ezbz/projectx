package org.projectx.hibernate.dao;

import java.io.Serializable;

/**
 * A super interface for persistable database objects. Provides common
 * identification for data access entities which can be represented as a
 * surrogate (synthetic) key but can also express composite or other types of
 * primary keys as long as they implement the {@link Serializable} interface.
 * <p>
 * <b>Note</b>: database entities which are implemented using synthetic keys
 * should not implement this interface directly but rather use the
 * {@link Entity} interface and return their own representation of the synthetic
 * key in {@link #getPrimaryKey()}.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface Persistable extends Serializable {

  /**
   * Get the database primary key for this entity. This typically synonymous to
   * the surrogate key of the object but can also represent other types.
   * 
   * @return the database primary key for this entity
   */
  public Serializable getPrimaryKey();

  /**
   * Determine the state entity as transient or persistent, this is typically
   * handled by checking the <code>null</code> state of the primary key
   * 
   * @return true if this a persistent entity, false if it is transient (i.e.,
   *         new or unsaved)
   */
  public boolean isPersistent();
}
