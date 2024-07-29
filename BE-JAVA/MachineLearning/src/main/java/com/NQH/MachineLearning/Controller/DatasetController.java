/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Controller;

import com.NQH.MachineLearning.DTO.Request.ApiResponse;
import com.NQH.MachineLearning.DTO.Request.DatasetCreateRequest;
import com.NQH.MachineLearning.DTO.Request.DatasetUpdateRequest;
import com.NQH.MachineLearning.DTO.Response.DatasetResponse;
import com.NQH.MachineLearning.DTO.Response.UserResponse;
import com.NQH.MachineLearning.service.DatasetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nqhkt
 */
@RestController
@RequestMapping("/dataset")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class DatasetController {

    DatasetService datasetService;
    
    @PostMapping("")
    ApiResponse<DatasetResponse> createDataset( @RequestBody DatasetCreateRequest request) {
        return ApiResponse.<DatasetResponse>builder()
                .result(datasetService.createDataset(request))
                .build();
    }
    @PutMapping("/{datasetId}")
    ApiResponse<DatasetResponse> updateDataset(@Valid @PathVariable String datasetId, @RequestBody DatasetUpdateRequest request) {
        return ApiResponse.<DatasetResponse>builder()
                .result(datasetService.updateDataset(datasetId, request))
                .build();
    }
    @GetMapping()
    ApiResponse<List<DatasetResponse>> getAllDataset() {
        return ApiResponse.<List<DatasetResponse>>builder()
                .result(datasetService.getAllDataset())
                .build();
    }
    @GetMapping("/{datasetId}")
     ApiResponse<DatasetResponse> getDataset(@PathVariable String datasetId) {
        return ApiResponse.<DatasetResponse>builder()
                .result(datasetService.getDataset(datasetId))
                .build();
    }
     @DeleteMapping("/{datasetId}")
     ApiResponse<String> deleteDataset(@PathVariable String datasetId){
         return ApiResponse.<String>builder()
                 .result(datasetService.deleteDataset(datasetId))
                 .build();
     }
}
