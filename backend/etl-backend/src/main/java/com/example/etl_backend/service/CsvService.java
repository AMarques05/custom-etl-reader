package com.example.etl_backend.service;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

@Service
public class CsvService {

  // Reads the CSV file and returns a list of records
  // Reading the first row as headers and if there is no header it returns an empty list
  // For each subsequent row, it creates a map of column name to value
  // Later Returning a List<Map<String, String>> for all the rows
  public List<Map<String, String>> parseCsv(MultipartFile file) throws IOException, com.opencsv.exceptions.CsvValidationException {
    List<Map<String, String>> records = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
      String[] header = reader.readNext();
      if (header == null) return records; // empty file

      String[] line;
      while ((line = reader.readNext()) != null) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < header.length; i++) {
          String value = i < line.length ? line[i] : "";
          map.put(header[i], value);
        }
        records.add(map);
      }
    }

    return records;
  }
}
