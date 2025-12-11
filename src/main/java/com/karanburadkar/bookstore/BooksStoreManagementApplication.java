package com.karanburadkar.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ConfigurationPropertiesScan
public class BooksStoreManagementApplication {

    public static void main(String[] args) {
		SpringApplication.run(BooksStoreManagementApplication.class, args);
		System.out.println("\nStart Executing..........");
	}

}
