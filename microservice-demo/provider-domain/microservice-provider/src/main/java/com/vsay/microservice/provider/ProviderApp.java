package com.vsay.microservice.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@Component
public class ProviderApp {

	private final ProviderRepository providerRepository;

	@Autowired
	public ProviderApp(ProviderRepository providerRepository) {
		this.providerRepository = providerRepository;
	}

	@PostConstruct
	public void generateTestData() {
		providerRepository.save(new Provider("Jon", "Smith",
				"jon.smith@gmail.com", "Newark", "NJ"));
		providerRepository.save(new Provider("Jane", "Smith",
				"jane.smith@gmail.com", "Manhattan", "NY"));

	}

	public static void main(String[] args) {
		SpringApplication.run(ProviderApp.class, args);
	}

}
