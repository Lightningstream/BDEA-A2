package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.interfaces.WordRepository;
import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.WordEntry;

@Service
public class DocumentAnalyzerService {

  /**
   * This class is not in use. It could be used as speed layer instead of a spark job.
   */

  private final WordRepository wordRepository;
  private final FileService fileService;

  public DocumentAnalyzerService(WordRepository wordRepository, FileService fileService) {
    this.wordRepository = wordRepository;
    this.fileService = fileService;
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
    final List<WordFrequency> tfIdfList = new ArrayList<>();
    // Generate the tf idf for all terms in that document.
    for (WordFrequency termFrequency : termFrequencies) {
      final Optional<WordEntry> currentWordEntry = wordRepository.findById(termFrequency.getWord());
      final int tfIdf; 
      if(currentWordEntry.isEmpty()) {
        tfIdf = getTfIdf(numberOfCalculatedDocuments, 1, termFrequency.getFrequency());
      } else {
        tfIdf = getTfIdf(numberOfCalculatedDocuments, currentWordEntry.get().documentFrequency, numberOfCalculatedDocuments);
      }
      tfIdfList.add(new WordFrequency(termFrequency.getWord(), tfIdf));
    }

    return tfIdfList;
  }

  public double getIdf(int numberOfDocuments, int documentFrequency) {
    // Add + 1 because Log(1) is 0. This is called inverse document frequency smooth.
    return Math.log((numberOfDocuments) / (documentFrequency)) + 1;
  }

  public int getTfIdf(int numberOfDocuments, int documentFrequency, int termFrequency) {
    return (int) Math.round(termFrequency * getIdf(numberOfDocuments, documentFrequency));
  }

}
