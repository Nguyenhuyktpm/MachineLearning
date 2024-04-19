/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Controller;

import com.NQH.MachineLearning.DTO.Request.ApiResponse;
import com.NQH.MachineLearning.DTO.Request.AuthenticationRequest;
import com.NQH.MachineLearning.DTO.Request.IntrospectRequest;
import com.NQH.MachineLearning.DTO.Request.LogoutRequest;
import com.NQH.MachineLearning.DTO.Request.RefreshRequest;
import com.NQH.MachineLearning.DTO.Response.AuthenticationResponse;
import com.NQH.MachineLearning.DTO.Response.IntrospectResponse;
import com.NQH.MachineLearning.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nqhkt
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
       AuthenticationService authenticationService;
       
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    
    
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) 
            throws ParseException, JOSEException{
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                            .build();
    }
    
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request) 
            throws JOSEException, ParseException{
        
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    
}
