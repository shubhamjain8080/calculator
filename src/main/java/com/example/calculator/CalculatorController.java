package com.example.calculator;

import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class CalculatorController {

  public static final String INVALID_QUERY = "Invalid Query";

  @MessageMapping("/calculate")
  @SendTo("/topic/calculationResult")
  public CalculationResult calculateResult(CalculationQuery query) {
    String result;

    try {
      result = String.valueOf(new ExpressionBuilder(query.getQuery()).build().evaluate());
    } catch (Exception e) {
      result = INVALID_QUERY;
    }

    return new CalculationResult(
        HtmlUtils.htmlEscape(query.getQuery()), HtmlUtils.htmlEscape(result));
  }
}
