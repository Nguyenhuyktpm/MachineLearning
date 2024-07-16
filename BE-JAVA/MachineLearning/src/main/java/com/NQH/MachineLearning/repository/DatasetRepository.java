/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.repository;

import com.NQH.MachineLearning.Entity.DatasetEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DatasetRepository extends JpaRepository<DatasetEntity, String>{
     List<DatasetEntity> findByDeletedAtIsNull();
     DatasetEntity findByName(String name);
}
