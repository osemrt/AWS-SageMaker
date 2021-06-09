package com.example.sagemaker.service;

import com.amazonaws.services.sagemaker.AmazonSageMaker;
import com.amazonaws.services.sagemaker.model.*;
import com.example.sagemaker.config.TrainingConfig;
import com.example.sagemaker.model.TrainingRequest;
import com.example.sagemaker.model.TrainingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {

    private final AmazonSageMaker sageMakerClient;
    private final TrainingConfig trainingConfig;

    public TrainingResponse trainJob(TrainingRequest trainingRequest) {

        CreateTrainingJobResult jobResult = new CreateTrainingJobResult();
        try {

            S3DataSource s3DataSource = new S3DataSource()
                    .withS3Uri(trainingConfig.getS3Uri())
                    .withS3DataType("S3Prefix")
                    .withS3DataDistributionType("FullyReplicated");

            DataSource dataSource = new DataSource()
                    .withS3DataSource(s3DataSource);

            Channel channel = new Channel()
                    .withChannelName(trainingConfig.getChannelName())
                    .withContentType("csv")
                    .withDataSource(dataSource);

            List<Channel> channelList = new ArrayList<>();
            channelList.add(channel);

            ResourceConfig resourceConfig = new ResourceConfig()
                    .withInstanceType(TrainingInstanceType.MlC4Xlarge.toString())
                    .withInstanceCount(1)
                    .withVolumeSizeInGB(1);

            CheckpointConfig checkpointConfig = new CheckpointConfig()
                    .withS3Uri(trainingConfig.getS3Uri());

            OutputDataConfig outputDataConfig = new OutputDataConfig()
                    .withS3OutputPath(trainingConfig.getS3OutputPath());

            StoppingCondition stoppingCondition = new StoppingCondition()
                    .withMaxRuntimeInSeconds(1200);

            AlgorithmSpecification algorithmSpecification = new AlgorithmSpecification()
                    .withTrainingImage(trainingConfig.getTrainingImage())
                    .withTrainingInputMode(TrainingInputMode.File.toString());

            Map<String, String> hyperParameters = new HashMap<>();
//            hyperParameters.put("bucket", trainingConfig.getBucket());
//            hyperParameters.put("training", trainingConfig.getTrainingFile());

            CreateTrainingJobRequest trainingJobRequest = new CreateTrainingJobRequest()
                    .withTrainingJobName(trainingConfig.getTrainingJobName())
                    .withAlgorithmSpecification(algorithmSpecification)
                    .withRoleArn(trainingConfig.getRoleArn())
                    .withResourceConfig(resourceConfig)
                    .withCheckpointConfig(checkpointConfig)
                    .withInputDataConfig(channelList)
                    .withOutputDataConfig(outputDataConfig)
                    .withStoppingCondition(stoppingCondition)
                    .withHyperParameters(hyperParameters);

            jobResult = sageMakerClient.createTrainingJob(trainingJobRequest);
            log.info("The Amazon Resource Name (ARN) of the training job is " + jobResult.getTrainingJobArn());

        } catch (AmazonSageMakerException e) {
            log.error(e.getErrorMessage());
            System.exit(1);
        }
        return TrainingResponse.builder()
                .arn(jobResult.getTrainingJobArn())
                .build();
    }
}
