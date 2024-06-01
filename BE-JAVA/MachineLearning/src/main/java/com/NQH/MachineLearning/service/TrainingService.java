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
import com.NQH.MachineLearning.repository.TrainingDataRepository;
import com.NQH.MachineLearning.repository.TrainingRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public String creationTraining(TrainingCreationRequest request) {
        TrainingEntity training = TrainingEntity.builder()
                .label_target(request.getLabels_targets())
                .labels_feature(request.getLabels_features())
                .build();

        TrainingDataEntity training_data = TrainingDataEntity.builder()
                .data(dataRepository.findById(request.getData_id()).get())
                .training(training)
                .build();

        trainingRepository.save(training);
        trainingDataRepository.save(training_data);

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

    public String trainModel(TrainModelRequest request, String id) {

        TrainingEntity training = trainingRepository.findById(id).get();
        List<TrainingDataEntity> trainingDatas = trainingDataRepository.findAllByTraining(training);
        TrainingDataEntity trainingData = trainingDatas.get(0);
        DataEntity data = trainingData.getData();
        DatasetEntity dataset = data.getDataset();
        String type = dataset.getType();
        Map<String, Object> response = new HashMap<>();
        
            response = flaskApiService.callFlaskTrainingApi(
                    request.getTrainFileLinks(),
                    request.getTestFileLink(),
                    request.getLabelsFeatures(),
                    request.getLabelTarget(),
                    request.getAlgorithm(),
                    type);
            log.warn("Response: " + response.toString());
        
        log.warn("Type: " + type);
        log.warn("Response: " + response.toString());
        return "Sucees";
    }
}
