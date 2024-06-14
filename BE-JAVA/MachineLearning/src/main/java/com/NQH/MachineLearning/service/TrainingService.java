/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Request.TrainModelRequest;
import com.NQH.MachineLearning.DTO.Request.TrainingCreationRequest;
import com.NQH.MachineLearning.DTO.Response.DataResponse;
import com.NQH.MachineLearning.DTO.Response.TrainingResponse;
import com.NQH.MachineLearning.Entity.DataEntity;
import com.NQH.MachineLearning.Entity.DatasetEntity;
import com.NQH.MachineLearning.Entity.ModelEntity;
import com.NQH.MachineLearning.Entity.TrainingEntity;
import com.NQH.MachineLearning.Entity.TrainingDataEntity;
import com.NQH.MachineLearning.Mapper.DataMapper;
import com.NQH.MachineLearning.Mapper.TrainingMapper;
import com.NQH.MachineLearning.repository.DataRepository;
import com.NQH.MachineLearning.repository.ModelRepository;
import com.NQH.MachineLearning.repository.TrainingDataRepository;
import com.NQH.MachineLearning.repository.TrainingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nqhkt
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TrainingService {
    
    FlaskApiService flaskApiService;
    TrainingRepository trainingRepository;
    TrainingDataRepository trainingDataRepository;
    DataRepository dataRepository;
    TrainingMapper trainingMapper;
    DataMapper dataMapper;
    ModelRepository modelRepository;
    
    public String creationTraining(TrainingCreationRequest request) {
        TrainingEntity training = TrainingEntity.builder()
                .label_target(request.getLabels_targets())
                .labels_feature(request.getLabels_features())
                .build();
        
        List<DataEntity> datas =  dataRepository.findAllById(request.getData_id());
        List<TrainingDataEntity> training_data = datas.stream()
                           .map(data -> TrainingDataEntity.builder()
                                                .data(data)
                                                .training(training)
                                                .build())
        
                           .collect(Collectors.toList());
        
        trainingRepository.save(training);
        trainingDataRepository.saveAll(training_data);
        
        return "Creation Success";
    }
    
    public List<TrainingResponse> getAllTraining() {
        
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::toTrainingResonse).toList();
    }
    
    public TrainingResponse getTraining(String trainingId) {
        TrainingEntity training = trainingRepository.findById(trainingId).orElseThrow(() -> new RuntimeException("Training not found"));
        TrainingResponse trainingResponse = trainingMapper.toTrainingResonse(training);
        List<TrainingDataEntity> trainingData = trainingDataRepository.findAllByTraining(training);
        List<DataEntity> datas = trainingData.stream()
                .map(TrainingDataEntity::getData) // Lấy thuộc tính data từ TrainingDataEntity
                .collect(Collectors.toList());
        
        List<DataResponse> dataResponse = dataMapper.toDataResponseList(datas);
        trainingResponse.setData(dataResponse);
        return trainingResponse;
    }
    
    public String trainModel(TrainModelRequest request, String id) 
                    throws JsonProcessingException {
        
        TrainingEntity training = trainingRepository.findById(id).get();
        List<TrainingDataEntity> trainingDatas = trainingDataRepository.findAllByTraining(training);
        List<DataEntity> dataTrains = trainingDatas.stream()
                .map(TrainingDataEntity::getData)
                .toList();
                
        TrainingDataEntity trainingData = trainingDatas.get(0);
        DataEntity data = trainingData.getData();
        DatasetEntity dataset = data.getDataset();
        String type = dataset.getType();
        
        
        Map<String, Object> response = new HashMap<>();
        
        
        DataEntity dataTest =  dataRepository.findById(request.getDataTestId()).get();
        List<String> dataTrainsLocation = dataTrains.stream()
                                                    .map(DataEntity::getLocation)
                                                    .toList();
        String dataTestLocation = dataTest.getLocation();
        
        response = flaskApiService.callFlaskTrainingApi(
                dataTrainsLocation,
                dataTestLocation,
                request.getLabelsFeatures(),
                request.getLabelTarget(),
                request.getAlgorithm(),
                type);
        
        log.warn(response.toString());

        Map<String, Object> metrics = new HashMap<>();
        ModelEntity model = new ModelEntity();
        response.forEach(
                (String key, Object value)
                -> {
            if (key.equals("model_path")) {
               model.setLocation(value.toString());
            }
            else{
                metrics.put(key, value);
            }
        });
        model.setMetrics(new ObjectMapper().writeValueAsString(metrics));
//        log.warn("Training: "+ training.toString() );
        model.setTraining(training);
        modelRepository.save(model);
        return "Sucees";
    }
}