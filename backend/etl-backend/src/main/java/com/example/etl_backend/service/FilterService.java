package com.example.etl_backend.service;

import org.springframework.stereotype.Service;
import com.example.etl_backend.controller.FilterDto;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class FilterService {

    public List<Map<String,Object>> filterData(List<Map<String, Object>> csvData, List<String> selectedColumns, List<FilterDto> filters) {

        System.out.println("Filtering Process Started");
        System.out.println("Selected Columns: " + selectedColumns);
        System.out.println("Number of filters: " + (filters != null ? filters.size() : 0));
        
        // Better logging for filters
        if (filters != null) {
            for (int i = 0; i < filters.size(); i++) {
                FilterDto filter = filters.get(i);
                System.out.println(String.format("  Filter %d: Column='%s', Operator='%s', Value='%s'", 
                                  i + 1, filter.getColumn(), filter.getOperator(), filter.getValue()));
            }
        }

        if(csvData == null || csvData.isEmpty()) {
            System.out.println("No data available for filtering.");
            return List.of();
        }

        System.out.println("Initial data size: " + csvData.size());
        List<Map<String, Object>> filteredData = new ArrayList<>(csvData);

        // Filter data based on the filter options
        if (filters != null) {
            for(FilterDto filter : filters) {
                if (filter.getColumn() == null || filter.getValue() == null || filter.getOperator() == null) {
                    System.out.println("Skipping invalid filter: Column=" + filter.getColumn() + 
                                     ", Operator=" + filter.getOperator() + ", Value=" + filter.getValue());
                    continue;
                }
                
                String column = filter.getColumn();
                String value = filter.getValue();
                String operator = filter.getOperator();

                System.out.println(String.format("Applying filter: %s %s %s (Current data size: %d)", 
                                                column, operator, value, filteredData.size()));
                
                filteredData = applyFilter(filteredData, column, value, operator);
                
                System.out.println("Data size after filter: " + filteredData.size());
            }
        }

        System.out.println("Data size before column selection: " + filteredData.size());

        // Filter data based off of the columns (only if selectedColumns is not empty)
        if (selectedColumns != null && !selectedColumns.isEmpty()) {
            filteredData = filteredData.stream()
                .map(row -> selectedColumns.stream()
                    .filter(row::containsKey)
                    .collect(Collectors.toMap(
                        column -> column,
                        row::get
                    )))
                .collect(Collectors.toList());
        }

        System.out.println("Final data size: " + filteredData.size());
        return filteredData;
    }

    private List<Map<String, Object>> applyFilter(List<Map<String, Object>> data, String column, String value, String operator) {

        System.out.println("Applying filter - Column: " + column + ", Operator: " + operator + ", Value: " + value);
        System.out.println("Input data size: " + data.size());
        
        // Check if column exists in the data
        if (!data.isEmpty() && !data.get(0).containsKey(column)) {
            System.out.println("WARNING: Column '" + column + "' does not exist in the data!");
            System.out.println("Available columns: " + data.get(0).keySet());
            return data; // Return original data if column doesn't exist
        }

        List<Map<String, Object>> result;
        
        //Method to check which operator was used then perform filtering based off the operator used
        switch(operator.toLowerCase()){
            case "equals":
                result = equals(data, column, value);
                break;
            case "not_equals":
                result = notEquals(data, column, value);
                break;
            case "contains":
                result = contains(data, column, value);
                break;
            case "greater_than":
                result = greaterThan(data, column, value);
                break;
            case "less_than":
                result = lessThan(data, column, value);
                break;
            case "greater_than_equals":
                result = greaterThanEquals(data, column, value);
                break;
            case "less_than_equals":
                result = lessThanEquals(data, column, value);
                break;
            default:
                System.out.println("Invalid Filter operator: " + operator);
                result = data;
        }
        
        System.out.println("Output data size: " + result.size());
        return result;
    }


    //Methods for each filtering operator
    public List<Map<String, Object>> equals(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);
                return cellValue != null && cellValue.toString().equals(value);
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> notEquals(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);
                return cellValue == null || !cellValue.toString().equals(value);
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> contains(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);
                return cellValue != null && cellValue.toString().toLowerCase()
                    .contains(value.toLowerCase());
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> greaterThan(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);

                if(cellValue == null) return false;
                try{
                    double cellNum = Double.parseDouble(cellValue.toString());
                    double valueNum = Double.parseDouble(value);
                    return cellNum > valueNum;
                }catch(NumberFormatException e){
                    System.out.println("Non-numeric comparison attempted.");
                    return false;
                }
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> lessThan(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);

                if(cellValue == null) return false;
                try{
                    double cellNum = Double.parseDouble(cellValue.toString());
                    double valueNum = Double.parseDouble(value);
                    return cellNum < valueNum;
                }catch(NumberFormatException e){
                    System.out.println("Non-numeric comparison attempted.");
                    return false;
                }
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> lessThanEquals(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);

                if(cellValue == null) return false;
                try{
                    double cellNum = Double.parseDouble(cellValue.toString());
                    double valueNum = Double.parseDouble(value);
                    return cellNum <= valueNum;
                }catch(NumberFormatException e){
                    System.out.println("Non-numeric comparison attempted.");
                    return false;
                }
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> greaterThanEquals(List<Map<String, Object>> data, String column, String value) {
        return data.stream()
            .filter(row -> {
                Object cellValue = row.get(column);

                if(cellValue == null) return false;
                try{
                    double cellNum = Double.parseDouble(cellValue.toString());
                    double valueNum = Double.parseDouble(value);
                    return cellNum >= valueNum;
                }catch(NumberFormatException e){
                    System.out.println("Non-numeric comparison attempted.");
                    return false;
                }
            })
            .collect(Collectors.toList());
    }


}

