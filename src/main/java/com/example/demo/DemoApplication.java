package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DemoApplication {

    public static final String APPLICATIN_LOCATINS = "spring.config.location=" +
            "classpath:application.yml," + "classpath:aws.yml";

    public static void main(String[] args) {

        new SpringApplicationBuilder(DemoApplication.class).properties(APPLICATIN_LOCATINS).run(args);

        // 메모리 사용량 출력
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
    }

}
