package com.example.sagemaker.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "amazon")
public class AmazonConfig {
    private String accessKey;
    private String secretKey;
    private String region;
}
