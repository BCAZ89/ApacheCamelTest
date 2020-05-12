package com.keuss.poc.apachecamel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootAndApacheCamelApplication {

    // see https://camel.apache.org/components/3.0.x/spring-boot.html for other configuration
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAndApacheCamelApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            // For example we cas see 'dataSource' bean used with apache camel ...
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}
