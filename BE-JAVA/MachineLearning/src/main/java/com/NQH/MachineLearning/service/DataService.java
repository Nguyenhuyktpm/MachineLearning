/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.service;

import com.NQH.MachineLearning.DTO.Response.DataResponse;
import com.NQH.MachineLearning.Entity.DataEntity;
import com.NQH.MachineLearning.Entity.DatasetEntity;
import com.NQH.MachineLearning.Mapper.DataMapper;
import com.NQH.MachineLearning.repository.DataRepository;
import com.NQH.MachineLearning.repository.DatasetRepository;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nqhkt
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataService {

    @Value("${file.upload-path}")
    String Path = "E:/MCLN/Data";
    DataMapper dataMapper;
    DatasetRepository datasetRepository;
    DataRepository dataRepository;

    public String createData(MultipartFile file, String id) {

        DatasetEntity dataset = datasetRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Dataset not exist"));
        String datasetname = dataset.getName();
        String uploadPath = Path + "/" + datasetname;
        try {
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            Path filePath = Paths.get(uploadPath, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            String dataName = file.getOriginalFilename();
            String dataLocation = uploadPath+"/"+file.getOriginalFilename();
            DataEntity data = DataEntity.builder()
                    .dataset(dataset)
                    .location(dataLocation)
                    .name(dataName)
                    .build();

            dataRepository.save(data);

        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred while processing the file.";
        }
        return "Upload complete";
    }

    public DataResponse getData(String id) {
        DataEntity data = dataRepository.findById(id).orElseThrow(() -> new RuntimeException("Data not exist"));
        return dataMapper.toDataResponse(data);
    }

    public List<DataResponse> getAllData() {

        return dataRepository.findAll()
                .stream()
                .map(dataMapper::toDataResponse).toList();
    }

    public List<String> getLabels(String id) throws IOException {
        DataEntity data = dataRepository.findById(id).orElseThrow(() -> new RuntimeException("Data not found"));

        List<String> labels = new ArrayList<>();
        String url = new String (data.getLocation());
       

        try (
                Reader reader = new FileReader(url); 
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {
            for (CSVRecord csvRecord : csvParser) {
                // Assume that the first row contains labels
                for (String label : csvRecord) {
                    labels.add(label);
                }
                break;
            }
        }
        return labels;
    }
}
