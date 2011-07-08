package org.projectx.hibernate.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.Assert;

/**
 * An abstract implementation of the {@link HibernateCallback} interface which
 * is concerned with an ordered query callback. Means to order by a specific
 * property name and determine the order direction are provided as well as a
 * method to populate the underlying {@link GenericCriteria} as denoted by
 * implementing the {@link CriteriaCallback} interface.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the type of the entity supported by this callback
 */

public abstract class AbstractOrderCallback<T> implements HibernateCallback<List<T>>,
    CriteriaCallback {

  private final String property;
  private final boolean isAscending;
  private final Class<T> clazz;

  public AbstractOrderCallback(final String property, final boolean isAscending,
      final Class<T> clazz) {
    Assert.hasText(property, "property must contain text");
    Assert.notNull(clazz, "clazz cannot be null");
    this.property = property;
    this.isAscending = isAscending;
    this.clazz = clazz;
  }

  @Override
  public List<T> doInHibernate(final Session session) throws HibernateException, SQLException {
    final GenericCriteria<T> criteria = new GenericCriteria<T>(session, clazz);
    final Order order = isAscending ? Order.asc(property) : Order.desc(property);
    criteria.addOrder(order);
    prepare(criteria);
    return criteria.list();
  }
}