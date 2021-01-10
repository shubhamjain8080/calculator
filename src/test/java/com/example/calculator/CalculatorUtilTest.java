package com.example.calculator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CalculatorUtilTest {

  @Test
  void emptyValueIsInvalidInput() {
    String query = "";
    Assert.assertEquals("Invalid Input", CalculatorUtil.resolveQuery(query));
  }

  @Test
  void singleValueReturnsSameValue() {
    String query = "5";
    Assert.assertEquals("5", CalculatorUtil.resolveQuery(query));
  }

  @Test
  void queryWithSingleOperatorReturnsCorrectResult() {
    String sumQuery = "5+5";
    String subtractionQuery = "5-3";
    String multiplicationQuery = "5*5";
    String divideQuery = "9/3";
    Assert.assertEquals("10.0", CalculatorUtil.resolveQuery(sumQuery));
    Assert.assertEquals("2.0", CalculatorUtil.resolveQuery(subtractionQuery));
    Assert.assertEquals("25.0", CalculatorUtil.resolveQuery(multiplicationQuery));
    Assert.assertEquals("3.0", CalculatorUtil.resolveQuery(divideQuery));
  }

  @Test
  void divisionAndMultiplicationOperatorsGetResolvedFirstInMultipleOperatorQuery() {
    String complexDivisionQuery = "5+9/3";
    String complexMultiplicationQuery = "5+3*2";
    Assert.assertEquals("8.0", CalculatorUtil.resolveQuery(complexDivisionQuery));
    Assert.assertEquals("11.0", CalculatorUtil.resolveQuery(complexMultiplicationQuery));
  }

  @Test
  void BracketsGetResolvedFirstInMultipleOperatorQuery() {
    String divisionQueryWithBrackets = "5+9/(1+2)";
    String multiplicationQueryWithBrackets = "5+3*(2+4)";
    Assert.assertEquals("8.0", CalculatorUtil.resolveQuery(divisionQueryWithBrackets));
    Assert.assertEquals("23.0", CalculatorUtil.resolveQuery(multiplicationQueryWithBrackets));
  }

  @Test
  void numberWithNegativeSignQueryReturnsCorrectResult() {
    String divisionQueryWithNegativeNumber = "5+9/(-1+2)";
    String multiplicationQueryWithNegativeNumber = "5+3*(-2)";
    String additionQueryWithNegativeNumber = "5+3+(-2)";
    String subtractionQueryWithNegativeNumber = "5+3-(-2)";
    Assert.assertEquals("14.0", CalculatorUtil.resolveQuery(divisionQueryWithNegativeNumber));
    Assert.assertEquals("-1.0", CalculatorUtil.resolveQuery(multiplicationQueryWithNegativeNumber));
    Assert.assertEquals("6.0", CalculatorUtil.resolveQuery(additionQueryWithNegativeNumber));
    Assert.assertEquals("10.0", CalculatorUtil.resolveQuery(subtractionQueryWithNegativeNumber));
  }

  @Test
  void queryWithDecimalReturnsCorrectResult() {
    String divisionQueryWithDecimal = "5+9.9/(-1+2.1)";
    String multiplicationQueryWithDecimal = "5+25.2*(2)";
    Assert.assertEquals("14.0", CalculatorUtil.resolveQuery(divisionQueryWithDecimal));
    Assert.assertEquals("55.4", CalculatorUtil.resolveQuery(multiplicationQueryWithDecimal));
  }

  @Test
  void queryWithMultipleBracketsReturnsCorrectResult() {
    String multipleBracketQuery = "(5*(125/(5*(3+2))))";
    String multipleBracketComplexQuery = "((4*(2+-3*(2+1))*(3+4*4))*(-3)/(5+7))";
    Assert.assertEquals("25.0", CalculatorUtil.resolveQuery(multipleBracketQuery));
    Assert.assertEquals("133.0", CalculatorUtil.resolveQuery(multipleBracketComplexQuery));
  }

  @Test
  void queryWithUnevenNumberOfBracketsReturnsInvalidResult() {
    String unevenBracketsQuery = "5*(25/(3+2)))";
    Assert.assertEquals("Invalid Input", CalculatorUtil.resolveQuery(unevenBracketsQuery));
  }
}
