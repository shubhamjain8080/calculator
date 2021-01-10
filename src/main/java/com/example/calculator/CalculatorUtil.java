package com.example.calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class CalculatorUtil {

  public static final String REGEX_FOR_VALID_QUERY =
      "^(\\()*[-]?(\\d+(\\.\\d*)?|\\.\\d+)([-+*/]{1}(\\()*[-]?(\\d+(\\.\\d*)?|\\.\\d+)(\\))*)*(\\))*";

  public static final String BRACKET_REGEX =
      "((\\()[-]?(\\d+(\\.\\d*)?|\\.\\d+)([-+*/]{1}[-]?(\\d+(\\.\\d*)?|\\.\\d+))*(\\)))+";
  public static final String SUBTRACT_REGEX =
      "[-]?(\\d+(\\.\\d*)?|\\.\\d+)([-]{1}[-]?(\\d+(\\.\\d*)?|\\.\\d+))+";
  public static final String ADDITION_REGEX =
      "[-]?(\\d+(\\.\\d*)?|\\.\\d+)([+]{1}[-]?(\\d+(\\.\\d*)?|\\.\\d+))+";
  public static final String DIVISION_REGEX =
      "[-]?(\\d+(\\.\\d*)?|\\.\\d+)([/]{1}[-]?(\\d+(\\.\\d*)?|\\.\\d+))+";
  public static final String MULTIPLICATION_REGEX =
      "[-]?(\\d+(\\.\\d*)?|\\.\\d+)([*]{1}[-]?(\\d+(\\.\\d*)?|\\.\\d+))+";

  public static String resolveQuery(String completeQuery) {

    if (!isValidQuery(completeQuery)) {
      return "Invalid Input";
    }

    if (completeQuery.startsWith("(") && completeQuery.endsWith(")")) {
      completeQuery = completeQuery.substring(1);
      completeQuery = completeQuery.substring(0, completeQuery.length() - 1);
    }
    completeQuery = resolveBracketQueries(completeQuery);
    completeQuery = resolveDivisionQueries(completeQuery);
    completeQuery = resolveMultiplicationQueries(completeQuery);
    completeQuery = resolveAdditionQueries(completeQuery);
    completeQuery = resolveSubtractionQueries(completeQuery);
    if (completeQuery.contains("(")) {
      completeQuery = completeQuery.replace("(", "");
      completeQuery = completeQuery.replace(")", "");
      completeQuery = resolveQuery(completeQuery);
    }
    return completeQuery;
  }

  private static boolean isValidQuery(String completeQuery) {
    return Pattern.matches(REGEX_FOR_VALID_QUERY, completeQuery)
        && StringUtils.countMatches(completeQuery, '(')
            == StringUtils.countMatches(completeQuery, ')');
  }

  private static String resolveBracketQueries(String completeQuery) {

    Pattern p = Pattern.compile(BRACKET_REGEX);
    Matcher matcher = p.matcher(completeQuery);
    while (matcher.find()) {
      String group = matcher.group();
      completeQuery = completeQuery.replace(matcher.group(), resolveQuery(group));
    }

    return completeQuery;
  }

  private static String resolveSubtractionQueries(String completeQuery) {
    Pattern p = Pattern.compile(SUBTRACT_REGEX);
    Matcher matcher = p.matcher(completeQuery);
    while (matcher.find()) {
      completeQuery = completeQuery.replace(matcher.group(), subtract(matcher.group()));
    }
    return completeQuery;
  }

  private static String resolveAdditionQueries(String completeQuery) {
    Pattern p = Pattern.compile(ADDITION_REGEX);
    Matcher matcher = p.matcher(completeQuery);
    while (matcher.find()) {
      completeQuery = completeQuery.replace(matcher.group(), add(matcher.group()));
    }
    return completeQuery;
  }

  private static String resolveMultiplicationQueries(String completeQuery) {
    String regex = MULTIPLICATION_REGEX;

    Pattern p = Pattern.compile(regex);
    Matcher matcher = p.matcher(completeQuery);
    while (matcher.find()) {
      completeQuery = completeQuery.replace(matcher.group(), multiply(matcher.group()));
    }
    return completeQuery;
  }

  private static String resolveDivisionQueries(String completeQuery) {
    Pattern p = Pattern.compile(DIVISION_REGEX);
    Matcher matcher = p.matcher(completeQuery);
    while (matcher.find()) {
      completeQuery = completeQuery.replace(matcher.group(), divide(matcher.group()));
    }
    return completeQuery;
  }

  private static String add(String expression) {
    String[] split = expression.split("\\+");
    Double result = Double.valueOf(split[0]);
    for (int i = 1; i < split.length; i++) {
      result = result + Double.parseDouble(split[i]);
    }
    return String.valueOf(result);
  }

  private static String subtract(String expression) {
    expression = expression.replace("--", "-NEGATIVE");
    String[] split = expression.split("-");
    Double result = Double.valueOf(split[0].replace("NEGATIVE", "-"));
    for (int i = 1; i < split.length; i++) {
      result = result - Double.parseDouble(split[i].replace("NEGATIVE", "-"));
    }
    return String.valueOf(result);
  }

  private static String multiply(String expression) {
    String[] split = expression.split("\\*");
    Double result = Double.valueOf(split[0]);
    for (int i = 1; i < split.length; i++) {
      result = result * Double.parseDouble(split[i]);
    }
    return String.valueOf(result);
  }

  private static String divide(String expression) {
    String[] split = expression.split("/");
    Double result = Double.valueOf(split[0]);
    for (int i = 1; i < split.length; i++) {
      result = result / Double.parseDouble(split[i]);
    }
    return String.valueOf(result);
  }
}
