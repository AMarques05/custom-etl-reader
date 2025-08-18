package com.example.etl_backend.service;

import org.springframework.stereotype.Service;
import com.example.etl_backend.controller.FilterDto;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class FilterService {

    public List<Map<String,Object>> filterData(List<Map<String, Object>> csvData, List<String> selectedColumns, List<FilterDto> filters) {

        System.out.println("Filtering Process Started");
        System.out.println("Selected Columns: " + selectedColumns);
        System.out.println("Filters: " + filters);

        if(csvData == null || csvData.isEmpty()) {
            System.out.println("No data available for filtering.");
            return List.of();
        }

        return csvData;
    }
    
}
