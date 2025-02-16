package com.nnk.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application {

	/**
	 * The main method which serves as the entry point for the application.
	 *
	 * @param args command-line arguments passed during application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
