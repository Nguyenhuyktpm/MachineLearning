/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Mapper;

import com.NQH.MachineLearning.DTO.Request.DatasetCreateRequest;
import com.NQH.MachineLearning.DTO.Request.DatasetUpdateRequest;
import com.NQH.MachineLearning.DTO.Response.DatasetResponse;
import com.NQH.MachineLearning.Entity.DatasetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 *
 * @author nqhkt
 */
@Mapper(componentModel = "spring")
public interface DatasetMapper {
    DatasetEntity todataset(DatasetCreateRequest request);
    
    DatasetResponse toDatasetResponse(DatasetEntity entity);
    
    void updateDataset(@MappingTarget DatasetEntity dataset, DatasetUpdateRequest request);
    
}
