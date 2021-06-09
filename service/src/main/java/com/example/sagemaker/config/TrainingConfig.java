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
@ConfigurationProperties(prefix = "amazon.sagemaker.training")
public class TrainingConfig {

    private String s3UriData;
    private String s3Uri;
    private String trainingJobName;
    private String roleArn;
    private String s3OutputPath;
    private String channelName;
    private String trainingImage;
    private String bucket;
    private String trainingFile;
}
