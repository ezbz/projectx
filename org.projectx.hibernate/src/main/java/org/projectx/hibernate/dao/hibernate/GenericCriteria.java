package org.projectx.hibernate.dao.hibernate;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

/**
 * An Adapter for the Hibernate {@link Criteria} API. All methods are delegated
 * to the underlying {@link Criteria} instance.
 * <p>
 * Use of this adapter provides a type-safe use of the {@link Criteria} and
 * alleviates the need for suppressing warning in consuming code.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the underlying type handled by the {@link Criteria} instance
 */

public class GenericCriteria<T> implements Criteria {

  private final Criteria criteria;

  public GenericCriteria(final Session session, final Class<? extends T> clazz) {
    this.criteria = session.createCriteria(clazz);
  }

  @Override
  public String getAlias() {
    return criteria.getAlias();
  }

  @Override
  public Criteria setProjection(final Projection projection) {
    return criteria.setProjection(projection);
  }

  @Override
  public Criteria add(final Criterion criterion) {
    return criteria.add(criterion);
  }

  @Override
  public Criteria addOrder(final Order order) {
    return criteria.addOrder(order);
  }

  @Override
  public Criteria setFetchMode(final String associationPath, final FetchMode mode)
      throws HibernateException {
    return criteria.setFetchMode(associationPath, mode);
  }

  @Override
  public Criteria setLockMode(final LockMode lockMode) {
    return criteria.setLockMode(lockMode);
  }

  @Override
  public Criteria setLockMode(final String alias, final LockMode lockMode) {
    return criteria.setLockMode(alias, lockMode);
  }

  @Override
  public Criteria createAlias(final String associationPath, final String alias)
      throws HibernateException {
    return criteria.createAlias(associationPath, alias);
  }

  @Override
  public Criteria createAlias(final String associationPath, final String alias, final int joinType)
      throws HibernateException {
    return criteria.createAlias(associationPath, alias, joinType);
  }

  @Override
  public Criteria createAlias(final String associationPath, final String alias, final int joinType,
      final Criterion withClause) throws HibernateException {
    return criteria.createAlias(associationPath, alias, joinType, withClause);
  }

  @Override
  public Criteria createCriteria(final String associationPath) throws HibernateException {
    return criteria.createCriteria(associationPath);
  }

  @Override
  public Criteria createCriteria(final String associationPath, final int joinType)
      throws HibernateException {
    return criteria.createCriteria(associationPath, joinType);
  }

  @Override
  public Criteria createCriteria(final String associationPath, final String alias)
      throws HibernateException {
    return criteria.createCriteria(associationPath, alias);
  }

  @Override
  public Criteria createCriteria(final String associationPath, final String alias,
      final int joinType) throws HibernateException {
    return criteria.createCriteria(associationPath, alias, joinType);
  }

  @Override
  public Criteria createCriteria(final String associationPath, final String alias,
      final int joinType, final Criterion withClause) throws HibernateException {
    return criteria.createCriteria(associationPath, alias, joinType, withClause);
  }

  @Override
  public Criteria setResultTransformer(final ResultTransformer resultTransformer) {
    return criteria.setResultTransformer(resultTransformer);
  }

  @Override
  public Criteria setMaxResults(final int maxResults) {
    return criteria.setMaxResults(maxResults);
  }

  @Override
  public Criteria setFirstResult(final int firstResult) {
    return criteria.setFirstResult(firstResult);
  }

  @Override
  public boolean isReadOnlyInitialized() {
    return criteria.isReadOnlyInitialized();
  }

  @Override
  public boolean isReadOnly() {
    return criteria.isReadOnly();
  }

  @Override
  public Criteria setReadOnly(final boolean readOnly) {
    return criteria.setReadOnly(readOnly);
  }

  @Override
  public Criteria setFetchSize(final int fetchSize) {
    return criteria.setFetchSize(fetchSize);
  }

  @Override
  public Criteria setTimeout(final int timeout) {
    return criteria.setTimeout(timeout);
  }

  @Override
  public Criteria setCacheable(final boolean cacheable) {
    return criteria.setCacheable(cacheable);
  }

  @Override
  public Criteria setCacheRegion(final String cacheRegion) {
    return criteria.setCacheRegion(cacheRegion);
  }

  @Override
  public Criteria setComment(final String comment) {
    return criteria.setComment(comment);
  }

  @Override
  public Criteria setFlushMode(final FlushMode flushMode) {
    return criteria.setFlushMode(flushMode);
  }

  @Override
  public Criteria setCacheMode(final CacheMode cacheMode) {
    return criteria.setCacheMode(cacheMode);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> list() throws HibernateException {
    return criteria.list();
  }

  @Override
  public ScrollableResults scroll() throws HibernateException {
    return criteria.scroll();
  }

  @Override
  public ScrollableResults scroll(final ScrollMode scrollMode) throws HibernateException {
    return criteria.scroll();
  }

  @Override
  @SuppressWarnings("unchecked")
  public T uniqueResult() throws HibernateException {
    return (T) criteria.uniqueResult();
  }
}
