package org.projectx.hibernate.dao;

/**
 * An extension of the {@link Entity} interface for database entities which have
 * a {@link #getName() name} property. Aimed to generalize entities whose name
 * are represented by a {@link String} value, a fairly common case for many
 * entities.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface NamedEntity extends Entity {

  /**
   * Get the logical name for the entity.
   * 
   * @return a {@link String} representation of the entity name
   */
  String getName();
}
