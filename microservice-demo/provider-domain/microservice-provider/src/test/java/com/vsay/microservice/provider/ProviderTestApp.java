package com.vsay.microservice.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
public class ProviderTestApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProviderTestApp.class);
		app.setAdditionalProfiles("test");
		app.run(args);
	}

}
