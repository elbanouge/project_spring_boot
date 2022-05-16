package com.project.request_credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nu.pattern.OpenCV;

@SpringBootApplication
public class CreditProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditProjectApplication.class, args);
		OpenCV.loadShared();
		System.out.println("******* Credit Project Application Started ********");
	}

}
