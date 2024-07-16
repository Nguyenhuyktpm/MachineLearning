/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 *
 * @author nqhkt
 */
@Data
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "training")
@AllArgsConstructor
@NoArgsConstructor

public class TrainingEntity extends BaseEntity {
    
    String name;
    List<String> labels_feature;
    String label_target;
    LocalDateTime training_start;
    LocalDateTime training_end;
    String training_status;

    @OneToOne(mappedBy = "training",fetch = FetchType.LAZY)
    ModelEntity model;

    @OneToMany(mappedBy = "training",fetch = FetchType.LAZY)
    List<TrainingDataEntity> training_datas;
}
