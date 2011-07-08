package org.projectx.hibernate.dao.hibernate;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * A Hibernate {@link EmptyInterceptor} implementation for capturing all
 * Hibernate data operations.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class HibernateEntityInterceptor extends EmptyInterceptor {

   private static final long serialVersionUID = 1L;

   private static final Log LOG = LogFactory
         .getLog(HibernateEntityInterceptor.class);

   @Override
   public boolean onSave(final Object entity, final Serializable id,
         final Object[] state, final String[] propertyNames, final Type[] types) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("HibernateEntityInterceptor.onSave(): "
               + createEntityString(entity, id));
      }
      return true;
   }

   @Override
   public boolean onFlushDirty(final Object entity, final Serializable id,
         final Object[] currentState, final Object[] previousState,
         final String[] propertyNames, final Type[] types) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("HibernateEntityInterceptor.onFlushDirty(): "
               + createEntityString(entity, id));
      }
      return true;
   }

   @Override
   public void onDelete(final Object entity, final Serializable id,
         final Object[] state, final String[] propertyNames, final Type[] types) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("HibernateEntityInterceptor.onDelete(): "
               + createEntityString(entity, id));
      }
   }

   @Override
   public boolean onLoad(final Object entity, final Serializable id,
         final Object[] state, final String[] propertyNames, final Type[] types) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("HibernateEntityInterceptor.onLoad(): "
               + createEntityString(entity, id));
      }
      return true;
   }

   private String createEntityString(final Object entity, final Serializable id) {
      return new StringBuilder().append("EntityClass: ").append(
            entity.getClass().getName()).append(", id=").append(id).toString();
   }
}
