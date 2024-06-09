/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NQH.MachineLearning.Mapper;

import com.NQH.MachineLearning.DTO.Response.ModelResponse;
import com.NQH.MachineLearning.Entity.ModelEntity;
import org.mapstruct.Mapper;

/**
 *
 * @author nqhkt
 */
@Mapper(componentModel = "spring")
public interface ModelMapper {
    ModelResponse toModelResponse(ModelEntity model);
}
