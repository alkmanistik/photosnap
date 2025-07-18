package com.alkmanistik.photosnap;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PhotosnapJavaApplication {

    public static void main(String[] args) {

        loadAndValidateEnv();

        SpringApplication.run(PhotosnapJavaApplication.class, args);
    }

    private static void loadAndValidateEnv() {
        Dotenv dotenv;
        try {
            dotenv = Dotenv.load();
        } catch (DotenvException e) {
            throw new IllegalStateException("Файл .env не найден в корне проекта!", e);
        }

        List<String> requiredEnvVars = List.of(
                "DB_USERNAME",
                "DB_PASSWORD",
                "REDIS_PASSWORD",
                "JWT_SECRET"
        );

        List<String> missingOrEmptyVars = new ArrayList<>();
        for (String varName : requiredEnvVars) {
            String value = dotenv.get(varName);
            if (value == null || value.trim().isEmpty()) {
                missingOrEmptyVars.add(varName);
            }
        }

        if (!missingOrEmptyVars.isEmpty()) {
            throw new IllegalStateException(
                    "В файле .env отсутствуют или пусты следующие переменные: " +
                            String.join(", ", missingOrEmptyVars)
            );
        }
    }

}
