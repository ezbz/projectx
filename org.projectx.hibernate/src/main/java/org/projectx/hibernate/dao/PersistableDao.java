package org.projectx.hibernate.dao;

/**
 * An interface for a Data Access object providing common operations for the
 * management of {@link Persistable} objects.
 * <p>
 * Represents an abstraction layer for operations involving database persistence
 * of a specific {@link Persistable} implementation.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the type of the {@link Persistable} interface which is data access
 *          object
 */
public interface PersistableDao<T extends Persistable> {
  /**
   * Get the {@link Class} of the supported entity, must be the same type as the
   * generic type of the data access object
   * 
   * @return the supported {@link Class}
   */
  Class<T> getEntityClass();
}
