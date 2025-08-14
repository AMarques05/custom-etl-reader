package com.example.etl_backend.controller;

import java.util.List;
import java.util.Map;

public class ProcessRequestDto {
    private List<Map<String, Object>> csvData;
    private List<String> selectedColumns;
    private List<String> selectedOptions;
    private List<FilterDto> filters;

    // Constructors
    public ProcessRequestDto() {}

    // Getters and Setters
    public List<Map<String, Object>> getCsvData() { return csvData; }
    public void setCsvData(List<Map<String, Object>> csvData) { this.csvData = csvData; }

    public List<String> getSelectedColumns() { return selectedColumns; }
    public void setSelectedColumns(List<String> selectedColumns) { this.selectedColumns = selectedColumns; }

    public List<String> getSelectedOptions() { return selectedOptions; }
    public void setSelectedOptions(List<String> selectedOptions) { this.selectedOptions = selectedOptions; }

    public List<FilterDto> getFilters() { return filters; }
    public void setFilters(List<FilterDto> filters) { this.filters = filters; }
}
