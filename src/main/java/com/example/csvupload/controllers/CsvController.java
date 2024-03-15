package com.example.csvupload.controllers;

import com.example.csvupload.entities.CsvData;
import com.example.csvupload.repositories.CsvRepository;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CsvController {
    @Autowired
    CsvRepository csvRepository;
    @PostMapping("/upload")
    public String uploadCsvFile(@RequestParam("file") MultipartFile file) throws Exception{
        List<CsvData> csvData = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings =new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        parser.parseAllRecords(inputStream);
        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
        parseAllRecords.forEach(record -> {
            CsvData csvData1 = new CsvData();
            csvData1.setCode(record.getString("code"));
            csvData1.setSource(record.getString("source"));
            csvData1.setCodeListCode(record.getString("codeListCode"));
            csvData1.setDisplayValue(record.getString("displayValue"));
            csvData1.setFromDate(record.getDate("fromDate"));
            csvData1.setToDate(record.getDate("toDate"));
            csvData1.setSortingPriority(record.getInt("sortingPriority"));
            csvData.add(csvData1);
        });
        csvRepository.saveAll(csvData);
        return "upload success";
    }
    @GetMapping("/data")
    public List<CsvData> getAllData() {
        return csvRepository.findAll();
    }

    @GetMapping("/data/{code}")
    public ResponseEntity<CsvData> getDataByCode(@PathVariable String code) {
        Optional<CsvData> csvDataOptional = csvRepository.findByCode(code);
        return csvDataOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public void deleteAllData() {
        csvRepository.deleteAll();
    }
}
