package com.example.calculator;

public class CalculationResult {
  private String query;
  private String result;

  public CalculationResult(String query, String result) {
    this.query = query;
    this.result = result;
  }

  public String getQuery() {
    return query;
  }

  public String getResult() {
    return result;
  }
}
