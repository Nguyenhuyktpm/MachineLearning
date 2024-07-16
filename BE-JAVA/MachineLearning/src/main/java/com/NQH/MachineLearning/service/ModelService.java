/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Response.ModelResponse;
import com.NQH.MachineLearning.Entity.DataEntity;
import com.NQH.MachineLearning.Entity.ModelEntity;
import com.NQH.MachineLearning.Entity.TrainingDataEntity;
import com.NQH.MachineLearning.Entity.TrainingEntity;
import com.NQH.MachineLearning.Exception.AppException;
import com.NQH.MachineLearning.Exception.ErrorCode;
import com.NQH.MachineLearning.Mapper.ModelMapper;
import com.NQH.MachineLearning.repository.DataRepository;
import com.NQH.MachineLearning.repository.ModelRepository;
import com.NQH.MachineLearning.repository.TrainingDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 *
 * @author nqhkt
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelService {
    
    FlaskApiService flaskApiService;
    ModelRepository modelRepository;
    ModelMapper modelMapper;
    DataRepository dataRepository;
    TrainingDataRepository trainingDataRepository;

    public List<ModelResponse> getAllModel() {
        return modelRepository.findByDeletedAtIsNull()
                    .stream()
                    .map(modelMapper::toModelResponse) 
                    .toList();
    }

    public ModelResponse getModel(String modelId) {
        ModelEntity model = modelRepository.findById(modelId).orElseThrow(()
                -> new AppException(ErrorCode.MODEL_NOT_EXISTED));
        ModelResponse modelResponse = modelMapper.toModelResponse(model);
        return modelResponse;
    }

    public ModelResponse updateModel(String modelId, List<String> dataId)
            throws JsonProcessingException {

        ModelEntity model = modelRepository.findById(modelId)
                .orElseThrow(()
                        -> new AppException(ErrorCode.MODEL_NOT_EXISTED));
        TrainingEntity training = model.getTraining();
        String modelLocation = model.getLocation();
        List<DataEntity> datas = dataRepository.findAllById(dataId);

        List<String> dataFileLink = datas.stream().map(DataEntity::getLocation).toList();
        List<String> labelsFeatures = training.getLabels_feature();
        String labelTarget = training.getLabel_target();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> metrics = new HashMap<>();
        
        log.warn(labelsFeatures.toString());
        log.warn(labelTarget);
        
        response = flaskApiService.callFlaskPretrainedModelApi(
                modelLocation,
                dataFileLink,
                labelsFeatures,
                labelTarget);
        response.forEach(
                (String key, Object value)
                -> {
            metrics.put(key, value);
        });
        model.setMetrics(new ObjectMapper().writeValueAsString(metrics));
        modelRepository.save(model);

        List<TrainingDataEntity> trainingData = datas.stream()
                .map(data -> TrainingDataEntity.builder()
                .data(data)
                .training(training)
                .build()).collect(Collectors.toList());
        trainingDataRepository.saveAll(trainingData);

        return modelMapper.toModelResponse(model);
    }

    public String deleteModel(String modelId) {
        ModelEntity model = modelRepository.findById(modelId).orElseThrow(()
                -> new AppException(ErrorCode.MODEL_NOT_EXISTED));
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        model.setDeletedAt(deletedAt);
        
        modelRepository.save(model);
        return "Model deleted !";
    }
}
