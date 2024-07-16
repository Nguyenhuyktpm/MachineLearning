/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Controller;

import com.NQH.MachineLearning.DTO.Request.ApiResponse;
import com.NQH.MachineLearning.DTO.Request.TrainModelRequest;
import com.NQH.MachineLearning.DTO.Request.TrainingCreationRequest;
import com.NQH.MachineLearning.DTO.Response.ModelResponse;
import com.NQH.MachineLearning.DTO.Response.TrainingResponse;
import com.NQH.MachineLearning.service.TrainingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nqhkt
 */
@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrainingController {

    TrainingService trainingService;

    @PostMapping()
    ApiResponse<TrainingResponse> createTraining(@RequestBody TrainingCreationRequest request) {

        return ApiResponse.<TrainingResponse>builder()
                .result(trainingService.creationTraining(request))
                .build();
    }

    @GetMapping()
    ApiResponse<List<TrainingResponse>> getAllTraining() {
        return ApiResponse.<List<TrainingResponse>>builder()
                .result(trainingService.getAllTraining())
                .build();
    }

    @GetMapping("/{trainingId}")
    ApiResponse<TrainingResponse> getTraining(@PathVariable String trainingId) {
        return ApiResponse.<TrainingResponse>builder()
                .result(trainingService.getTraining(trainingId))
                .build();

    }

    @PostMapping("/model/{trainingId}")
    ApiResponse<ModelResponse> trainModel(@PathVariable String trainingId, @RequestBody TrainModelRequest request)
            throws JsonProcessingException {

        return ApiResponse.<ModelResponse>builder()
                .result(trainingService.trainModel(request, trainingId))
                .build();
    }

    @DeleteMapping("/{trainingId}")
    ApiResponse<String> deleteTraining(@PathVariable String trainingId)
             {

        return ApiResponse.<String>builder()
                .result(trainingService.deleteTraining(trainingId))
                .build();
    }
}
