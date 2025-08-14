package com.example.etl_backend.controller;

public class FilterDto {
    private String column;
    private String operator;
    private String value;

    // Constructors
    public FilterDto() {}

    // Getters and Setters
    public String getColumn() { return column; }
    public void setColumn(String column) { this.column = column; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
