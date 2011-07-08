package org.projectx.hibernate.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

/**
 * A type safe adapter for the Hibernate {@link Query} API. All implemented
 * methods are delegated to the underlying {@link Query} instance.
 * <p>
 * Use of this adapter provides a type-safe use of the {@link Query} and
 * alleviates the need for suppressing warning in consuming code.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the underlying type handled by the {@link Query} instance
 */
public class GenericQuery<T> implements Query {

  private final Query query;

  public GenericQuery(final Query query) {
    Assert.notNull(query, "query cannot be null");
    this.query = query;
  }

  @Override
  public int executeUpdate() throws HibernateException {
    return query.executeUpdate();
  }

  @Override
  public String[] getNamedParameters() throws HibernateException {
    return query.getNamedParameters();
  }

  @Override
  public String getQueryString() {
    return query.getQueryString();
  }

  @Override
  public String[] getReturnAliases() throws HibernateException {
    return query.getReturnAliases();
  }

  @Override
  public Type[] getReturnTypes() throws HibernateException {
    return query.getReturnTypes();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterator<T> iterate() throws HibernateException {
    return query.iterate();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> list() throws HibernateException {
    return query.list();
  }

  @Override
  public ScrollableResults scroll() throws HibernateException {
    return query.scroll();
  }

  @Override
  public ScrollableResults scroll(final ScrollMode scrollMode) throws HibernateException {
    return query.scroll(scrollMode);
  }

  @Override
  public Query setBigDecimal(final int position, final BigDecimal number) {
    return query.setBigDecimal(position, number);
  }

  @Override
  public Query setBigDecimal(final String name, final BigDecimal number) {
    return query.setBigDecimal(name, number);
  }

  @Override
  public Query setBigInteger(final int position, final BigInteger number) {
    return query.setBigInteger(position, number);
  }

  @Override
  public Query setBigInteger(final String name, final BigInteger number) {
    return query.setBigInteger(name, number);
  }

  @Override
  public Query setBinary(final int position, final byte[] val) {
    return query.setBinary(position, val);
  }

  @Override
  public Query setBinary(final String name, final byte[] val) {
    return query.setBinary(name, val);
  }

  @Override
  public Query setBoolean(final int position, final boolean val) {
    return query.setBoolean(position, val);
  }

  @Override
  public Query setBoolean(final String name, final boolean val) {
    return query.setBoolean(name, val);
  }

  @Override
  public Query setByte(final int position, final byte val) {
    return query.setByte(position, val);
  }

  @Override
  public Query setByte(final String name, final byte val) {
    return query.setByte(name, val);
  }

  @Override
  public Query setCacheable(final boolean cacheable) {
    return query.setCacheable(cacheable);
  }

  @Override
  public Query setCacheMode(final CacheMode cacheMode) {
    return query.setCacheMode(cacheMode);
  }

  @Override
  public Query setCacheRegion(final String cacheRegion) {
    return query.setCacheRegion(cacheRegion);
  }

  @Override
  public Query setCalendar(final int position, final Calendar calendar) {
    return query.setCalendar(position, calendar);
  }

  @Override
  public Query setCalendar(final String name, final Calendar calendar) {
    return query.setCalendar(name, calendar);
  }

  @Override
  public Query setCalendarDate(final int position, final Calendar calendar) {
    return query.setCalendarDate(position, calendar);
  }

  @Override
  public Query setCalendarDate(final String name, final Calendar calendar) {
    return query.setCalendarDate(name, calendar);
  }

  @Override
  public Query setCharacter(final int position, final char val) {
    return query.setCharacter(position, val);
  }

  @Override
  public Query setCharacter(final String name, final char val) {
    return query.setCharacter(name, val);
  }

  @Override
  public Query setComment(final String comment) {
    return query.setComment(comment);
  }

  @Override
  public Query setDate(final int position, final Date date) {
    return query.setDate(position, date);
  }

  @Override
  public Query setDate(final String name, final Date date) {
    return query.setDate(name, date);
  }

  @Override
  public Query setDouble(final int position, final double val) {
    return query.setDouble(position, val);
  }

  @Override
  public Query setDouble(final String name, final double val) {
    return query.setDouble(name, val);
  }

  @Override
  public Query setEntity(final int position, final Object val) {
    return query.setEntity(position, val);
  }

  @Override
  public Query setEntity(final String name, final Object val) {
    return query.setEntity(name, val);
  }

  @Override
  public Query setFetchSize(final int fetchSize) {
    return query.setFetchSize(fetchSize);
  }

  @Override
  public Query setFirstResult(final int firstResult) {
    return query.setFirstResult(firstResult);
  }

  @Override
  public Query setFloat(final int position, final float val) {
    return query.setFloat(position, val);
  }

  @Override
  public Query setFloat(final String name, final float val) {
    return query.setFloat(name, val);
  }

  @Override
  public Query setFlushMode(final FlushMode flushMode) {
    return query.setFlushMode(flushMode);
  }

  @Override
  public Query setInteger(final int position, final int val) {
    return query.setInteger(position, val);
  }

  @Override
  public Query setInteger(final String name, final int val) {
    return query.setInteger(name, val);
  }

  @Override
  public Query setLocale(final int position, final Locale locale) {
    return query.setLocale(position, locale);
  }

  @Override
  public Query setLocale(final String name, final Locale locale) {
    return query.setLocale(name, locale);
  }

  @Override
  public Query setLockMode(final String alias, final LockMode lockMode) {
    return query.setLockMode(alias, lockMode);
  }

  @Override
  public Query setLong(final int position, final long val) {
    return query.setLong(position, val);
  }

  @Override
  public Query setLong(final String name, final long val) {
    return query.setLong(name, val);
  }

  @Override
  public Query setMaxResults(final int maxResults) {
    return query.setMaxResults(maxResults);
  }

  @Override
  public Query setParameter(final int position, final Object val, final Type type) {
    return query.setParameter(position, val, type);
  }

  @Override
  public Query setParameter(final int position, final Object val) throws HibernateException {
    return query.setParameter(position, val);
  }

  @Override
  public Query setParameter(final String name, final Object val, final Type type) {
    return query.setParameter(name, val, type);
  }

  @Override
  public Query setParameter(final String name, final Object val) throws HibernateException {
    return query.setParameter(name, val);
  }

  @Override
  public Query setParameterList(final String name,
      @SuppressWarnings("rawtypes") final Collection vals, final Type type)
      throws HibernateException {
    return query.setParameterList(name, vals, type);
  }

  @Override
  public Query setParameterList(final String name,
      @SuppressWarnings("rawtypes") final Collection vals) throws HibernateException {
    return query.setParameterList(name, vals);
  }

  @Override
  public Query setParameterList(final String name, final Object[] vals, final Type type)
      throws HibernateException {
    return query.setParameterList(name, vals, type);
  }

  @Override
  public Query setParameterList(final String name, final Object[] vals) throws HibernateException {
    return query.setParameterList(name, vals);
  }

  @Override
  public Query setParameters(final Object[] values, final Type[] types) throws HibernateException {
    return query.setParameters(values, types);
  }

  @Override
  public Query setProperties(@SuppressWarnings("rawtypes") final Map bean)
      throws HibernateException {
    return query.setProperties(bean);
  }

  @Override
  public Query setProperties(final Object bean) throws HibernateException {
    return query.setProperties(bean);
  }

  @Override
  public Query setReadOnly(final boolean readOnly) {
    return query.setReadOnly(readOnly);
  }

  @Override
  public Query setSerializable(final int position, final Serializable val) {
    return query.setSerializable(position, val);
  }

  @Override
  public Query setSerializable(final String name, final Serializable val) {
    return query.setSerializable(name, val);
  }

  @Override
  public Query setShort(final int position, final short val) {
    return query.setShort(position, val);
  }

  @Override
  public Query setShort(final String name, final short val) {
    return query.setShort(name, val);
  }

  @Override
  public Query setString(final int position, final String val) {
    return query.setString(position, val);
  }

  @Override
  public Query setString(final String name, final String val) {
    return query.setString(name, val);
  }

  @Override
  public Query setText(final int position, final String val) {
    return query.setText(position, val);
  }

  @Override
  public Query setText(final String name, final String val) {
    return query.setText(name, val);
  }

  @Override
  public Query setTime(final int position, final Date date) {
    return query.setTime(position, date);
  }

  @Override
  public Query setTime(final String name, final Date date) {
    return query.setTime(name, date);
  }

  @Override
  public Query setTimeout(final int timeout) {
    return query.setTimeout(timeout);
  }

  @Override
  public Query setTimestamp(final int position, final Date date) {
    return query.setTimestamp(position, date);
  }

  @Override
  public Query setTimestamp(final String name, final Date date) {
    return query.setTimestamp(name, date);
  }

  @Override
  public Object uniqueResult() throws HibernateException {
    return query.uniqueResult();
  }

  @Override
  public boolean isReadOnly() {
    return query.isReadOnly();
  }

  @Override
  public Query setLockOptions(final LockOptions lockOptions) {
    return query.setLockOptions(lockOptions);
  }

  @Override
  public Query setResultTransformer(final ResultTransformer transformer) {
    return query.setResultTransformer(transformer);
  }
}
