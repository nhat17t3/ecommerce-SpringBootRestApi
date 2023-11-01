package com.nhat.demoSpringbooRestApi.configs;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("Demo Rest API spring boot")
                        .description("Backend APIs for demo")
                        .version("v1.0.0")
                        .contact(new Contact().name("Hoang Long Nhat"))
                        .license(new License().name("License").url("/")))
                        .externalDocs(new ExternalDocumentation().description("Demo Rest API spring boot Documentation")
                        .url("http://localhost:8080/swagger-ui/index.html"));
    }

}