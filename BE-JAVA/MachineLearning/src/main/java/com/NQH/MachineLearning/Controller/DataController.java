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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    @PostMapping(value = "/{datasetId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<DataResponse> uploadFile(@PathVariable String datasetId,@RequestPart("file") MultipartFile file) {
        
        return ApiResponse.<DataResponse>builder()
                .result(dataService.createData(file, datasetId))
                .build();
    }
    
    @GetMapping("{dataId}")
    ApiResponse<DataResponse> getFile(@PathVariable String dataId){
        
        return ApiResponse.<DataResponse>builder()
                .result(dataService.getData(dataId))
                .build();
    }
    
    @GetMapping()
    ApiResponse<List<DataResponse>> getAllFile(){
        
        return ApiResponse.<List<DataResponse>>builder()
                .result(dataService.getAllData())
                .build();
    }
    
    
    @GetMapping("labels/{dataId}")
    ApiResponse<List<String>> getLabels(@PathVariable String dataId) throws IOException{
        
        return ApiResponse.<List<String>>builder()
                .result(dataService.getLabels(dataId))
                .build();
    }
    
    @DeleteMapping("{dataId}")
        ApiResponse<String> deleteData(@PathVariable String dataId){
        
        return ApiResponse.<String>builder()
                .result(dataService.deleteData(dataId))
                .build();
    }
}
