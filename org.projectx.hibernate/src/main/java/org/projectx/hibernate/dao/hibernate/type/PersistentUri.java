package org.projectx.hibernate.dao.hibernate.type;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

/**
 * Hibernate UserType for persisting URIs
 * 
 * URIs are persisted as Strings
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class PersistentUri implements UserType {

  /**
   * SQL type is a string
   */
  private static final int[] SQL_TYPES = new int[] { StandardBasicTypes.STRING.sqlType() };

  public static final PersistentUri INSTANCE = new PersistentUri();

  @Override
  public Object assemble(final Serializable cached, final Object value) throws HibernateException {
    return cached;
  }

  @Override
  public Object deepCopy(final Object value) throws HibernateException {
    return value;
  }

  @Override
  public Serializable disassemble(final Object value) throws HibernateException {
    return (Serializable) value;
  }

  @Override
  public boolean equals(final Object x, final Object y) throws HibernateException {
    return x.equals(y);
  }

  @Override
  public int hashCode(final Object obj) throws HibernateException {
    return obj.hashCode();
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  /**
   * Convert persisted string to a URI
   */
  @Override
  public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner)
      throws HibernateException, SQLException {
    final String name = (String) StandardBasicTypes.STRING.nullSafeGet(rs, names, null, owner);
    if (name == null) {
      return null;
    }
    try {
      return new URI(name);
    } catch (final URISyntaxException e) {
      // Translate any URI syntax exception to a hibernate exception
      throw new HibernateException("URI has invalid syntax - could not read entity", e);
    }
  }

  /**
   * Convert a URI to a string
   */
  @Override
  public void nullSafeSet(final PreparedStatement st, final Object value, final int index)
      throws HibernateException, SQLException {
    StandardBasicTypes.STRING.nullSafeSet(st, ((URI) value).toString(), index);
  }

  @Override
  public Object replace(final Object original, final Object target, final Object owner)
      throws HibernateException {
    return original;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Class returnedClass() {
    return URI.class;
  }

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

}
