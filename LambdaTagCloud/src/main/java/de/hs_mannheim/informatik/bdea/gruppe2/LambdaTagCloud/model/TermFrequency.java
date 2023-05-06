package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("TermFrequency")
public class TermFrequency {
  @Id
  public String name;
  public int termFrequency;

  public TermFrequency(String name, int frequency) {
    this.name = name;
    this.termFrequency = frequency;
  }

  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getTermFrequency() {
    return this.termFrequency;
  }

  public void setTermFrequency(int frequency) {
    this.termFrequency = frequency;
  } 
}
