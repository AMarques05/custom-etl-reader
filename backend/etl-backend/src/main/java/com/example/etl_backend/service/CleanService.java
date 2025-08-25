package com.example.etl_backend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class CleanService {

    // Define placeholder values that should be preserved
    private static final Set<String> PLACEHOLDER_VALUES = Set.of("N/A", "None", "NULL", "null");

    // Helper method to check if a value is a placeholder
    private boolean isPlaceholderValue(Object value) {
        if (value == null) return false;
        String strValue = value.toString();
        return PLACEHOLDER_VALUES.contains(strValue) || 
               PLACEHOLDER_VALUES.contains(strValue.toUpperCase()) ||
               PLACEHOLDER_VALUES.contains(strValue.toLowerCase());
    }

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
                    
                    // Remove rows that have ANY empty/null values (but keep placeholder values)
                    return row.values().stream().allMatch(value -> 
                            value != null && !value.toString().trim().isEmpty() || isPlaceholderValue(value));
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> trimWhitespace(List<Map<String, Object>> data) {
        System.out.println("Applying: Trim whitespace");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String && !isPlaceholderValue(value)) {
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
        System.out.println("Input data size: " + data.size());
        
        // First, identify date columns for normalization
        Set<String> dateColumns = identifyDateColumns(data);
        System.out.println("Date columns for normalization: " + dateColumns);
        
        Set<String> seen = new HashSet<>();
        List<Map<String, Object>> result = data.stream()
                .filter(row -> {
                    // Create a normalized signature for comparison
                    String rowSignature = row.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // Sort by key for consistent ordering
                            .map(entry -> {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                
                                if (isPlaceholderValue(value)) {
                                    return key + ":" + value.toString();
                                } else if (value == null) {
                                    return key + ":null";
                                } else {
                                    String stringValue = value.toString().trim();
                                    
                                    // If this is a date column, try to normalize the date format
                                    if (dateColumns.contains(key) && !stringValue.isEmpty()) {
                                        String normalizedDate = tryNormalizeDateForComparison(stringValue);
                                        return key + ":" + normalizedDate.toLowerCase();
                                    } else {
                                        // For non-date values, just normalize case and whitespace
                                        return key + ":" + stringValue.toLowerCase();
                                    }
                                }
                            })
                            .collect(Collectors.joining("|"));
                    
                    boolean isUnique = seen.add(rowSignature);
                    
                    if (!isUnique) {
                        System.out.println("Duplicate found and removed: " + rowSignature);
                    }
                    
                    return isUnique;
                })
                .collect(Collectors.toList());
        
        System.out.println("Output data size: " + result.size());
        System.out.println("Removed " + (data.size() - result.size()) + " duplicate rows");
        
        return result;
    }

    private String tryNormalizeDateForComparison(String dateStr) {
        try {
            // Try to parse and normalize the date to YYYY-MM-DD format for comparison
            String normalized = standardizeDateFormat(dateStr);
            
            // If standardization worked (date was successfully parsed), use the normalized version
            if (!normalized.equals(dateStr)) {
                return normalized;
            }
            
            // If it looks like a date but couldn't be standardized, return as-is
            return dateStr;
        } catch (Exception e) {
            // If any error occurs, just return the original string
            return dateStr;
        }
    }

    private List<Map<String, Object>> standardizeCase(List<Map<String, Object>> data) {
        System.out.println("Applying: Standardize case (lowercase)");
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String && !isPlaceholderValue(value)) {
                            cleanedRow.put(key, ((String) value).toLowerCase().trim());
                        } else {
                            cleanedRow.put(key, value); // Preserve placeholder values
                        }
                    });
                    return cleanedRow;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> removeSpecialCharacters(List<Map<String, Object>> data) {
        System.out.println("Applying: Remove special characters");
        
        // First, identify which columns likely contain dates
        Set<String> dateColumns = identifyDateColumns(data);
        System.out.println("Preserving date separators in columns: " + dateColumns);
        
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String && !isPlaceholderValue(value)) {
                            String cleaned;
                            if (dateColumns.contains(key)) {
                                // For date columns, preserve / and - separators
                                cleaned = ((String) value).replaceAll("[^a-zA-Z0-9\\s.,'/:-]", "").trim();
                            } else {
                                // For non-date columns, remove all special characters including / and -
                                cleaned = ((String) value).replaceAll("[^a-zA-Z0-9\\s.,'-]", "").trim();
                            }
                            cleanedRow.put(key, cleaned);
                        } else {
                            cleanedRow.put(key, value); // Preserve placeholder values
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
                           !value.toString().equalsIgnoreCase("null") && !isPlaceholderValue(value))
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
        
        // First, identify which columns likely contain dates
        Set<String> dateColumns = identifyDateColumns(data);
        System.out.println("Detected date columns: " + dateColumns);
        
        return data.stream()
                .map(row -> {
                    Map<String, Object> cleanedRow = new LinkedHashMap<>();
                    row.forEach((key, value) -> {
                        if (value instanceof String && dateColumns.contains(key) && 
                            !isPlaceholderValue(value) && !value.toString().trim().isEmpty()) {
                            String dateValue = (String) value;
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

    private Set<String> identifyDateColumns(List<Map<String, Object>> data) {
        if (data.isEmpty()) return new HashSet<>();
        
        Set<String> dateColumns = new HashSet<>();
        Set<String> allColumns = data.get(0).keySet();
        
        for (String columnName : allColumns) {
            // Check if column name suggests it's a date
            if (isDateColumnByName(columnName)) {
                dateColumns.add(columnName);
                continue;
            }
            
            // Check if the values in this column look like dates
            long dateCount = data.stream()
                    .limit(20) // Sample first 20 rows
                    .map(row -> row.get(columnName))
                    .filter(value -> value != null && !value.toString().trim().isEmpty() &&
                                   !isPlaceholderValue(value))
                    .filter(value -> looksLikeDate(value.toString()))
                    .count();
            
            // If more than 60% of sampled values look like dates, consider it a date column
            long sampleSize = Math.min(20, data.size());
            if (sampleSize > 0 && ((double) dateCount / sampleSize) >= 0.6) {
                dateColumns.add(columnName);
            }
        }
        
        return dateColumns;
    }

    private boolean isDateColumnByName(String columnName) {
        String lowerName = columnName.toLowerCase();
        return lowerName.contains("date") || 
               lowerName.contains("time") || 
               lowerName.contains("created") || 
               lowerName.contains("updated") || 
               lowerName.contains("modified") ||
               lowerName.contains("birth") ||
               lowerName.contains("start") ||
               lowerName.contains("end") ||
               lowerName.contains("expiry") ||
               lowerName.contains("due") ||
               lowerName.endsWith("_at") ||
               lowerName.endsWith("_on");
    }

    private boolean looksLikeDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = value.trim();
        
        // Common date patterns
        return trimmed.matches("\\d{1,2}[/.-]\\d{1,2}[/.-]\\d{2,4}") ||          // MM/DD/YYYY variants
               trimmed.matches("\\d{4}[/.-]\\d{1,2}[/.-]\\d{1,2}") ||             // YYYY/MM/DD variants
               trimmed.matches("\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{2,4}") ||       // DD Month YYYY
               trimmed.matches("[A-Za-z]{3,9}\\s+\\d{1,2},?\\s+\\d{2,4}") ||     // Month DD, YYYY
               trimmed.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}") ||           // ISO datetime
               trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{2,4}\\s+\\d{1,2}:\\d{2}"); // Date with time
    }

    private String standardizeDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return dateStr;
        }
        
        String trimmed = dateStr.trim();
        
        try {
            // Try multiple date patterns using DateTimeFormatter
            List<DateTimeFormatter> formatters = Arrays.asList(
                // Standard formats
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                
                // US formats (MM/DD/YYYY)
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                DateTimeFormatter.ofPattern("MM.dd.yyyy"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("M-d-yyyy"),
                
                // European formats (DD/MM/YYYY)
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                
                // Short year formats
                DateTimeFormatter.ofPattern("MM/dd/yy"),
                DateTimeFormatter.ofPattern("dd/MM/yy"),
                DateTimeFormatter.ofPattern("yy/MM/dd"),
                DateTimeFormatter.ofPattern("MM-dd-yy"),
                DateTimeFormatter.ofPattern("dd-MM-yy"),
                DateTimeFormatter.ofPattern("yy-MM-dd"),
                
                // Text month formats
                DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                DateTimeFormatter.ofPattern("MMMM dd, yyyy"),
                DateTimeFormatter.ofPattern("dd MMM yyyy"),
                DateTimeFormatter.ofPattern("dd MMMM yyyy"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
                DateTimeFormatter.ofPattern("MMMM dd yyyy"),
                
                // ISO formats
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            
            // Try each formatter
            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDate date = LocalDate.parse(trimmed, formatter);
                    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    // Continue to next formatter
                }
            }
            
            // If standard formatters fail, try regex-based parsing for edge cases
            return parseWithRegex(trimmed);
            
        } catch (Exception e) {
            System.out.println("Could not parse date: " + trimmed + " - " + e.getMessage());
            return dateStr; // Return original if parsing fails
        }
    }
    
    private String parseWithRegex(String dateStr) {
        // Handle various formats with regex
        String trimmed = dateStr.trim();
        
        // MM/DD/YYYY or MM-DD-YYYY variants
        Pattern mmddyyyy = Pattern.compile("(\\d{1,2})[/-](\\d{1,2})[/-](\\d{4})");
        Matcher matcher = mmddyyyy.matcher(trimmed);
        if (matcher.matches()) {
            int month = Integer.parseInt(matcher.group(1));
            int day = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));
            
            if (isValidDate(year, month, day)) {
                return String.format("%04d-%02d-%02d", year, month, day);
            }
        }
        
        // DD/MM/YYYY European format (when day > 12, we know it's DD/MM)
        Pattern ddmmyyyy = Pattern.compile("(\\d{1,2})[/.-](\\d{1,2})[/.-](\\d{4})");
        matcher = ddmmyyyy.matcher(trimmed);
        if (matcher.matches()) {
            int first = Integer.parseInt(matcher.group(1));
            int second = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));
            
            // If first number > 12, it must be day (European format)
            if (first > 12 && isValidDate(year, second, first)) {
                return String.format("%04d-%02d-%02d", year, second, first);
            }
        }
        
        // YYYY/MM/DD variants
        Pattern yyyymmdd = Pattern.compile("(\\d{4})[/-](\\d{1,2})[/-](\\d{1,2})");
        matcher = yyyymmdd.matcher(trimmed);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            
            if (isValidDate(year, month, day)) {
                return String.format("%04d-%02d-%02d", year, month, day);
            }
        }
        
        // Handle 2-digit years (assume 20xx for years 00-30, 19xx for 31-99)
        Pattern mmddyy = Pattern.compile("(\\d{1,2})[/-](\\d{1,2})[/-](\\d{2})");
        matcher = mmddyy.matcher(trimmed);
        if (matcher.matches()) {
            int month = Integer.parseInt(matcher.group(1));
            int day = Integer.parseInt(matcher.group(2));
            int shortYear = Integer.parseInt(matcher.group(3));
            int year = shortYear <= 30 ? 2000 + shortYear : 1900 + shortYear;
            
            if (isValidDate(year, month, day)) {
                return String.format("%04d-%02d-%02d", year, month, day);
            }
        }
        
        return dateStr; // Return original if no pattern matches
    }
    
    private boolean isValidDate(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
