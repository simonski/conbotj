package conbot.utils;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.CsvOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.LoadJobConfiguration;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.google.cloud.bigquery.TableId;

// Sample to load CSV data from Cloud Storage into a new BigQuery table
public class BQLoader {

//  public static void runLoadCsvFromGcs() throws Exception {
//    // TODO(developer): Replace these variables before running the sample.
//    String datasetName = "MY_DATASET_NAME";
//    String tableName = "MY_TABLE_NAME";
//    String sourceUri = "gs://cloud-samples-data/bigquery/us-states/us-states.csv";
//    Schema schema =
//        Schema.of(
//            Field.of("name", StandardSQLTypeName.STRING),
//            Field.of("post_abbr", StandardSQLTypeName.STRING));
//    loadCsvFromGcs(datasetName, tableName, sourceUri, schema);
//  }
//
	public static void loadCsvFromGcs(String datasetName, String tableName, String sourceUri, Schema schema) {
		try {
			// Initialize client that will be used to send requests. This client only needs
			// to be created
			// once, and can be reused for multiple requests.
			BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

			// Skip header row in the file.
			CsvOptions csvOptions = CsvOptions.newBuilder().setSkipLeadingRows(1).build();

			TableId tableId = TableId.of(datasetName, tableName);
			LoadJobConfiguration loadConfig = LoadJobConfiguration.newBuilder(tableId, sourceUri, csvOptions)
					.setSchema(schema).build();

			// Load data from a GCS CSV file into the table
			Job job = bigquery.create(JobInfo.of(loadConfig));
			// Blocks until this load table job completes its execution, either failing or
			// succeeding.
			job = job.waitFor();
			if (job.isDone()) {
				System.out.println("CSV from GCS successfully added during load append job");
			} else {
				System.out.println(
						"BigQuery was unable to load into the table due to an error:" + job.getStatus().getError());
			}
		} catch (BigQueryException | InterruptedException e) {
			System.out.println("Column not added during load append \n" + e.toString());
		}
	}

}