package com.example.sagemaker.controller;

import com.example.sagemaker.model.PredictionRequest;
import com.example.sagemaker.model.PredictionResponse;
import com.example.sagemaker.model.TrainingRequest;
import com.example.sagemaker.model.TrainingResponse;
import com.example.sagemaker.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("model")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping("predict")
    public PredictionResponse predict(PredictionRequest predictionRequest){
        return modelService.predict(predictionRequest);
    }

    @PostMapping("train")
    public TrainingResponse train(TrainingRequest trainingRequest){
        return modelService.train(trainingRequest);
    }

}
