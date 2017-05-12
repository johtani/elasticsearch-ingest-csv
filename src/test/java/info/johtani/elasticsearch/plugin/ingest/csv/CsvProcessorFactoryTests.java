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


import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.test.ESTestCase;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class CsvProcessorFactoryTests extends ESTestCase {

    private static final CsvProcessor.Factory factory = new CsvProcessor.Factory();
    private static List<String> defaultColumns;

    @BeforeClass
    private static void defaultSettings() {
        defaultColumns = new ArrayList<>();
        defaultColumns.add("a");
        defaultColumns.add("b");
    }

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("field", "_field");
        config.put("columns", defaultColumns);
        //config.put("");
        return config;
    }

    public void testDefaultSettings() throws Exception {
        Map<String, Object> config = getDefaultConfig();
        String processorTag = randomAlphaOfLength(10);
        CsvProcessor processor = factory.create(null, processorTag, config);

        assertThat(processor.getTag(), equalTo(processorTag));
        assertThat(processor.getField(), equalTo("_field"));
        assertThat(processor.getColumns(), equalTo(defaultColumns));
        assertThat(processor.getCsvSettings().getFormat().getQuote(), equalTo('"'));
        assertThat(processor.getCsvSettings().getFormat().getDelimiter(), equalTo(','));
    }

    public void testQuoteChar() throws Exception {
        String processorTag = randomAlphaOfLength(10);

        final Map<String, Object> config = getDefaultConfig();
        config.put("quote_char", "aa");

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class,
            () -> factory.create(null, processorTag, config));
        assertThat(e.getMessage(), equalTo("quote_char must be a character, like \" or \'"));

        final Map<String, Object> configEmpty = getDefaultConfig();
        configEmpty.put("quote_char", "");

        e = expectThrows(IllegalArgumentException.class,
            () -> factory.create(null, processorTag, configEmpty));
        assertThat(e.getMessage(), equalTo("quote_char must be a character, like \" or \'"));
    }

    public void testSeparator() throws Exception {
        String separator = "aa";

        Map<String, Object> config = getDefaultConfig();
        config.put("separator", separator);
        String processorTag = randomAlphaOfLength(10);

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class,
            () -> factory.create(null, processorTag, config));
        assertThat(e.getMessage(), equalTo("separator must be a character, like , or TAB"));

        final Map<String, Object> configEmpty = getDefaultConfig();
        configEmpty.put("separator", "");

        e = expectThrows(IllegalArgumentException.class,
            () -> factory.create(null, processorTag, configEmpty));
        assertThat(e.getMessage(), equalTo("separator must be a character, like , or TAB"));
    }

    public void testColumns() throws Exception {
        Map<String, Object> config = getDefaultConfig();
        config.put("columns", new ArrayList<>());
        String processorTag = randomAlphaOfLength(10);

        IllegalArgumentException e = expectThrows(IllegalArgumentException.class,
            () -> factory.create(null, processorTag, config));
        assertThat(e.getMessage(), equalTo("columns is missing"));

        final Map<String, Object> configEmpty = getDefaultConfig();
        configEmpty.put("columns", null);

        ElasticsearchParseException epe = expectThrows(ElasticsearchParseException.class,
            () -> factory.create(null, processorTag, configEmpty));
        assertThat(e.getMessage(), equalTo("columns is missing"));
    }

}
