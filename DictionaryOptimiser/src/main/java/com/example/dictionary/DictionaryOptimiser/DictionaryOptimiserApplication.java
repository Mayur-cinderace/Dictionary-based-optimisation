package com.example.dictionary.DictionaryOptimiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DictionaryOptimiserApplication {
	public static void main(String[] args) {
		SpringApplication.run(DictionaryOptimiserApplication.class, args);
	}
}
