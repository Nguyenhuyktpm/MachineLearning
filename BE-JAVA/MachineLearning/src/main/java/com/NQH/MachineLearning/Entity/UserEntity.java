/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    String username;
    String password;
    String fullname;
    String email;
    LocalDate dob;

    Set<String> roles;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    List<DatasetEntity> datasets;
}
