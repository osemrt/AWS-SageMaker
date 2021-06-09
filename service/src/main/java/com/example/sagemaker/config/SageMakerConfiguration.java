package com.example.sagemaker.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sagemaker.AmazonSageMaker;
import com.amazonaws.services.sagemaker.AmazonSageMakerClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SageMakerConfiguration {

    private final AmazonConfig amazonConfig;

    @Bean
    public AmazonSageMaker amazonSageMakerClient() {
        return AmazonSageMakerClientBuilder.standard()
                .withCredentials(awsCredentialsProvider(awsCredentials()))
                .withClientConfiguration(clientConfiguration())
                .withRegion(amazonConfig.getRegion())
                .build();
    }

    private AWSCredentialsProvider awsCredentialsProvider(AWSCredentials awsCredentials) {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    private AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(amazonConfig.getAccessKey(), amazonConfig.getSecretKey());
    }

    private ClientConfiguration clientConfiguration() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(100);
        clientConfiguration.setMaxErrorRetry(5);
        return clientConfiguration;
    }


}
