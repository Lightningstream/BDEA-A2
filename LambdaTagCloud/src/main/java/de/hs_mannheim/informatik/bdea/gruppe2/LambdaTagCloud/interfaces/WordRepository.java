package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.interfaces;
import org.springframework.data.mongodb.repository.MongoRepository;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.WordEntry;


public interface WordRepository extends MongoRepository<WordEntry,String>{
  
}
