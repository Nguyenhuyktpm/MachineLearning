/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NQH.MachineLearning.Mapper;

import com.NQH.MachineLearning.DTO.Response.DataResponse;
import com.NQH.MachineLearning.Entity.DataEntity;
import java.util.List;
import org.mapstruct.Mapper;

/**
 *
 * @author nqhkt
 */
@Mapper(componentModel = "spring")
public interface DataMapper {
    
    DataResponse toDataResponse(DataEntity entity);
     List<DataResponse> toDataResponseList(List<DataEntity> dataEntities);
}
