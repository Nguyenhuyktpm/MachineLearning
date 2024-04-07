/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NQH.MachineLearning.Mapper;

import com.NQH.MachineLearning.DTO.Request.UserCreationRequest;
import com.NQH.MachineLearning.DTO.Request.UserUpdateRequest;
import com.NQH.MachineLearning.DTO.Response.UserResponse;
import com.NQH.MachineLearning.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 *
 * @author nqhkt
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
        UserEntity toUser(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity user);

    void updateUser(@MappingTarget UserEntity user, UserUpdateRequest request);
}
