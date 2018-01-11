/*
 * Copyright [2017] [Jun Ohtani]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package info.johtani.elasticsearch.plugin.ingest.csv;

import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.RandomDocumentPicks;
import org.elasticsearch.test.ESTestCase;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class CsvProcessorTests extends ESTestCase {

    private static Map<String, List<String>> defaultColumns = new HashMap<String, List<String>>();

    @BeforeClass
    public static void defaultSettings() {

        defaultColumns.put( "csv1", new ArrayList<>( Arrays.asList("a","b") ) );
//        defaultColumns.put( "csv2", new ArrayList<>( Arrays.asList("c","d","e") ) );
    }

    public void testSimple() throws Exception {
        Map<String, Object> document = new HashMap<>();
        document.put("source_field", "a_value, b_value");
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), document);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',', "", 32766);
        processor.execute(ingestDocument);
        Map<String, Object> data = ingestDocument.getSourceAndMetadata();

        assertThat(data, hasKey("a"));
        assertThat(data.get("a"), is("a_value"));
        assertThat(data, hasKey("b"));
        assertThat(data.get("b"), is("b_value"));

    }

    public void testMismatchLength() throws Exception {
        Map<String, Object> documentShort = new HashMap<>();
        documentShort.put("source_field", "a_value");
        IngestDocument ingestDocumentShort = RandomDocumentPicks.randomIngestDocument(random(), documentShort);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',', "", 32766);

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> processor.execute(ingestDocumentShort));
        assertThat(e.getMessage(), equalTo("field[source_field] size [1] doesn't match header size [2]."));

        Map<String, Object> documentLong = new HashMap<>();
        documentLong.put("source_field", "a_value,b_value,c_value");
        IngestDocument ingestDocumentLong = RandomDocumentPicks.randomIngestDocument(random(), documentLong);

        e = expectThrows(IllegalArgumentException.class, () -> processor.execute(ingestDocumentLong));
        assertThat(e.getMessage(), equalTo("field[source_field] size [3] doesn't match header size [2]."));
    }

    public void testEmptyField() throws Exception {
        Map<String, Object> document = new HashMap<>();
        document.put("source_field", "");
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), document);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',', "", 32766);

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> processor.execute(ingestDocument));
        assertThat(e.getMessage(), equalTo("field[source_field] is empty string."));
    }

    public void testKeyField() throws Exception {
        Map<String, Object> document = new HashMap<>();
        document.put("source_field", "a_value, b_value");
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), document);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',', "key", 32766);
        processor.execute(ingestDocument);
        Map<String, Object> data = ingestDocument.getSourceAndMetadata();

        assertThat(data, hasKey("key"));
        assertThat(data.get("key"), is("csv1"));
    }

}
