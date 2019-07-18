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

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.elasticsearch.common.Strings;
import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.ingest.ConfigurationUtils.readList;
import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;
import static org.elasticsearch.ingest.ConfigurationUtils.readIntProperty;

public class CsvProcessor extends AbstractProcessor {

    public static final String TYPE = "csv";

    private final String field;
    private final List<String> columns;
    private final CsvParserSettings csvSettings;

    public CsvProcessor(String tag, String field, List<String> columns,
                        char quoteChar, char separator, int maxCharsPerColumn) throws IOException {
        super(tag);
        this.field = field;
        this.columns = columns;
        csvSettings = new CsvParserSettings();
        csvSettings.getFormat().setQuote(quoteChar);
        csvSettings.getFormat().setDelimiter(separator);
        csvSettings.setMaxCharsPerColumn(maxCharsPerColumn);
    }

    @Override
    public IngestDocument execute(IngestDocument ingestDocument) throws Exception {
        String content = ingestDocument.getFieldValue(field, String.class);

        if (Strings.hasLength(content)) {
            CsvParser parser = new CsvParser(this.csvSettings);
            String[] values = parser.parseLine(content);
            if (values.length != this.columns.size()) {
                // TODO should be error?
                throw new IllegalArgumentException("field[" + this.field + "] size ["
                    + values.length + "] doesn't match header size [" + columns.size() + "].");
            }

            for (int i = 0; i < columns.size(); i++) {
                ingestDocument.setFieldValue(columns.get(i), values[i]);
            }
        } else {
            // TODO should we have ignoreMissing flag?
            throw new IllegalArgumentException("field[" + this.field + "] is empty string.");
        }
        return ingestDocument;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    String getField() {
        return field;
    }

    List<String> getColumns() {
        return columns;
    }

    CsvParserSettings getCsvSettings() {
        return csvSettings;
    }

    public static final class Factory implements Processor.Factory {

        @Override
        public CsvProcessor create(Map<String, Processor.Factory> factories, String tag, Map<String, Object> config) 
            throws Exception {
            String field = readStringProperty(TYPE, tag, config, "field");
            List<String> columns = readList(TYPE, tag, config, "columns");
            // FIXME should test duplicate name
            if (columns.size() == 0) {
                throw new IllegalArgumentException("columns is missing");
            }
            String quoteChar = readStringProperty(TYPE, tag, config, "quote_char", "\"");
            if (Strings.isEmpty(quoteChar) || quoteChar.length() != 1) {
                throw new IllegalArgumentException("quote_char must be a character, like \" or \'");
            }
            String separator = readStringProperty(TYPE, tag, config, "separator", ",");
            if (Strings.isEmpty(separator) || separator.length() != 1) {
                throw new IllegalArgumentException("separator must be a character, like , or TAB");
            }
            int maxCharsPerColumn = readIntProperty(TYPE, tag, config, "max_chars_per_column", 4096);
            if (maxCharsPerColumn < 1 || maxCharsPerColumn > 64000) {
                throw new IllegalArgumentException("maxCharsPerColumn must be between 1 and 64000 (default 4096)");
            }
            return new CsvProcessor(tag, field, columns, quoteChar.charAt(0), separator.charAt(0),maxCharsPerColumn);
        }
    }
}
