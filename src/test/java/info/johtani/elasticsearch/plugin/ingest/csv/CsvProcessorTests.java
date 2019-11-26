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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class CsvProcessorTests extends ESTestCase {

    private static List<String> defaultColumns;

    @BeforeClass
    public static void defaultSettings() {
        defaultColumns = new ArrayList<>();
        defaultColumns.add("a");
        defaultColumns.add("b");
    }

    public void testSimple() throws Exception {
        Map<String, Object> document = new HashMap<>();
        document.put("source_field", "a_value, b_value");
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), document);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',',4096);
        Map<String, Object> data = processor.execute(ingestDocument).getSourceAndMetadata();

        assertThat(data, hasKey("a"));
        assertThat(data.get("a"), is("a_value"));
        assertThat(data, hasKey("b"));
        assertThat(data.get("b"), is("b_value"));

    }

    public void testMismatchLength() throws Exception {
        Map<String, Object> documentShort = new HashMap<>();
        documentShort.put("source_field", "a_value");
        IngestDocument ingestDocumentShort = RandomDocumentPicks.randomIngestDocument(random(), documentShort);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',',4096);

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> processor.execute(ingestDocumentShort));
        assertThat(e.getMessage(), equalTo("field[source_field] size [1] doesn't match header size [" + defaultColumns.size() + "]."));

        Map<String, Object> documentLong = new HashMap<>();
        documentLong.put("source_field", "a_value,b_value,c_value");
        IngestDocument ingestDocumentLong = RandomDocumentPicks.randomIngestDocument(random(), documentLong);

        e = expectThrows(IllegalArgumentException.class, () -> processor.execute(ingestDocumentLong));
        assertThat(e.getMessage(), equalTo("field[source_field] size [3] doesn't match header size [" + defaultColumns.size() + "]."));
    }

    public void testEmptyField() throws Exception {
        Map<String, Object> document = new HashMap<>();
        document.put("source_field", "");
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), document);

        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',',4096);

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> processor.execute(ingestDocument));
        assertThat(e.getMessage(), equalTo("field[source_field] is empty string."));
    }

    public void testManyTimes() throws Exception {
        CsvProcessor processor = new CsvProcessor(randomAlphaOfLength(10), "source_field", defaultColumns, '\"', ',',4096);
        int times = 50000;

        logger.info("start");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Map<String, Object> document = new HashMap<>();
            document.put("source_field", "a_value, b_value");
            IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), document);


            Map<String, Object> data = processor.execute(ingestDocument).getSourceAndMetadata();

            assertThat(data, hasKey("a"));
            assertThat(data.get("a"), is("a_value"));
            assertThat(data, hasKey("b"));
            assertThat(data.get("b"), is("b_value"));
        }
        logger.info("end. Loop is " + times + " times. Process Time is " + String.valueOf(System.currentTimeMillis() - startTime) + " ms");
    }


}

