package com.example.sagemaker.service;

import com.example.sagemaker.model.PredictionRequest;
import com.example.sagemaker.model.PredictionResponse;
import com.example.sagemaker.model.TrainingRequest;
import com.example.sagemaker.model.TrainingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final TrainingService trainingService;

    public PredictionResponse predict(PredictionRequest predictionRequest) {
        return null;
    }

    public TrainingResponse train(TrainingRequest trainingRequest) {
        return trainingService.trainJob(trainingRequest);
    }
}
