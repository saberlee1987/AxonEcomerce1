package com.saber.ecom.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EcomRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomRegistryApplication.class, args);
	}

}
