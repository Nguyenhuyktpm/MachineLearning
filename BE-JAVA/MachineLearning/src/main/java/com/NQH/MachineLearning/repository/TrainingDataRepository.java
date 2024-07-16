/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.repository;

import com.NQH.MachineLearning.Entity.DataEntity;
import com.NQH.MachineLearning.Entity.TrainingEntity;
import com.NQH.MachineLearning.Entity.TrainingDataEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nqhkt
 */
public interface TrainingDataRepository extends JpaRepository<TrainingDataEntity, String>{
    List<TrainingDataEntity> findAllByTraining(TrainingEntity training);
    TrainingDataEntity deleteByTraining(TrainingEntity training);
    TrainingDataEntity deleteByData(DataEntity data);
    List<TrainingDataEntity> findByData(DataEntity data);
    
     
}
