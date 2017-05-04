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
import java.util.Iterator;

import static org.elasticsearch.ingest.ConfigurationUtils.readMap;
import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;
import static org.elasticsearch.ingest.ConfigurationUtils.readIntProperty;

public class CsvProcessor extends AbstractProcessor {

    public static final String TYPE = "csv";

    private final String field;
    private final String keyField;
    private final Map<String,List<String>> columns;
    private final CsvParserSettings csvSettings;

    public CsvProcessor(String tag, String field, Map<String, List<String>> columns,
            char quoteChar, char separator, String keyField, int maxChars)
            throws IOException {
        super(tag);
        this.field = field;
        this.keyField = keyField;
        this.columns = columns;
        csvSettings = new CsvParserSettings();
        csvSettings.getFormat().setQuote(quoteChar);
        csvSettings.getFormat().setDelimiter(separator);
        csvSettings.setMaxCharsPerColumn(maxChars);
    }

    @Override
    public void execute(IngestDocument ingestDocument) throws Exception {
        String content = ingestDocument.getFieldValue(field, String.class);

        if (!Strings.hasLength(content)) {
            throw new IllegalArgumentException("field[" + this.field + "] is empty string.");
        }
        else {

            CsvParser parser = new CsvParser(this.csvSettings);

            for( Iterator< Map.Entry< String, List<String> > > it = columns.entrySet().iterator(); it.hasNext(); ) {

                Map.Entry<String,List<String>> entry = it.next();
                String[] values = parser.parseLine(content);

                if (values.length != entry.getValue().size()) {
                    if (it.hasNext()) {
                        continue;
                    }
                    // TODO should be error?
                    else {
                        throw new IllegalArgumentException("field[" + this.field + "] size ["
                            + values.length + "] doesn't match header size [" + entry.getValue().size() + "].");
                    }
                }
                else {
                    for ( int i=0; i<entry.getValue().size(); i++) {
                        ingestDocument.setFieldValue(entry.getValue().get(i), values[i]);
                    }
                    if (!keyField.equals("")) {
                        ingestDocument.setFieldValue(keyField, entry.getKey());
                    }
                    // Exit on the first matching pattern
                    break;
                }
            }
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }

    String getField() {
        return field;
    }

    Map<String, List<String>> getColumns() {
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
            Map<String, List<String>> columns = readMap( TYPE, tag, config, "columns");
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
            String keyField = readStringProperty(TYPE, tag, config, "key_field", "");
            int maxChars = readIntProperty(TYPE, tag, config, "max_chars", 32766);

            return new CsvProcessor(tag, field, columns, quoteChar.charAt(0), separator.charAt(0), keyField, maxChars);
        }
    }
}
