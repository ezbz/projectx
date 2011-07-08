package org.projectx.hibernate.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for the data access layer abstraction framework. Provides
 * common operations associated with {@link Persistable} implementations.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public final class DaoUtils {

  private DaoUtils() {
  }

  /**
   * Order entities in memory. This is typically used after retrieval of a
   * collection of entities using an <code>in</code> clause. The latter doesn't
   * always retrieve a collection of entities in the same order as the primary
   * keys provided to the query, hence the need for in memory ordering.
   * 
   * @param <T>
   * @param entities
   *          the {@link Collection} of {@link Persistable} entities to order
   * @param orderBy
   *          the collection of primary keys to order by
   * @return a new {@link List} of {@link Persistable entities} ordered
   *         according to the primary keys provided.
   */
  public static <T extends Persistable> List<T> orderEntitiesByPrimaryKeys(final List<T> entities,
      final List<? extends Serializable> orderBy) {
    final Map<Serializable, T> id2Entity = new HashMap<Serializable, T>();
    for (final T entity : entities) {
      id2Entity.put(entity.getPrimaryKey(), entity);
    }

    final List<T> sortedEntities = new LinkedList<T>();
    for (final Serializable pk : orderBy) {
      sortedEntities.add(id2Entity.get(pk));
    }

    return sortedEntities;
  }
}
