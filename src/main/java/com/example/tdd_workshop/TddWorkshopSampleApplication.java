package com.example.tdd_workshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "TDD Workshop Sample Application",
                version = "1.0",
                description = "TDD Workshopで使用するサンプルアプリケーション"
        )
)
@SpringBootApplication
public class TddWorkshopSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TddWorkshopSampleApplication.class, args);
    }

}
