/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NQH.MachineLearning.Repository;

import com.NQH.MachineLearning.Entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nqhkt
 */
public interface UserRepository extends JpaRepository<UserEntity, String>{
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}
