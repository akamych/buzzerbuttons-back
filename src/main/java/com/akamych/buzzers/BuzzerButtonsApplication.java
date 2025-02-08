package com.akamych.buzzers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuzzerButtonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuzzerButtonsApplication.class, args);
	}

}
