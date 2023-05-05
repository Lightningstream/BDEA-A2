package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class LambdaTagCloudApplication {

	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir", System.getenv("HADOOP_HOME"));

		SpringApplication.run(LambdaTagCloudApplication.class, args);
	}

}
