package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("word")
public class WordEntry {
  
  @Id
  public String name;
  public int documentFrequency;
}
