package org.projectx.elasticsearch;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.admin.indices.gateway.snapshot.GatewaySnapshotResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.status.IndicesStatusResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.xcontent.FieldQueryBuilder;
import org.elasticsearch.index.query.xcontent.QueryBuilders;
import org.elasticsearch.index.query.xcontent.QueryStringQueryBuilder;
import org.elasticsearch.index.query.xcontent.XContentQueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.projectx.index.IndexSearchEngine;
import org.springframework.util.Assert;

/**
 * A template based implementation for use with an Elasticsearch {@link Node}
 * and {@link Client}. Provides an abstraction around common operations needed
 * from the Elasticsearch infrastructure to the consuming classes.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class NodeTemplate implements IndexSearchEngine<SearchHit>, NodeOperations {

  private final Node node;
  private final String indexName;

  public NodeTemplate(final Node node, final String indexName) {
    Assert.notNull(node, "node cannot be null");
    Assert.notNull(indexName, "indexName cannot be null");
    this.node = node;
    this.indexName = indexName;
  }

  @Override
  public boolean indexExists() {
    executeGet(new ClusterCallback<ClusterHealthResponse>() {

      @Override
      public ActionFuture<ClusterHealthResponse> execute(final ClusterAdminClient admin) {
        return admin.health(Requests.clusterHealthRequest().waitForStatus(
            ClusterHealthStatus.YELLOW));
      }
    });

    final IndicesStatusResponse response = executeGet(new NodeCallback<IndicesStatusResponse>() {

      @Override
      public ActionFuture<IndicesStatusResponse> execute(final IndicesAdminClient admin) {
        return admin.status(Requests.indicesStatusRequest());
      }
    });

    return response.getIndices().get(indexName) != null;
  }

  @Override
  public void deleteIndex() {
    executeGet(new NodeCallback<DeleteIndexResponse>() {

      @Override
      public ActionFuture<DeleteIndexResponse> execute(final IndicesAdminClient admin) {
        return admin.delete(Requests.deleteIndexRequest(indexName));
      }
    });
  }

  @Override
  public void refreshIndex() {
    executeGet(new NodeCallback<RefreshResponse>() {

      @Override
      public ActionFuture<RefreshResponse> execute(final IndicesAdminClient admin) {
        return admin.refresh(Requests.refreshRequest(indexName).waitForOperations(true));
      }
    });
  }

  @Override
  public void closeIndex() {
    executeGet(new NodeCallback<CloseIndexResponse>() {
      @Override
      public ActionFuture<CloseIndexResponse> execute(final IndicesAdminClient admin) {
        return admin.close(Requests.closeIndexRequest(indexName));
      }
    });
  }

  @Override
  public void flushIndex() {
    executeGet(new NodeCallback<FlushResponse>() {
      @Override
      public ActionFuture<FlushResponse> execute(final IndicesAdminClient admin) {
        return admin.flush(Requests.flushRequest(indexName));
      }
    });
  }

  @Override
  public void snapshotIndex() {
    executeGet(new NodeCallback<GatewaySnapshotResponse>() {
      @Override
      public ActionFuture<GatewaySnapshotResponse> execute(final IndicesAdminClient admin) {
        return admin.gatewaySnapshot(Requests.gatewaySnapshotRequest(indexName));
      }
    });
  }

  @Override
  public <Q> List<SearchHit> search(final String field, final Q query, final int maxResults) {
    final FieldQueryBuilder fieldQuery = QueryBuilders.fieldQuery(field, query);
    return searchInternal(fieldQuery, maxResults);
  }

  @Override
  public List<SearchHit> search(final String queryString, final int maxResults) {
    final QueryStringQueryBuilder query = QueryBuilders.queryString(queryString);
    return searchInternal(query, maxResults);
  }

  private List<SearchHit> searchInternal(final XContentQueryBuilder query, final int maxResults) {
    final SearchResponse response = executeGet(new ClientCallback<SearchResponse>() {

      @Override
      public ActionFuture<SearchResponse> execute(final Client client) {
        final SearchRequest request = Requests.searchRequest().searchType(
            SearchType.DFS_QUERY_AND_FETCH);
        final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(query);

        sourceBuilder.size(maxResults);
        request.source(sourceBuilder);
        return client.search(request);
      }
    });
    return Arrays.asList(response.getHits().getHits());

  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends ActionResponse> T executeGet(final NodeCallback<T> callback) {
    final Client client = node.client();
    final IndicesAdminClient indicesAdmin = client.admin().indices();
    final ActionFuture<?> action = callback.execute(indicesAdmin);
    final T response = (T) action.actionGet();
    client.close();
    return response;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends ActionResponse> T executeGet(final ClusterCallback<T> callback) {
    final Client client = node.client();
    final ClusterAdminClient clusterAdmin = client.admin().cluster();
    final ActionFuture<?> action = callback.execute(clusterAdmin);
    final T response = (T) action.actionGet();
    client.close();
    return response;
  }

  @Override
  public <T extends ActionResponse> T executeGet(final ClientCallback<T> callback) {
    final Client client = node.client();
    final ActionFuture<T> action = callback.execute(client);
    final T response = action.actionGet();
    client.close();
    return response;
  }

  @Override
  public String getIndexName() {
    return indexName;
  }
}
