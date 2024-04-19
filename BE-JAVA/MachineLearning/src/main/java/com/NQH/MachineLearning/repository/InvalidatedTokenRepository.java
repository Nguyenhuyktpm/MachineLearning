/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NQH.MachineLearning.repository;

import com.NQH.MachineLearning.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nqhkt
 */
@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String>{
    
}
