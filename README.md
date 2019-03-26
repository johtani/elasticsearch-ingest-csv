# Elasticsearch csv Ingest Processor

This processor can parse CSV data and stores it as individual fields.
This filter can also parse data with any separator, not just commas.

## Installation

| ES version | Command |
| ---------- | ------- |
| 6.6.2 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.6.2.0/ingest-csv-6.6.2.0.zip` |
| 6.6.1 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.6.1.0/ingest-csv-6.6.1.0.zip` |
| 6.6.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.6.0.0/ingest-csv-6.6.0.0.zip` |
| 6.5.4 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.5.4.0/ingest-csv-6.5.4.0.zip` |
| 6.5.3 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.5.3.0/ingest-csv-6.5.3.0.zip` |
| 6.5.2 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.5.2.0/ingest-csv-6.5.2.0.zip` |
| 6.5.1 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.5.1.0/ingest-csv-6.5.1.0.zip` |
| 6.5.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.5.0.0/ingest-csv-6.5.0.0.zip` |
| 6.4.3 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.4.3.0/ingest-csv-6.4.3.0.zip` |
| 6.4.2 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.4.2.0/ingest-csv-6.4.2.0.zip` |
| 6.4.1 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.4.1.0/ingest-csv-6.4.1.0.zip` |
| 6.4.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.4.0.0/ingest-csv-6.4.0.0.zip` |
| 6.3.2 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.3.2.0/ingest-csv-6.3.2.0.zip` |
| 6.3.1 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.3.1.0/ingest-csv-6.3.1.0.zip` |
| 6.3.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.3.0.0/ingest-csv-6.3.0.0.zip` |
| 6.2.4 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.2.4.0/ingest-csv-6.2.4.0.zip` |
| 6.2.4 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.2.4.0/ingest-csv-6.2.4.0.zip` |
| 6.2.3 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.2.3.0/ingest-csv-6.2.3.0.zip` |
| 6.2.2 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.2.2.0/ingest-csv-6.2.2.0.zip` |
| 6.2.1 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.2.1.0/ingest-csv-6.2.1.0.zip` |
| 6.2.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.2.0.0/ingest-csv-6.2.0.0.zip` |
| 6.1.3 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.1.3.0/ingest-csv-6.1.3.0.zip` |
| 6.1.2 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.1.2.0/ingest-csv-6.1.2.0.zip` |
| 6.1.1 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.1.1.0/ingest-csv-6.1.1.0.zip` |
| 6.0.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/6.0.0.0/ingest-csv-6.0.0.0.zip` |
| 5.6.3 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/5.6.3.0/ingest-csv-5.6.3.0.zip` |
| 5.5.0 | `./bin/elasticsearch-plugin install https://oss.sonatype.org/service/local/repositories/releases/content/info/johtani/elasticsearch/plugin/ingest/ingest-csv/5.5.0.0/ingest-csv-5.5.0.0.zip` |

## Usage


```
PUT _ingest/pipeline/csv-pipeline
{
  "description": "A pipeline to do whatever",
  "processors": [
    {
      "csv" : {
        "field" : "my_field",
        "columns" : ["a", "b"]
      }
    }
  ]
}

PUT /my-index/my-type/1?pipeline=csv-pipeline
{
  "my_field" : "a_value,b_value"
}

GET /my-index/my-type/1

then, the doc has 3 fields like this.
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
| columns  | Define a list of column names. | Yes |
| quote_char | Define the character used to quote CSV fields. If this is not specified the default is a double quote ". | No |
| separator | Define the column separator value. If this is not specified, the default is a comma ,. | No |

## Setup

In order to install this plugin, you need to create a zip distribution first by running

```bash
./gradlew clean check
```

This will produce a zip file in `build/distributions`.

After building the zip file, you can install it like this

```bash
bin/plugin install file:///path/to/ingest-csv/build/distribution/ingest-csv-0.0.1-SNAPSHOT.zip
```

## Bugs & TODO

* Need more test, like using separator and quote_char 
* and todos...

