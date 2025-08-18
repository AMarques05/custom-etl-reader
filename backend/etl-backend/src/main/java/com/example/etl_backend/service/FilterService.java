package com.example.etl_backend.service;

import org.springframework.stereotype.Service;
import com.example.etl_backend.controller.FilterDto;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class FilterService {

    public List<Map<String,Object>> filterData(List<Map<String, Object>> csvData, List<String> selectedColumns, List<FilterDto> filters) {

        
        return csvData;
    }
    
}
