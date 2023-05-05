package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("word")
public class WordEntry {
  
  @Id
  public String name;
  public int documentFrequency;

  public WordEntry(String name, int documentFrequency) {
    this.name = name;
    this.documentFrequency = documentFrequency;
  } 

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDocumentFrequency() {
    return documentFrequency;
  }

  public void setDocumentFrequency(int documentFrequency) {
    this.documentFrequency = documentFrequency;
  }
}
