/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Controller;

import com.NQH.MachineLearning.DTO.Request.ApiResponse;
import com.NQH.MachineLearning.DTO.Response.ModelResponse;
import com.NQH.MachineLearning.service.ModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nqhkt
 */
@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelController {

    ModelService modelService;

    @GetMapping
    public ApiResponse<List<ModelResponse>> getAllModel() {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getAllModel())
                .build();
    }

    @GetMapping("/{modelId}")
    public ApiResponse<ModelResponse> getModel(@PathVariable String modelId) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.getModel(modelId))
                .build();
    }

    @PostMapping("/{modelId}")
    public ApiResponse<ModelResponse> retrainModel(@PathVariable String modelId, @RequestParam List<String> dataId)
            throws JsonProcessingException {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.retrainModel(modelId, dataId))
                .build();
    }

    @DeleteMapping("/{modelId}")
    public ApiResponse<String> deleteModel(@PathVariable String modelId)
            throws JsonProcessingException {
        return ApiResponse.<String>builder()
                .result(modelService.deleteModel(modelId))
                .build();
    }

    @PostMapping("/predict/{modelId}")
    public ApiResponse<String> predict(@PathVariable String modelId, @RequestParam String dataId)
            throws JsonProcessingException {
        return ApiResponse.<String>builder()
                .result(modelService.predict(modelId, dataId))
                .build();
    }
}
