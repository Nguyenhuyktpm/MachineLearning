/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Request.TrainModelRequest;
import com.NQH.MachineLearning.DTO.Request.TrainingCreationRequest;
import com.NQH.MachineLearning.DTO.Response.DataResponse;
import com.NQH.MachineLearning.DTO.Response.ModelResponse;
import com.NQH.MachineLearning.DTO.Response.TrainingResponse;
import com.NQH.MachineLearning.Entity.DataEntity;
import com.NQH.MachineLearning.Entity.DatasetEntity;
import com.NQH.MachineLearning.Entity.ModelEntity;
import com.NQH.MachineLearning.Entity.TrainingEntity;
import com.NQH.MachineLearning.Entity.TrainingDataEntity;
import com.NQH.MachineLearning.Exception.AppException;
import com.NQH.MachineLearning.Exception.ErrorCode;
import com.NQH.MachineLearning.Mapper.DataMapper;
import com.NQH.MachineLearning.Mapper.ModelMapper;
import com.NQH.MachineLearning.Mapper.TrainingMapper;
import com.NQH.MachineLearning.repository.DataRepository;
import com.NQH.MachineLearning.repository.ModelRepository;
import com.NQH.MachineLearning.repository.TrainingDataRepository;
import com.NQH.MachineLearning.repository.TrainingRepository;
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
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

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
    ModelMapper modelMapper;

    public TrainingResponse creationTraining(TrainingCreationRequest request) {
        log.warn(request.getName());

        TrainingEntity oldTraining = trainingRepository.findByName(request.getName());
        if (oldTraining != null) {
            throw new AppException(ErrorCode.TRAINING_EXISTED);
        }

        TrainingEntity training = TrainingEntity.builder()
                .label_target(request.getLabels_targets())
                .labels_feature(request.getLabels_features())
                .name(request.getName())
                .build();

        List<DataEntity> datas = dataRepository.findAllById(request.getData_id());
        List<TrainingDataEntity> training_data = datas.stream()
                .map(data -> TrainingDataEntity.builder()
                .data(data)
                .training(training)
                .build())
                .collect(Collectors.toList());

        trainingRepository.save(training);
        trainingDataRepository.saveAll(training_data);

        return trainingMapper.toTrainingResonse(training);
    }

    public List<TrainingResponse> getAllTraining() {

        return trainingRepository.findByDeletedAtIsNull()
                .stream()
                .map(trainingMapper::toTrainingResonse).toList();
    }

    public TrainingResponse getTraining(String trainingId) {
        TrainingEntity training = trainingRepository.findById(trainingId).orElseThrow(()
                -> new AppException(ErrorCode.TRAINING_NOT_EXISTED));

        if (training.getDeletedAt() != null) {
            throw new AppException(ErrorCode.TRAINING_NOT_EXISTED);
        }

        TrainingResponse trainingResponse = trainingMapper.toTrainingResonse(training);
        List<TrainingDataEntity> trainingData = trainingDataRepository.findAllByTraining(training);
        List<DataEntity> datas = trainingData.stream()
                .map(TrainingDataEntity::getData) // Lấy thuộc tính data từ TrainingDataEntity
                .collect(Collectors.toList());

        List<DataResponse> dataResponse = dataMapper.toDataResponseList(datas);
        trainingResponse.setData(dataResponse);
        return trainingResponse;
    }

    public ModelResponse trainModel(TrainModelRequest request, String id)
            throws JsonProcessingException {

        TrainingEntity training = trainingRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.TRAINING_NOT_EXISTED));
        if (training.getDeletedAt() != null) {
            throw new AppException(ErrorCode.TRAINING_NOT_EXISTED);
        }

        List<TrainingDataEntity> trainingDatas = trainingDataRepository.findAllByTraining(training);
        List<DataEntity> dataTrains = trainingDatas.stream()
                .map(TrainingDataEntity::getData)
                .toList();

        TrainingDataEntity trainingData = trainingDatas.get(0);
        DataEntity data = trainingData.getData();
        DatasetEntity dataset = data.getDataset();
        String type = dataset.getType();

        Map<String, Object> response;

        DataEntity dataTest = dataRepository.findById(request.getDataTestId()).get();
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

            } else if (key.equals("encoder_path")) {
                model.setEncodeLocation(value.toString());
            } else {
                metrics.put(key, value);
            }
        });
        model.setMetrics(new ObjectMapper().writeValueAsString(metrics));

//        log.warn("Training: "+ training.toString() );
        model.setTraining(training);
        modelRepository.save(model);
        return modelMapper.toModelResponse(model);
    }

    public String deleteTraining(String trainingId) {
        TrainingEntity training = trainingRepository.findById(trainingId).orElseThrow(()
                -> new AppException(ErrorCode.TRAINING_NOT_EXISTED));
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        training.setDeletedAt(deletedAt);
        trainingRepository.save(training);
        return "Training deleted !";
    }
}
