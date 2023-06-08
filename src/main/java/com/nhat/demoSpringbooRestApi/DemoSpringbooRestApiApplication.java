package com.nhat.demoSpringbooRestApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.nhat.demoSpringbooRestApi")
@EnableJpaRepositories(basePackages = "com.nhat.demoSpringbooRestApi.repositories")

public class DemoSpringbooRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringbooRestApiApplication.class, args);
	}

}
