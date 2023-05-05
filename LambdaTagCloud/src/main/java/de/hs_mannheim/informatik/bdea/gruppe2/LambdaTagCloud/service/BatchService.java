package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.interfaces.WordRepository;
import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.TermFrequency;
import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.WordEntry;

@Service
public class BatchService {

    private final WordRepository wordRepository;
    private final FileService fileService;
    private final TagCloudService tagCloudService;
    private final SparkSession sparkSession;

    public double getIdf(int numberOfDocuments, int documentFrequency) {
        // Add + 1 because Log(1) is 0. This is called inverse document frequency
        // smooth.
        return Math.log((numberOfDocuments) / (documentFrequency)) + 1;
    }

    public int getTfIdf(int numberOfDocuments, int documentFrequency, int termFrequency) {
        return (int) Math.round(termFrequency * getIdf(numberOfDocuments, documentFrequency));
    }

    public BatchService(WordRepository wordRepository, FileService fileService, TagCloudService tagCloudService) {
        this.wordRepository = wordRepository;
        this.fileService = fileService;
        this.tagCloudService = tagCloudService;
        this.sparkSession = SparkSession.builder()
        .master("local")
        .appName("MongoSparkConnectorIntro")
        .config("spark.mongodb.read.connection.uri", "mongodb://root:root@mongo/tagclouds.WordEntry?authSource=admin")
        .config("spark.mongodb.write.connection.uri", "mongodb://root:root@mongo/tagclouds.WordEntry?authSource=admin")
        .getOrCreate();
    }

    public List<WordFrequency> getTfIdfList(String text) {

        final int numberOfCalculatedDocuments = this.fileService.getAllDocuments().length;
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(4);
        List<String> texts = new ArrayList<>();
        texts.add(text);
        // Calculate the term frequencies
        final List<WordFrequency> termFrequencies = frequencyAnalyzer.load(texts);
        final List<TermFrequency> hashMap = new ArrayList<>();
        final List<WordEntry> hashMap2 = new ArrayList<>();
        for (WordFrequency term : termFrequencies) {
            hashMap.add(new TermFrequency(term.getWord(), term.getFrequency()));
        }
        hashMap2.add(new WordEntry("joel", 2));
        final List<WordFrequency> tfIdfList = new ArrayList<>();
        // Generate the tf idf for all terms in that document.

        var tftIdList = sparkSession.createDataFrame(hashMap, TermFrequency.class);
        var tftIdList2 = sparkSession.createDataFrame(hashMap2, WordEntry.class);
        System.out.println("Hallo");
        tftIdList.show();
        Dataset<Row> documentFrequencies = sparkSession.read().format("mongodb").load();
        // documentFrequencies.foreach(df -> {
        //     System.out.println(df);
        // });

        var joinedDataset = tftIdList.join(tftIdList2, "name", "left_outer");
        Dataset<Row> joinedDataWithDefault = joinedDataset.withColumn("documentFrequency", functions.coalesce(joinedDataset.col("documentFrequency"), functions.lit(1)));
        joinedDataWithDefault.show();

        var test = joinedDataWithDefault.withColumn("tf-idf", functions.log(functions.lit(numberOfCalculatedDocuments).divide(joinedDataWithDefault.col("documentFrequency"))).plus(1).multiply(joinedDataWithDefault.col("frequency")));
        test.show();
        // tftIdList.map(wordTuple -> {
        //     return new Tuple2<T1,T2>(wordTuple._1, this.getTfIdf(numberOfCalculatedDocuments, ))
        // }, null)

        // tftIdList.foreach(word -> {
        //     var test = documentFrequencies.where("name =" + word._1);
        //     System.out.println(word._1 + ":" + word._2 + "; " + test);
        // });
        // joinedDataset.show();
        return tfIdfList;
        // for (WordFrequency termFrequency : termFrequencies) {
        // final Optional<WordEntry> currentWordEntry =
        // wordRepository.findById(termFrequency.getWord());
        // final int tfIdf;
        // if (currentWordEntry.isEmpty()) {
        // tfIdf = getTfIdf(numberOfCalculatedDocuments, 1,
        // termFrequency.getFrequency());
        // } else {
        // tfIdf = getTfIdf(numberOfCalculatedDocuments,
        // currentWordEntry.get().documentFrequency,
        // numberOfCalculatedDocuments);
        // }
        // tfIdfList.add(new WordFrequency(termFrequency.getWord(), tfIdf));
        // }

    }

