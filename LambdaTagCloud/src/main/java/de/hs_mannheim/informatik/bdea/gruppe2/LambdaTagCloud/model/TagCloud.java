package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tagCloud")
public class TagCloud {

  @Id
  public String name;
  public String base64Image;

  public TagCloud(String name, String base64Image) {
    this.name = name;
    this.base64Image = base64Image;
  }
}