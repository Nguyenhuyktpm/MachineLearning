/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Controller;

import com.NQH.MachineLearning.DTO.Request.ApiResponse;
import com.NQH.MachineLearning.DTO.Response.DataResponse;
import com.NQH.MachineLearning.service.DataService;
import java.io.IOException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nqhkt
 */
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataController {
    
    DataService dataService;
    
    @PostMapping("/{id}")
    ApiResponse<String> uploadFile(@PathVariable String id,@RequestParam("file") MultipartFile file) {
        
        return ApiResponse.<String>builder()
                .result(dataService.createData(file, id))
                .build();
    }
    
    @GetMapping("file/{dataset_id}")
    ApiResponse<DataResponse> getFile(@PathVariable String dataset_id){
        
        return ApiResponse.<DataResponse>builder()
                .result(dataService.getData(dataset_id))
                .build();
    }
    
    @GetMapping()
    ApiResponse<List<DataResponse>> getAllFile(){
        
        return ApiResponse.<List<DataResponse>>builder()
                .result(dataService.getAllData())
                .build();
    }
    
    
    @GetMapping("labels/{id}")
    ApiResponse<List<String>> getLabels(@PathVariable String id) throws IOException{
        
        return ApiResponse.<List<String>>builder()
                .result(dataService.getLabels(id))
                .build();
    }
}