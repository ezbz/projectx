package org.projectx.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.Client;

/**
 * A callback for an Elasticsearch {@link Client} based operations
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * @param <T>
 *          the {@link ActionResponse} sub-class returned by the client
 *          operation
 */
public interface ClientCallback<T extends ActionResponse> {
  /**
   * Execute an action using the configured {@link Client}
   * 
   * @param client
   *          an instance of {@link Client}
   * @return an {@link ActionFuture} with the appropriate {@link ActionResponse}
   *         subclass
   */
  ActionFuture<T> execute(final Client client);
}
