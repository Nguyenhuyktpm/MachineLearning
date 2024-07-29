/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Configuration.AudittingConfig;

import com.NQH.MachineLearning.Entity.UserEntity;
import com.NQH.MachineLearning.Repository.UserRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author nqhkt
 */

public class AuditorAwareImpl implements  AuditorAware<String>{
    
    @Autowired
    private UserRepository userRepository;
    
   
    @Override
    @Transactional(propagation  = Propagation.REQUIRES_NEW)
    public Optional<String> getCurrentAuditor() {
        String username = "";
        if(SecurityContextHolder.getContext().getAuthentication() !=null){
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        UserEntity user = userRepository.findByUsername(username).get();
        
        return Optional.ofNullable(user.getFullname());
    }
}
