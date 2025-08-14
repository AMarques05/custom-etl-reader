package com.example.etl_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Adjust for your frontend port
public class ProcessController {

    @PostMapping("/process")
    public ResponseEntity<?> processData(@RequestBody ProcessRequestDto request) {
        try {
            // Your processing logic here
            // Process request.getCsvData(), request.getSelectedColumns(), etc.
            List<Map<String, Object>> csvData = request.getCsvData();
            List<String> selectedColumns = request.getSelectedColumns();
            List<String> selectedOptions = request.getSelectedOptions();
            List<FilterDto> filters = request.getFilters();

            // Print out all the "request" variables
            System.out.println("CSV Data: " + csvData);
            System.out.println("Selected Columns: " + selectedColumns);
            System.out.println("Selected Options: " + selectedOptions);
            
            System.out.println("Applied Filters:");
            if (filters != null && !filters.isEmpty()) {
                for (int i = 0; i < filters.size(); i++) {
                    FilterDto filter = filters.get(i);
                    System.out.println("  Filter " + (i + 1) + ": Column='" + filter.getColumn() + 
                                     "', Operator='" + filter.getOperator() + 
                                     "', Value='" + filter.getValue() + "'");
                }
            } else {
                System.out.println("  No filters applied");
            }

            String processedData = "Processed data"; // Replace with actual processing result

            return ResponseEntity.ok(processedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing data: " + e.getMessage());
        }
    }
}
