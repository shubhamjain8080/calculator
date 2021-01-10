package com.example.calculator;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class CalculatorController {

  @MessageMapping("/calculate")
  @SendTo("/topic/calculationResult")
  public CalculationResult calculateResult(CalculationQuery query) {
    return new CalculationResult(
        HtmlUtils.htmlEscape(query.getQuery()),
        HtmlUtils.htmlEscape(CalculatorUtil.resolveQuery(query.getQuery())));
  }
}
