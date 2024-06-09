/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Response.ModelResponse;
import com.NQH.MachineLearning.Entity.ModelEntity;
import com.NQH.MachineLearning.Mapper.ModelMapper;
import com.NQH.MachineLearning.repository.ModelRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.stereotype.Service;

/**
 *
 * @author nqhkt
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelService {
    ModelRepository modelRepository;
    ModelMapper modelMapper;
    public List<ModelResponse> getAllModel() {
        return modelRepository.findAll().stream().map(modelMapper :: toModelResponse).toList();
    }

    public ModelResponse getModel(String modelId) {
        ModelEntity model = modelRepository.findById(modelId).orElseThrow(() -> new RuntimeException("Model not found"));
        ModelResponse modelResponse  = modelMapper.toModelResponse(model);
        return modelResponse;
    }
}
