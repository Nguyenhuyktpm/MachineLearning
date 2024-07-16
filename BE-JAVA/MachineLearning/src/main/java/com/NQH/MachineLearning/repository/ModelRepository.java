/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.repository;

import com.NQH.MachineLearning.Entity.ModelEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nqhkt
 */
public interface ModelRepository extends JpaRepository<ModelEntity, String>{
    List<ModelEntity> findByDeletedAtIsNull();
}
