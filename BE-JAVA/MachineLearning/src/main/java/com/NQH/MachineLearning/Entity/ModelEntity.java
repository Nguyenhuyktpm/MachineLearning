/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "model")
@AllArgsConstructor
@NoArgsConstructor
public class ModelEntity extends BaseEntity{
    String location;
    String algorithm;
    @ManyToOne
    @JoinColumn(name = "dataset_id")
    DatasetEntity dataset;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_id",referencedColumnName = "id")
    TrainingEntity training;
    
    @Column(columnDefinition = "json")
    String metrics;
}
