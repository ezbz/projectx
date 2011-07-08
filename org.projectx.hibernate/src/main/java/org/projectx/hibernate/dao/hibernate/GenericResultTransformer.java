package org.projectx.hibernate.dao.hibernate;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

/**
 * A generic extension for Hibernate's {@link ResultTransformer} interface.
 * <p>
 * Use of this adapter provides a type-safe use of the {@link ResultTransformer}
 * and alleviates the need for suppressing warning in consuming code.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the underlying type handled by the {@link ResultTransformer}
 */
public interface GenericResultTransformer<T> extends ResultTransformer {

  @Override
  public T transformTuple(Object[] tuple, String[] aliases);

  @Override
  public List<T> transformList(@SuppressWarnings("rawtypes") List list);

}
