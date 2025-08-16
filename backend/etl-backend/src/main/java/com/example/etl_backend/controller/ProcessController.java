package com.example.etl_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.etl_backend.service.CleanService;
import com.example.etl_backend.service.FilterService;

@RestController
@RequestMapping("/api/process")
@CrossOrigin(origins = "http://localhost:3000") // Adjust for your frontend port
public class ProcessController {

    private final CleanService cleanService;
    private final FilterService filterService;

    public ProcessController(CleanService cleanService, FilterService filterService) {
        this.cleanService = cleanService;
        this.filterService = filterService;
    }

    //PostMapping for cleaning the data
    @PostMapping("/clean")
    public ResponseEntity<?> cleanData(@RequestBody ProcessRequestDto request) {
        try {
            String result = cleanService.cleanData(request.getCsvData(), request.getSelectedOptions());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error cleaning data: " + e.getMessage());
        }
    }

    //PostMapping for filtering the data
    @PostMapping("/filter")
    public ResponseEntity<?> filterData(@RequestBody ProcessRequestDto request) {
        try {
            String result = filterService.filterData(request.getCsvData(), request.getSelectedColumns(), request.getFilters());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error filtering data: " + e.getMessage());
        }
    }

}
