package com.example.etl_backend.controller;

import com.example.etl_backend.service.CsvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UploadController {

  private final CsvService csvService;

  public UploadController(CsvService csvService) {
    this.csvService = csvService;
  }

  /**
   * Endpoint to upload a CSV file and parse it.
   * @param file the uploaded CSV file
   * @return a list of records parsed from the CSV file
   */

  @PostMapping("/upload")
  public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return ResponseEntity.badRequest().body("No file uploaded");
    }
    try {
      List<Map<String, String>> rows = csvService.parseCsv(file);
      return ResponseEntity.ok(rows);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to parse CSV: " + e.getMessage());
    }
  }
}
