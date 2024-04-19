/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Configuration;

import com.NQH.MachineLearning.DTO.Request.ApiResponse;
import com.NQH.MachineLearning.Exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 *
 * @author nqhkt
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
       
        
       ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
       response.setStatus(errorCode.getHttpStatusCode().value());
       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
       
        ApiResponse<?> apiResponse = ApiResponse.builder()
                                                .code(errorCode.getCode())
                                                .message(errorCode.getMessage())
                                                .build();
        
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
                
    }
 
    
}
