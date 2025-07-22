package com.alkmanistik.photosnap.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private Storage storage;

    @Data
    public static class Storage {
        private String uploadDir;
        private String allowedExtensions;
    }
}