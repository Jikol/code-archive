package com.presentationlayer;

import com.dataaccesslayer.Database;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.security.Key;

@SpringBootApplication
public class PresentationLayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PresentationLayerApplication.class, args);
        Database.Create("postgresql", "localhost", "5432",
                "problem_reporter", "postgres", "postgres");
    }
}
