package org.projectx.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.IndicesAdminClient;

/**
 * A callback for an Elasticsearch <code>Node</code> based operations
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the {@link ActionResponse} sub-class returned by the node operation
 */
public interface NodeCallback<T extends ActionResponse> {
  /**
   * Execute an action using the provided {@link IndicesAdminClient} given on a
   * configured client
   * 
   * @param client
   *          an instance of {@link IndicesAdminClient}
   * @return an {@link ActionFuture} with the appropriate {@link ActionResponse}
   *         subclass
   */
  ActionFuture<T> execute(final IndicesAdminClient client);
}
