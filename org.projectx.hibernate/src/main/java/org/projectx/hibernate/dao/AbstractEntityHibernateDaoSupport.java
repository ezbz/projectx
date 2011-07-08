package org.projectx.hibernate.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.projectx.hibernate.dao.PropertyNames.EntityProperties;
import org.projectx.hibernate.dao.hibernate.CriteriaCallback;
import org.projectx.hibernate.dao.hibernate.GenericCriteria;
import org.projectx.hibernate.dao.hibernate.NamedQueryCallback;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

/**
 * Base implementation for entity data access objects that support
 * {@link Persistable} entities. Provides common operations on the supported
 * entity for convenience and consistency across the data access layer.
 * <p>
 * This class extends Springs {@link HibernateDaoSupport} class which provides
 * basic functionality for dealing with the underlying {@link HibernateTemplate}
 * such functionality is delegated to the base class.
 * <p>
 * Entities managed by extensions of this class should follow a convention where
 * the Hibernate entity name is the same as the corresponding class name. When
 * this is convention is not followed then the {@link #getEntityName()} method
 * must be overriden to provide the actual entity name.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the type of the supported entity (should always be a concrete type
 *          of a Hibernate managed entity)
 */
public abstract class AbstractEntityHibernateDaoSupport<T extends Persistable> extends
    HibernateDaoSupport implements PersistableDao<T> {

  /**
   * The supported entity's name, cached for performance
   */
  private final String supportedEntityName;

  /**
   * Default constructor
   * 
   * @param hibernateTemplate
   *          an instance of a configured {@link HibernateTemplate}, provided to
   *          the {@link AbstractEntityHibernateDaoSupport}.
   */
  public AbstractEntityHibernateDaoSupport(final HibernateTemplate hibernateTemplate) {
    Assert.notNull(hibernateTemplate, "hibernateTemplate cannot be null");
    setHibernateTemplate(hibernateTemplate);
    supportedEntityName = ClassUtils.getShortClassName(getEntityClass());
  }

  /**
   * Find all entities of the supported type
   * 
   * @return a list of persistent supported entities or an empty {@link List} if
   *         none exists
   */
  public List<T> findAll() {
    return getHibernateTemplate().loadAll(getEntityClass());
  }

  /**
   * Find an entity by its primary key
   * 
   * @param primaryKey
   *          primary key for the fetched entity
   * @return a persistent supported entity, or <code>null</code> if entity is
   *         not found.
   */
  @SuppressWarnings("unchecked")
  protected T findByPrimaryKey(final Serializable primaryKey) {
    return (T) getHibernateTemplate().get(getEntityName(), primaryKey);
  }

  /**
   * Find an entities by a {@link Collection} of primary keys.
   * <p>
   * <b>Important</b>: This method uses
   * {@link Restrictions#in(String, Collection)} on the provided primary keys.
   * It is up to the caller to verify compliance with restrictions on the
   * allowed number of literals in an IN clause (i.e., ensure the
   * <code>primaryKeys.size()</code> is &lt; 1000 or an exception will be
   * thrown).
   * <p>
   * <b>Note</b>: This method doesn't guarantee that the order of the returned
   * entities is the same order as the one denoted by the provided
   * {@link Collection} of primary keys. For an order alternative see
   * {@link #findByPrimaryKeysOrdered(List)}
   * 
   * @param primaryKeys
   *          primary key for the fetched entities
   * @return a {@link List} of entities or an empty list if none were found
   */
  protected List<T> findByPrimaryKeys(final Collection<? extends Serializable> primaryKeys) {
    return findEntities(new CriteriaCallback() {

      @Override
      public void prepare(final Criteria criteria) {
        criteria.add(Restrictions.in(getEntityPrimaryKeyPropertyName(), primaryKeys));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
      }
    });
  }

  /**
   * Find an entities by a {@link Collection} of primary keys ensuring that the
   * returned entities are in the same order as the one in the primary keys
   * {@link Collection}
   * <p>
   * 
   * @param primaryKeys
   *          primary keys for the entities that are fetched.
   * @return a list of entities that were specified by their Primary keys. If
   *         none found and empty list is returned.
   */
  protected List<T> findByPrimaryKeysOrdered(final List<? extends Serializable> primaryKeys) {
    if (primaryKeys.isEmpty()) {
      return Collections.emptyList();
    }

    final List<T> entities = findByPrimaryKeys(primaryKeys);
    return DaoUtils.orderEntitiesByPrimaryKeys(entities, primaryKeys);
  }

  /**
   * Finds an entity that matches a criteria populated by the provided
   * {@link CriteriaCallback}.
   * <p>
   * <b>Note</b>: It is up to the criteria implementation to ensure that a
   * unique result is matched or a {@link HibernateException} will be thrown
   * from the underlying framework. If this requirement cannot be met consider
   * using {@link #findEntities(CriteriaCallback)} instead.
   * 
   * @param criteriaCallback
   *          an implementation of the {@link CriteriaCallback} which populates
   *          the desired criteria for this query
   * @return An entity matching the criteria
   */
  protected T findEntity(final CriteriaCallback criteriaCallback) {
    return getHibernateTemplate().execute(new HibernateCallback<T>() {

      @Override
      public T doInHibernate(final Session session) throws HibernateException, SQLException {
        final GenericCriteria<T> criteria = new GenericCriteria<T>(session, getEntityClass());
        criteriaCallback.prepare(criteria);
        return criteria.uniqueResult();
      }
    });
  }

  /**
   * Finds a {@link Collection} of entities that match a criteria populated by
   * the provided {@link CriteriaCallback}.
   * 
   * @param criteriaCallback
   *          an implementation of the {@link CriteriaCallback} which populates
   *          the desired criteria for this query
   * @return A {@link Collection} of entities matching the criteria
   */
  protected List<T> findEntities(final CriteriaCallback criteriaCallback) {
    return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {

      @Override
      public List<T> doInHibernate(final Session session) throws HibernateException, SQLException {
        final GenericCriteria<T> criteria = new GenericCriteria<T>(session, getEntityClass());
        criteriaCallback.prepare(criteria);
        return criteria.list();
      }
    });
  }

  /**
   * Execute an update query for an update or delete operation using a
   * {@link NamedQueryCallback}, the named query must be and update or delete
   * query.
   * 
   * @param namedQueryCallback
   *          a {@link NamedQueryCallback} implementation which updates or
   *          deletes the supported entity
   * @return the number of affected rows
   */
  protected int executeUpdate(final NamedQueryCallback<T> namedQueryCallback) {
    final Integer affectedTuples = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

      @Override
      public Integer doInHibernate(final Session session) throws HibernateException, SQLException {
        final Query query = session.getNamedQuery(namedQueryCallback.getName());
        namedQueryCallback.prepareQuery(query);
        return query.executeUpdate();
      }
    });
    return affectedTuples;
  }

  /**
   * Updates or creates a supported entity. After execution the entity is
   * returned in its updated persistent state (i.e., the primary key populated).
   * 
   * @param entity
   *          an instance of the entity to update or create
   * @return the entity after persistence
   */
  public T update(final T entity) {
    getHibernateTemplate().saveOrUpdate(getEntityName(), entity);
    return entity;
  }

  /**
   * Deletes a supported entity.
   * <p>
   * <b>Note</b>: Hibernate is not able to delete a transient entity, the
   * primary key must be populated
   */
  protected void delete(final T entity) {
    getHibernateTemplate().delete(entity);
  }

  /**
   * Deletes a supported entity by its primary key
   */
  @SuppressWarnings("unchecked")
  protected void deleteByPK(final Serializable primaryKey) {
    final T entity = (T) getHibernateTemplate().load(getEntityName(), primaryKey);
    delete(entity);
  }

  /**
   * Deletes a {@link Collection} of entities by their primary keys
   * 
   * @param primaryKeys
   *          a {@link Collection} of primary keys
   */
  protected void deleteByPKs(final Collection<? extends Serializable> primaryKeys) {
    for (final Serializable pk : primaryKeys) {
      deleteByPK(pk);
    }
  }

  /**
   * Get the primary key property. Defaults to {@link EntityProperties#ID}, when
   * this convention is not followed subclasses must provided the actual propert
   * name for the primary key of the supported entity
   * 
   * @return the property name of the supported entity
   */
  protected String getEntityPrimaryKeyPropertyName() {
    return EntityProperties.ID;
  }

  /**
   * Get the logical Hibernate name for the supported entity.
   * <p>
   * <b>Note</b>: this method returns the default convention of using the
   * {@link #getEntityClass()} name. When the convention is not followed
   * subclasses must return the actual logical name of the supported entity.
   * 
   * @return the default entity primary key name as designated by
   *         {@link EntityProperties#ID}
   */
  public String getEntityName() {
    return supportedEntityName;
  }

  protected void evictEntity(final Entity entity) {
    if (entity.isPersistent()) {
      getHibernateTemplate().evict(
          getHibernateTemplate().load(getEntityName(), entity.getPrimaryKey()));
    }
  }

  /**
   * Count the number of entities that match a given {@link GenericCriteria
   * criteria}
   * 
   * @param criteria
   *          an implementation of the {@link GenericCriteria} interface
   *          filtering the results for the count
   * @return the row count for the provided criteria
   */
  protected Integer countDistinct(final GenericCriteria<Integer> criteria) {
    return (Integer) criteria.setProjection(
        Projections.countDistinct(getEntityPrimaryKeyPropertyName())).uniqueResult();
  }

  /**
   * Count an entity based on the provided {@link CriteriaCallback}
   * implementation provicountDistinctded. Allows extending DAOs to quickly
   * query an entity by specifying only the filtration fields on the criteria
   * <p>
   * <b>Note</b>: consumers do not have to explicitly call
   * {@link Criteria#setProjection(org.hibernate.criterion.Projection)} within
   * {@link CriteriaCallback#prepare(GenericCriteria)} as this method will do it
   * for them.
   * 
   * @param criteriaCallback
   *          an instance of the criteria callback which specifies the fields to
   *          filter by
   * @return an {@link Integer} value representing the count derived by
   *         executing the associated criteria
   */
  protected Integer count(final CriteriaCallback criteriaCallback) {
    return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

      @Override
      public Integer doInHibernate(final Session session) throws HibernateException, SQLException {
        final Criteria criteria = session.createCriteria(getEntityClass());
        criteriaCallback.prepare(criteria);
        criteria.setProjection(Projections.rowCount());
        return (Integer) criteria.uniqueResult();
      }
    });
  }
}
