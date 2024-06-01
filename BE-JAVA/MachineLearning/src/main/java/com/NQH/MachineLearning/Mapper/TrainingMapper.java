/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Mapper;

import com.NQH.MachineLearning.DTO.Response.TrainingResponse;
import com.NQH.MachineLearning.Entity.TrainingEntity;
import org.mapstruct.Mapper;

/**
 *
 * @author nqhkt
 */
@Mapper(componentModel = "spring")
public interface TrainingMapper {
    TrainingResponse toTrainingResonse(TrainingEntity entity);
}