    public void countWords() {
        // var words = sparkSession.read()
        // .textFile("files/Goethe--Faust.txt")
        // .flatMap((FlatMapFunction<String, String>) lines -> {
        // return Arrays.asList(lines.split("\\W+")).iterator();
        // }, Encoders.STRING())
        // .map((MapFunction<String, Tuple2<String, Integer>>) word -> new
        // Tuple2<>(word, 1),
        // // Encoders.tuple(Encoders.STRING(), Encoders.INT())).reduce(null);

        // words.foreach(map -> {
        // System.out.println(map._1 + ":" + map._2);
        // });
    }

    // public void startBatch() throws IOException {
    // File[] files = fileService.getAllDocuments();
    // int i = 0;

    // Dataset<String> lines = sparkSession.read().tex
    // // /*for (File file : files) {
    // // Dataset<String> lines =
    // sparkSession.read().textFile(file.getAbsolutePath()).as(Encoders.STRING());

    // // // Split the lines into words and check if they are present in the
    // document
    // // Set<String> uniqueWords = new HashSet<>();
    // // lines.foreach(line -> {
    // // String[] words = line.split(" ");
    // // for (String word : words) {
    // // uniqueWords.add(word);
    // // }
    // // });

    // // // Update the corresponding Word entities in the database
    // // for (String word : uniqueWords) {
    // // Optional<WordEntry> existingWord = wordRepository.findById(word);
    // // if (existingWord.isPresent()) {
    // // WordEntry updatedWord = existingWord.get();
    // // if (i > 1) System.out.println(wordRepository.findById(word));
    // // updatedWord.setDocumentFrequency(updatedWord.getDocumentFrequency() + 1);
    // // wordRepository.save(updatedWord);
    // // if (i > 1) System.out.println(wordRepository.findById(word) + "\n");
    // // } else {
    // // WordEntry newWord = new WordEntry(word, 1);
    // // wordRepository.save(newWord);
    // // }
    // // }
    // // i++;
    // // }*/

    // // Dataset<Row> df = sparkSession.read().format("mongodb").option("database",
    // "tagclouds").option("collection", "WordEntry").load();

    // // // Extract the distinct words from the dataset
    // // Dataset<Row> wordsDataset = df
    // // .flatMap((Function1<Row, IterableOnce<Row>>)row -> {
    // // String[] words = row.getString(0).split(" ");
    // // List<Row> list = new ArrayList<>();
    // // for (String word : words) {
    // // list.add(RowFactory.create(word));
    // // }
    // // return JavaConverters.asScalaIteratorConverter(list.iterator()).asScala();
    // // }, RowEncoder.apply(StructType.fromDDL("word string")))
    // // .distinct();

    // // // Iterate over each word to count the number of files that contain that
    // word
    // // Dataset<Row> countsDataset = wordsDataset
    // // .map((Function1<Row,Row>)row -> {
    // // String word = row.getString(0);
    // // int count = (int)df.filter(col("value").contains(word)).count();
    // // return RowFactory.create(word, count);
    // // }, RowEncoder.apply(StructType.fromDDL("word string, count int")));

    // // countsDataset.show();
    // // countsDataset.write()
    // // .format("mongo")
    // // .option("database", "tagclouds")
    // // .option("collection", "WordEntry")
    // // .option("replaceDocument", "true")
    // // .mode("append")
    // // .save();

    // // for (File file : files) {
    // // FileInputStream fis = new FileInputStream(file);
    // // byte[] data = new byte[(int) file.length()];
    // // fis.read(data);
    // // fis.close();
    // // tagCloudService.createTagCloud(file.getName(), new String(data));
    // // }

    // // // Dataset<Row> wordCounts = df.groupBy("word")
    // // // .agg(sum("count").as("count"),
    // first("documentFrequency").as("documentFrequency"))
    // // // .withColumn("tfidf", col("count").divide(col("documentFrequency")))
    // // // .orderBy(desc("tfidf"));

    // // // wordCounts.show();
    // // // wordCounts.write().text(FileService.FILE_PATH + "global.txt"); // TODO

    // sparkSession.close();
    // }

}