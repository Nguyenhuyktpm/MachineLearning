/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Request.DatasetCreateRequest;
import com.NQH.MachineLearning.DTO.Request.DatasetUpdateRequest;
import com.NQH.MachineLearning.DTO.Response.DatasetResponse;
import com.NQH.MachineLearning.Entity.DatasetEntity;
import com.NQH.MachineLearning.Exception.AppException;
import com.NQH.MachineLearning.Exception.ErrorCode;
import com.NQH.MachineLearning.Mapper.DatasetMapper;
import com.NQH.MachineLearning.Repository.UserRepository;
import com.NQH.MachineLearning.repository.DatasetRepository;
import java.util.List;
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
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class DatasetService {

    DatasetRepository datasetRepository;
    DatasetMapper datasetMapper;
    UserRepository userRepository;

    public DatasetResponse createDataset(DatasetCreateRequest request) {

        DatasetEntity dataset = datasetMapper.todataset(request);
        dataset.setUser(userRepository.findById(request.getUserId()).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        log.warn("YourData:" + dataset.toString());
        return datasetMapper.toDatasetResponse(datasetRepository.save(dataset));
    }

    public DatasetResponse updateDataset(String id, DatasetUpdateRequest request) {
        DatasetEntity dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_EXISTED));

        log.warn("Before: " + dataset.toString());
        datasetMapper.updateDataset(dataset, request);

        log.warn("After: " + dataset.toString());
        return datasetMapper.toDatasetResponse(datasetRepository.save(dataset));

    }

    public List<DatasetResponse> getAllDataset() {

        return datasetRepository.findAll()
                .stream()
                .map(datasetMapper::toDatasetResponse).toList();
    }

    public DatasetResponse getDataset(String id) {
        DatasetEntity dataset = datasetRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.DATASET_NOT_EXISTED));
        return datasetMapper.toDatasetResponse(dataset);

    }
}
