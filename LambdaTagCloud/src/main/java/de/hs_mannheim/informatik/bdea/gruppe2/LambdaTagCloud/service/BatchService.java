package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.interfaces.WordRepository;
import scala.Tuple2;

@Service
public class BatchService {

	private final WordRepository wordRepository;
	private final FileService fileService;
	private final TagCloudService tagCloudService;
	private final SparkSession sparkSession;

	public BatchService(WordRepository wordRepository, FileService fileService, TagCloudService tagCloudService) {
		this.wordRepository = wordRepository;
		this.fileService = fileService;
		this.tagCloudService = tagCloudService;
		this.sparkSession = SparkSession.builder()
				.master("local")
				.appName("MongoSparkConnectorIntro")
				.config("spark.mongodb.read.connection.uri",
						"mongodb://root:root@mongo/tagclouds.WordEntry?authSource=admin")
				.config("spark.mongodb.write.connection.uri",
						"mongodb://root:root@mongo/tagclouds.WordEntry?authSource=admin")
				.getOrCreate();
	}

	public List<Tuple2<String, Double>> runTfIdfJob(String filename) throws IOException {
		Dataset<String> data = sparkSession.read().option("wholetext", "true")
				.textFile(FileService.FILE_PATH + filename + ".txt");
		final long amountOfFiles = this.fileService.getAllDocuments().length;
		Dataset<Row> termFrequencyDataFrame = data
				.flatMap((FlatMapFunction<String, String>) row -> {
					// Split all words and allow only words with more than 3 characters
					System.out.println(row);
					String[] words = row.split("[^\\p{L}]+");
					List<String> wordList = new ArrayList<>();
					for (String word : words) {
						if (word.length() >= 4)
							wordList.add(word);
					}
					return wordList.iterator();
				}, Encoders.STRING()).groupBy(functions.col("value")).count()
				.orderBy(functions.desc("count")).limit(300).withColumnRenamed("value", "name")
				.withColumnRenamed("count", "termFrequency").toDF();

		termFrequencyDataFrame.show();
		Dataset<Row> documentFrequencies = sparkSession.read().format("mongodb").load();
		// Creates default documentFrequency and name column if database is empty.
		// This allows joining the data frames later.
		if (!Arrays.asList(documentFrequencies.schema().fieldNames()).contains("name")) {
			documentFrequencies = documentFrequencies.withColumn("name", functions.lit("default"))
					.withColumn("documentFrequency", functions.lit(1));
		}
		documentFrequencies.show();
		// Join term frequencies and document frequencies and add document frequency 1
		// to
		// all empty rows.
		termFrequencyDataFrame = termFrequencyDataFrame.join(documentFrequencies, "name", "left_outer");
		termFrequencyDataFrame = termFrequencyDataFrame.withColumn(
				"documentFrequency",
				functions.coalesce(termFrequencyDataFrame.col("documentFrequency"), functions.lit(1)));

		// Calculate tf_idf with formula tfidf = (Log(amountOfDocuments /
		// documentFrequency) + 1) * termFrequency
		// This is called tf idf smooth
		Dataset<Row> termFrequencyDataFrameTfIdf = termFrequencyDataFrame.withColumn("tfIdf",
				functions.log(functions.lit(amountOfFiles)
						.divide(termFrequencyDataFrame.col("documentFrequency")))
						.plus(1)
						.multiply(termFrequencyDataFrame.col("termFrequency")));

		termFrequencyDataFrameTfIdf.show();

		// Map result to tuple
		return termFrequencyDataFrameTfIdf.select("name", "tfIdf")
				.as(Encoders.tuple(Encoders.STRING(), Encoders.DOUBLE()))
				.collectAsList();
	}

	public void runBatch() throws IOException {

		// Read all files as strings (wholetext is important here!)
		Dataset<String> data = sparkSession.read().option("wholetext", "true").textFile("files/*.txt");
		// Create DataSet which contains all document frequencies for each word grouped
		// by word.
		Dataset<Row> documentFrequencies = data
				.flatMap((FlatMapFunction<String, String>) row -> {
					// Split all words and fill into set to contain every occurrence of a word only
					// once per file
					Set<String> set = new HashSet<>();
					String[] words = row.split("[^\\p{L}]+");
					for (String word : words) {
						set.add(word);
					}
					return set.iterator();
				}, Encoders.STRING()).groupBy(functions.col("value")).count().toDF();

		documentFrequencies = documentFrequencies.withColumnRenamed("value", "name").withColumnRenamed("count",
				"documentFrequency");
		documentFrequencies.show();

		// Write document frequency to mongodb
		documentFrequencies.write().format("mongodb").mode("overwrite").save();

		// Rebuild all tag clouds.
		this.calculateAllDocuments();
		// Build global tfidf file.
		this.calculateGlobalTfIdf();
	}

	public void calculateAllDocuments() throws IOException {
		for (File file : this.fileService.getAllDocuments()) {
			String fileName = file.getName().replace(".txt", "");
			List<Tuple2<String, Double>> tfIdfList = this.runTfIdfJob(fileName);
			this.tagCloudService.createTagCloud(fileName, tfIdfList);
		}
	}

	public void calculateGlobalTfIdf() throws IOException {
		List<Tuple2<String, Double>> tfIdfList = this.runTfIdfJob("*");
		this.tagCloudService.createTagCloud("global", tfIdfList);
	}
}