package org.projectx.elasticsearch;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.junit.After;
import org.junit.Before;

public class ElasticsearchTestBase {

  @Resource
  NodeTemplate nodeTemplate;

  @Before
  public void before() {
    if (!nodeTemplate.indexExists()) {

      nodeTemplate.executeGet(new NodeCallback<CreateIndexResponse>() {

        @Override
        public ActionFuture<CreateIndexResponse> execute(final IndicesAdminClient admin) {
          return admin.create(Requests.createIndexRequest(nodeTemplate.getIndexName()));
        }
      });
    }
  }

  @After
  public void after() {
    nodeTemplate.deleteIndex();
  }

  protected void refreshIndex() {
    nodeTemplate.refreshIndex();
  }

  protected void flushIndex() {
    nodeTemplate.flushIndex();
  }

  protected IndexResponse index(final XContentBuilder content) {
    final IndexResponse response = nodeTemplate.executeGet(new ClientCallback<IndexResponse>() {

      @Override
      public ActionFuture<IndexResponse> execute(final Client client) {
        final IndexRequest request = Requests.indexRequest(nodeTemplate.getIndexName())
                                             .source(content).type("log");
        return client.index(request);
      }
    });
    assertNotNull("response is null", response);
    return response;
  }

  protected <Q> List<SearchHit> search(final String field, final Q value, final int maxResults) {
    return nodeTemplate.search(field, value, maxResults);
  }

}
