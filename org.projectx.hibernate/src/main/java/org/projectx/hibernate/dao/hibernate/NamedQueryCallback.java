
 
 
package org.projectx.hibernate.dao.hibernate;


import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Callback interface for Hibernate <code>Query</code> populaion. This interface
 * and the methods it is used in, is designed to make queries execution simpler.
 * <p>
 * Typically used in conjunction with {@link Session#getNamedQuery(String)}
 * method.
 * <p>
 */
public interface NamedQueryCallback<T> {

    /**
     * Prepare the query before execution (set parameters...)
     *
     * @param query a <code>Query</code> object that is to be populated by the
     *            callback implementation.
     */
    public void prepareQuery(Query query);

    /**
     * Returns the query name , must be valid named query define in he hbm
     * files.
     *
     * @return A String value
     */
    public String getName();
}
