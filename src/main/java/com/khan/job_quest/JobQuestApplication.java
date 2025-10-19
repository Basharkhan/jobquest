package com.khan.job_quest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class JobQuestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobQuestApplication.class, args);
        System.out.println("App is running...");
	}

}
