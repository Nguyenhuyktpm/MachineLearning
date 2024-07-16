/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NQH.MachineLearning.repository;

import com.NQH.MachineLearning.Entity.TrainingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author nqhkt
 */
public interface TrainingRepository extends JpaRepository<TrainingEntity, String> {
    List<TrainingEntity> findByDeletedAtIsNull();
    TrainingEntity findByName(String name);
}
