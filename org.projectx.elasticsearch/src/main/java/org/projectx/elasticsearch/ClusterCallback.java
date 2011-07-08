package org.projectx.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.ClusterAdminClient;

/**
 * A callback for an Elasticsearch <code>Cluster</code> based operations
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the {@link ActionResponse} sub-class returned by the client
 *          operation
 */
public interface ClusterCallback<T extends ActionResponse> {
  /**
   * Execute an action using the configured {@link ClusterAdminClient}
   * 
   * @param client
   *          an instance of {@link ClusterAdminClient}
   * @return an {@link ActionFuture} with the appropriate {@link ActionResponse}
   *         subclass
   */
  ActionFuture<T> execute(ClusterAdminClient admin);

}
