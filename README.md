# Elasticsearch csv Ingest Processor

This processor can parse CSV data and stores it as individual fields.
This filter can also parse data with any separator, not just commas.

## Fork contribution

In this version the processor handles multiple patterns: it will consider the first one matching.
There is also an optional field, `key_field`, that allows to store the key value of the matching pattern.

## Installation

| ES version | Command |
| ---------- | ------- |
| 5.3.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/5.3.0/ingest-csv-5.3.0.zip` |

If you need to work on a different version, you can try to change the setting in `gradle.properties` before compiling. Use at your own risk!

## Usage


```
PUT _ingest/pipeline/csv-pipeline
{
  "description": "A pipeline to do whatever",
  "processors": [
    {
      "csv" : {
        "field" : "my_field",
        "columns" : {
          "csv_pattern1" : [ "a","b","c" ],
          "csv_pattern2" : [ "a","b" ],
          "csv_pattern3" : [ "a","b","c","d" ]
        }
      }
    }
  ]
}

PUT /my-index/my-type/1?pipeline=csv-pipeline
{
  "my_field" : "a_value,b_value"
}

GET /my-index/my-type/1
{
  "my_field" : "a_value,b_value",
  "a" : "a_value",
  "b" : "b_value"
}
```

## Configuration

| Parameter | Use | Required |
| --- | --- | --- |
| field   | Field name of where to read the content from | Yes |
| columns  | Define multiple maps, each one with a list of column names. | Yes |
| quote_char | Define the character used to quote CSV fields. If this is not specified the default is a double quote ". | No |
| separator | Define the column separator value. If this is not specified, the default is a comma ,. | No |
| key_field | Define the field where to save the key value of the matching pattern in `columns` (for example `csv_pattern1`,`csv_pattern2` or `csv_pattern3` in the example). If not specified, this field will not be saved. | No |

## Setup

In order to install this plugin, you need to create a zip distribution first by running

```bash
gradle clean check
```

This will produce a zip file in `build/distributions`.

After building the zip file, you can install it like this

```bash
bin/plugin install file:///path/to/ingest-csv/build/distribution/ingest-csv-0.0.1-SNAPSHOT.zip
```

## Bugs & TODO

* Need more test, like using separator and quote_char 
* and todos...

