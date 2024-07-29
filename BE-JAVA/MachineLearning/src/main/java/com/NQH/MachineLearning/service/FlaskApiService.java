/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author nqhkt
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlaskApiService {

    RestTemplate restTemplate;

    public Map<String, Object> callFlaskTrainingApi(
            List<String> trainFileLinks,
            String testFileLink,
            List<String> labelsFeatures,
            String labelTarget,
            String algorithm,
            String datasetType) {

        String url = null;
        log.warn("You started callFlaskTrainingApiClassificion");

        if (datasetType.equals("classification")) {
            url = "http://localhost:5000/training_classification";
        } else if (datasetType.equals("regression")) {
            url = "http://localhost:5000/training_regression";
        }
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("train_file_links", trainFileLinks);
        requestBody.put("test_file_link", testFileLink);
        requestBody.put("labels_features", labelsFeatures);
        requestBody.put("label_target", labelTarget);
        requestBody.put("algorithm", algorithm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        log.warn("You started callFlaskTrainingApiClassificion");

        return response.getBody();
    }

    public Map<String, Object> callFlaskRetrainedModelApi(
            String modelLocation,
            List<String> dataFileLinkOld,
            List<String> dataFileLink,
            List<String> labelsFeatures,
            String labelTarget) {

        String url = "http://localhost:5000/retrain_model";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model_location", modelLocation);
        requestBody.put("data_file_links_old", dataFileLinkOld);
        requestBody.put("data_file_links", dataFileLink);
        requestBody.put("labels_features", labelsFeatures);
        requestBody.put("label_target", labelTarget);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        return response.getBody();
    }

    public Map<String, Object> callFlaskPredictApi(
            String modelLocation,
            String dataFileLink,
            List<String> labelsFeatures,
            String encodeLocation) {

        String url = "http://localhost:5000/predict";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model_location", modelLocation);
        requestBody.put("data_file_link", dataFileLink);
        requestBody.put("labels_features", labelsFeatures);
        requestBody.put("encoder_location", modelLocation);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        return response.getBody();
    }
}
