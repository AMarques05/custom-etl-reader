package com.example.etl_backend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
public class CleanService {

    public List<Map<String, Object>> cleanData(List<Map<String, Object>> csvData, List<String> selectedOptions) {
        
        System.out.println("Cleaning started");
        System.out.println("Options:" + selectedOptions);

        if(csvData == null || csvData.isEmpty()) {
            System.out.println("No data to clean");
            return new ArrayList<>();
        }

                List<Map<String, Object>> cleanData = new ArrayList<>(csvData);

        for (String option : selectedOptions) {
            switch (option.toLowerCase()) {
                case "remove_empty_rows":
                    cleanData = removeEmptyRows(cleanData);
                    break;
                case "trim_whitespace":
                    cleanData = trimWhitespace(cleanData);
                    break;
                case "remove_duplicates":
                    cleanData = removeDuplicates(cleanData);
                    break;
                case "standardize_case":
                    cleanData = standardizeCase(cleanData);
                    break;
                case "remove_special_characters":
                    cleanData = removeSpecialCharacters(cleanData);
                    break;
                case "remove_null_values":
                    cleanData = removeNullValues(cleanData);
                    break;
                default:
                    System.out.println("Unknown cleaning option: " + option);
            }
        }



        return cleanData;
    }

    private List<Map<String, Object>> removeEmptyRows(List<Map<String, Object>> data) {
        System.out.println("Applying: Remove empty rows");
        return data.stream()
                .filter(row -> row != null && !row.isEmpty() && 
                        row.values().stream().anyMatch(value -> 
                                value != null && !value.toString().trim().isEmpty()))
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> trimWhitespace(List<Map<String, Object>> data) {
        System.out.println("Applying: Trim whitespace");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String) {
                            cleanedRow.put(key, ((String) value).trim());
                        } else {
                            cleanedRow.put(key, value);
                        }
                    });
                    return cleanedRow;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> removeDuplicates(List<Map<String, Object>> data) {
        System.out.println("Applying: Remove duplicates");
        Set<Map<String, Object>> seen = new HashSet<>();
        return data.stream()
                .filter(row -> {
                    Map<String, Object> normalizedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        String normalizedValue = value != null ? value.toString().trim().toLowerCase() : "";
                        normalizedRow.put(key, normalizedValue);
                    });
                    return seen.add(normalizedRow);
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> standardizeCase(List<Map<String, Object>> data) {
        System.out.println("Applying: Standardize case (lowercase)");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String) {
                            cleanedRow.put(key, ((String) value).toLowerCase().trim());
                        } else {
                            cleanedRow.put(key, value);
                        }
                    });
                    return cleanedRow;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> removeSpecialCharacters(List<Map<String, Object>> data) {
        System.out.println("Applying: Remove special characters");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String) {
                            String cleaned = ((String) value).replaceAll("[^a-zA-Z0-9\\s.,'-]", "").trim();
                            cleanedRow.put(key, cleaned);
                        } else {
                            cleanedRow.put(key, value);
                        }
                    });
                    return cleanedRow;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> removeNullValues(List<Map<String, Object>> data) {
        System.out.println("Applying: Remove null values");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value != null && !value.toString().trim().isEmpty() && 
                            !value.toString().equalsIgnoreCase("null")) {
                            cleanedRow.put(key, value);
                        }
                    });
                    return cleanedRow;
                })
                .filter(row -> !row.isEmpty()) // Remove rows that become empty after null removal
                .collect(Collectors.toList());
    }



}
