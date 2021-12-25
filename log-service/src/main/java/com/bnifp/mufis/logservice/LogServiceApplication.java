package com.bnifp.mufis.logservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication()
public class LogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogServiceApplication.class, args);
	}

}
