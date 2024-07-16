/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Request.DatasetCreateRequest;
import com.NQH.MachineLearning.DTO.Request.DatasetUpdateRequest;
import com.NQH.MachineLearning.DTO.Response.DatasetResponse;
import com.NQH.MachineLearning.Entity.DatasetEntity;
import com.NQH.MachineLearning.Entity.UserEntity;
import com.NQH.MachineLearning.Exception.AppException;
import com.NQH.MachineLearning.Exception.ErrorCode;
import com.NQH.MachineLearning.Mapper.DatasetMapper;
import com.NQH.MachineLearning.Repository.UserRepository;
import com.NQH.MachineLearning.repository.DatasetRepository;
import java.sql.Timestamp;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
        DatasetEntity oldDataset =  datasetRepository.findByName(request.getName());
        if(oldDataset != null){
            throw new AppException(ErrorCode.DATASET_EXISTED);
        }
        DatasetEntity dataset = datasetMapper.todataset(request);
        UserEntity user = userRepository.findById(request.getUserId()).get();
        
        dataset.setUser(user);
        
        datasetRepository.save(dataset);
     
        return datasetMapper.toDatasetResponse(dataset);
    }

    public DatasetResponse updateDataset(String id, DatasetUpdateRequest request) {
        DatasetEntity dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_EXISTED));

        DatasetEntity oldDataset = datasetRepository.findByName(request.getName());
        if(oldDataset != null){
            throw new AppException(ErrorCode.DATASET_EXISTED);
        }
        datasetMapper.updateDataset(dataset, request);

        
        return datasetMapper.toDatasetResponse(datasetRepository.save(dataset));

    }

    public List<DatasetResponse> getAllDataset() {

        return datasetRepository.findByDeletedAtIsNull()
                .stream()
                .map(datasetMapper::toDatasetResponse).toList();
    }

    public DatasetResponse getDataset(String id) {
        DatasetEntity dataset = datasetRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.DATASET_NOT_EXISTED));
        if(dataset.getDeletedAt() != null)
            throw new AppException(ErrorCode.DATASET_NOT_EXISTED);
        return datasetMapper.toDatasetResponse(dataset);

    }

    public String deleteDataset(String datasetId) {
        DatasetEntity dataset = datasetRepository.findById(datasetId).orElseThrow(()
                -> new AppException(ErrorCode.DATASET_NOT_EXISTED));
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        dataset.setDeletedAt(deletedAt);
        
        datasetRepository.save(dataset);
        return "Dataset deleted !";
    }
}
