/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "dataset")
@AllArgsConstructor
@NoArgsConstructor
public class DatasetEntity extends BaseEntity {

    String name;
    String type;

    @OneToMany(mappedBy = "dataset",fetch = FetchType.LAZY)
    List<DataEntity> data;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY )
    @JoinColumn(name = "user_id")
    UserEntity user;
}
