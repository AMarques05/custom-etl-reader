package com.example.etl_backend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
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

        //Switch case loop to check which cleaning option to perform
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
                case "fix_date_format":
                    cleanData = fixDateFormat(cleanData);
                    break;
                default:
                    System.out.println("Unknown cleaning option: " + option);
            }
        }



        return cleanData;
    }


    //Respective cleaning options for each cleaning option
    private List<Map<String, Object>> removeEmptyRows(List<Map<String, Object>> data) {
        System.out.println("Applying: Remove empty rows");
        return data.stream()
                .filter(row -> {
                    if (row == null || row.isEmpty()) {
                        return false; // Remove completely empty rows
                    }
                    
                    // Remove rows that have ANY empty/null values
                    return row.values().stream().allMatch(value -> 
                            value != null && !value.toString().trim().isEmpty());
                })
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
        System.out.println("Applying: Replace null values with N/A or None");
        return data.stream()
            .map(row -> {
                Map<String, Object> cleanedRow = new LinkedHashMap<>();
                row.forEach((key, value) -> {
                    if (value == null || value.toString().trim().isEmpty() || 
                        value.toString().equalsIgnoreCase("null")) {
                        
                            //If number put None if a word/string put N/A
                        if (isNumericColumn(data, key)) {
                            cleanedRow.put(key, "None");
                        } else {
                            cleanedRow.put(key, "N/A");
                        }
                    } else {
                        cleanedRow.put(key, value);
                    }
                });
                return cleanedRow;
            })
            .collect(Collectors.toList());
    }

    private boolean isNumericColumn(List<Map<String, Object>> data, String columnName) {
    // Sample a few non-null values to determine if column is numeric
    return data.stream()
            .limit(10) // Check first 10 rows for performance
            .map(row -> row.get(columnName))
            .filter(value -> value != null && !value.toString().trim().isEmpty() && 
                           !value.toString().equalsIgnoreCase("null"))
            .anyMatch(value -> {
                try {
                    Double.parseDouble(value.toString().trim());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
    }

    private List<Map<String, Object>> fixDateFormat(List<Map<String, Object>> data) {
        System.out.println("Applying: Fix date format");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String) {
                            String dateValue = (String) value;
                            // Try to standardize common date formats to YYYY-MM-DD
                            String fixedDate = standardizeDateFormat(dateValue);
                            cleanedRow.put(key, fixedDate);
                        } else {
                            cleanedRow.put(key, value);
                        }
                    });
                    return cleanedRow;
                })
                .collect(Collectors.toList());
    }

    private String standardizeDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return dateStr;
        }
        
        String trimmed = dateStr.trim();
        
        // Common date patterns to standardize
        // MM/DD/YYYY or MM-DD-YYYY -> YYYY-MM-DD
        if (trimmed.matches("\\d{1,2}[/-]\\d{1,2}[/-]\\d{4}")) {
            String[] parts = trimmed.split("[/-]");
            return String.format("%s-%02d-%02d", parts[2], Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        
        // DD/MM/YYYY or DD-MM-YYYY -> YYYY-MM-DD (European format)
        if (trimmed.matches("\\d{1,2}[/-]\\d{1,2}[/-]\\d{4}") && trimmed.contains(".")) {
            String[] parts = trimmed.split("[/.-]");
            return String.format("%s-%02d-%02d", parts[2], Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
        }
        
        // YYYY/MM/DD or YYYY-MM-DD -> keep as YYYY-MM-DD
        if (trimmed.matches("\\d{4}[/-]\\d{1,2}[/-]\\d{1,2}")) {
            String[] parts = trimmed.split("[/-]");
            return String.format("%s-%02d-%02d", parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }
        
        // If no pattern matches, return original
        return dateStr;
    }



}
