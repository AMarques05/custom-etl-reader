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
        System.out.println("Filters: " + filters);

        if(csvData == null || csvData.isEmpty()) {
            System.out.println("No data available for filtering.");
            return List.of();
        }

        List<Map<String, Object>> filteredData = new ArrayList<>(csvData);

        // Filter data based on the filter options
        for(FilterDto filter : filters) {
            if (filter.getColumn() == null || filter.getValue() == null || filter.getOperator() == null) {
                System.out.println("Skipping invalid filter: " + filter);
                continue;
            }
            
            String column = filter.getColumn();
            String value = filter.getValue();
            String operator = filter.getOperator();

            filteredData = applyFilter(filteredData, column, value, operator);
        }


        // Filter data based off of the columns
        filteredData = filteredData.stream()
            .map(row -> selectedColumns.stream()
                .filter(row::containsKey)
                .collect(Collectors.toMap(
                    column -> column,
                    row::get
                )))
            .collect(Collectors.toList());

        return filteredData;
    }

    private List<Map<String, Object>> applyFilter(List<Map<String, Object>> data, String column, String value, String operator) {

        //Method to check which operator was used then perform filtering based off the operator used
        switch(operator.toLowerCase()){
            case "equals":
                data = equals(data, column, value);
                break;
            case "not_equals":
                data = notEquals(data, column, value);
                break;
            case "contains":
                data = contains(data, column, value);
                break;
            case "greater_than":
                data = greaterThan(data, column, value);
                break;
            case "less_than":
                data = lessThan(data, column, value);
                break;
            default:
                System.out.println("Invalid Filter operator.");
        }
        return data;
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
                return cellValue != null && cellValue.toString()
                    .contains(value.toLowerCase());
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> greaterThan(List<Map<String, Object>> data, String column, String value) {
        return data;
    }

    public List<Map<String, Object>> lessThan(List<Map<String, Object>> data, String column, String value) {
        return data;
    }

}

