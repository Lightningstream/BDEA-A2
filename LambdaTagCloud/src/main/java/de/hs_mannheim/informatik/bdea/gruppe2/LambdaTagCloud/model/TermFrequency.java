package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("TermFrequency")
public class TermFrequency {
  @Id
  public String name;
  public int frequency;

  public TermFrequency(String name, int frequency) {
    this.name = name;
    this.frequency = frequency;
  }

  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getFrequency() {
    return this.frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  } 
}
