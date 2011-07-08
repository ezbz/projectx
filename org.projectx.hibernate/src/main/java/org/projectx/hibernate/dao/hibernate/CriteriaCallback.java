package org.projectx.hibernate.dao.hibernate;

import org.hibernate.Criteria;

/**
 * Callback interface for use with Hibernate's {@link Criteria} API.
 * <p>
 * Used to simplify the criteria preparation flow and separate criteria creation
 * from criteria population assuming the former is created by lower level data
 * access objects while the latter depends more on the actual logic of the
 * criteria formation (since most {@link Criteria} implementations usually
 * require knowledge of the Hibernate session to live).
 * <p>
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */

public interface CriteriaCallback {

  /**
   * Prepare a {@link GenericCriteria}. Implementations of this callback use the
   * provided criteria to filter their query
   * 
   * @param criteria
   */
  void prepare(final Criteria criteria);
}
