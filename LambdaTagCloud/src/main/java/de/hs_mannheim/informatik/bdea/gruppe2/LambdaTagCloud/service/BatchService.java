package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.StructType;
import org.springframework.stereotype.Service;

//import com.mongodb.spark.MongoSpark;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.interfaces.WordRepository;
import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.WordEntry;
import scala.Function1;
import scala.collection.IterableOnce;
import scala.collection.JavaConverters;

import static org.apache.spark.sql.functions.*;

@Service
public class BatchService {

    private final WordRepository wordRepository;
    private final FileService fileService;
    private final TagCloudService tagCloudService;

    public BatchService(WordRepository wordRepository, FileService fileService, TagCloudService tagCloudService) {
        this.wordRepository = wordRepository;
        this.fileService = fileService;
        this.tagCloudService = tagCloudService;
    }

    public void startBatch() throws IOException {
		SparkSession sparkSession = SparkSession.builder()
            .appName("WordEntryBatchJob")
            .master("local[*]")
            .getOrCreate();
        File[] files = fileService.getAllDocuments();
        int i = 0;
        
        /*for (File file : files) {
            Dataset<String> lines = sparkSession.read().textFile(file.getAbsolutePath()).as(Encoders.STRING());

            // Split the lines into words and check if they are present in the document
            Set<String> uniqueWords = new HashSet<>();
            lines.foreach(line -> {
                String[] words = line.split(" ");
                for (String word : words) {
                    uniqueWords.add(word);
                }
            });

            // Update the corresponding Word entities in the database
            for (String word : uniqueWords) {
                Optional<WordEntry> existingWord = wordRepository.findById(word);
                if (existingWord.isPresent()) {
                    WordEntry updatedWord = existingWord.get();
                    if (i > 1) System.out.println(wordRepository.findById(word));
                    updatedWord.setDocumentFrequency(updatedWord.getDocumentFrequency() + 1);
                    wordRepository.save(updatedWord);
                    if (i > 1) System.out.println(wordRepository.findById(word) + "\n");
                } else {
                    WordEntry newWord = new WordEntry(word, 1);
                    wordRepository.save(newWord);
                }
            }
            i++;
        }*/

        DataFrameReader reader = sparkSession.read()
            .format("com.mongodb.spark.sql.DefaultSource")
            .option("uri", "mongodb://localhost:27017/WordRepository.WordEntity");
        Dataset<Row> df = reader.load();

        // Extract the distinct words from the dataset
        Dataset<Row> wordsDataset = df
                .flatMap((Function1<Row, IterableOnce<Row>>)row -> {
                    String[] words = row.getString(0).split(" ");
                    List<Row> list = new ArrayList<>();
                    for (String word : words) {
                        list.add(RowFactory.create(word));
                    }
                    return JavaConverters.asScalaIteratorConverter(list.iterator()).asScala();
                }, RowEncoder.apply(StructType.fromDDL("word string")))
                .distinct();

        // Iterate over each word to count the number of files that contain that word
        Dataset<Row> countsDataset = wordsDataset
                .map((Function1<Row,Row>)row -> {
                    String word = row.getString(0);
                    int count = (int)df.filter(col("value").contains(word)).count();
                    return RowFactory.create(word, count);
                }, RowEncoder.apply(StructType.fromDDL("word string, count int")));

        countsDataset.show();
        countsDataset.write()
            .format("mongo")
            .option("uri", "mongodb://localhost:27017/myDatabase.myCollection") // TODO
            .option("replaceDocument", "true")
            .mode("append")
            .save();
        
        for (File file : files) {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            tagCloudService.createTagCloud(file.getName(), new String(data));
        }

        Dataset<Row> wordCounts = df.groupBy("word")
            .agg(sum("count").as("count"), first("documentFrequency").as("documentFrequency"))
            .withColumn("tfidf", col("count").divide(col("documentFrequency")))
            .orderBy(desc("tfidf"));

        wordCounts.show();
        wordCounts.write().text(FileService.FILE_PATH + "global.txt"); // TODO
        
        sparkSession.close();
    }

}