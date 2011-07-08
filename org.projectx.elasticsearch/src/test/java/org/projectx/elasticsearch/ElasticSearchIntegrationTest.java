package org.projectx.elasticsearch;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple lucene index.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/projectx/elasticsearch/applicationContext-elasticsearch.xml" })
public class ElasticSearchIntegrationTest extends ElasticsearchTestBase {

  @Test
  public void testIndex() throws Exception {

    final Collection<File> files = Collections.singletonList(new File(
        "src/test/resources/othello1-1.txt"));

    int totalDocs = 0;
    for (final File file : files) {
      @SuppressWarnings("unchecked")
      final List<String> lines = FileUtils.readLines(file, "UTF-8");

      int lineNumber = 1;
      for (final String line : lines) {
        indexLine(file, line, lineNumber);
        lineNumber++;
        totalDocs++;
      }
    }

    refreshIndex();

    final List<SearchHit> hits = search("rownum", 1, 100);

    assertEquals("incorrect number of results", 1, hits.size());
  }

  private void indexLine(final File file, final String line, final int rownum) throws Exception {
    final XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
    jsonBuilder.startObject();
    jsonBuilder.field("rownum", rownum);
    jsonBuilder.field("path", file.getAbsolutePath());
    jsonBuilder.field("modified", file.lastModified());
    jsonBuilder.field("contents", line);
    jsonBuilder.endObject();

    index(jsonBuilder);
  }

}
