package org.projectx.index;

import java.util.List;

/**
 * An interface for a generalized index based search engine
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public interface IndexSearchEngine<T> {
  /**
   * A generalized method for returning a list of hits based on a provided query
   * *
   * <p>
   * TODO: (erez) this method is here only as a place holder for future
   * functionality of searching the index which should be replaced once the
   * behavior is defined
   * 
   * @param field
   *          the field to search by
   * @param query
   *          the query string
   * @param maxResults
   *          the maximum number of results
   * @return a list of hits
   */
  <Q> List<T> search(final String field, final Q query, int maxResults);

  /**
   * A generalized method for returning a list of hits based on a provided query
   * <p>
   * TODO: (erez) this method is here only as a place holder for future
   * functionality of searching the index which should be replaced once the
   * behavior is defined
   * 
   * @param query
   *          the query string
   * @param maxResults
   *          the maximum number of results
   * @return a list of hits
   */
  List<T> search(final String queryString, int maxResults);
}
