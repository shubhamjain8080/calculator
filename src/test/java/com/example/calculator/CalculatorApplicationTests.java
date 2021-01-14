package com.example.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculatorApplicationTests {

  @LocalServerPort private int port;

  private SockJsClient sockJsClient;

  private WebSocketStompClient webSocketStompClient;

  @BeforeEach
  public void setup() {
    List<Transport> transports = new ArrayList<>();
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    this.sockJsClient = new SockJsClient(transports);

    this.webSocketStompClient = new WebSocketStompClient(sockJsClient);
    this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
  }

  @Test
  public void testGetResultForQuery() {

    StompSessionHandler handler =
        new StompSessionHandlerAdapter() {

          @Override
          public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
            session.subscribe(
                "/topic/calculationResult",
                new StompFrameHandler() {
                  @Override
                  public Type getPayloadType(StompHeaders headers) {
                    return CalculationResult.class;
                  }

                  @Override
                  public void handleFrame(StompHeaders headers, Object payload) {
                    CalculationResult calculationResult = (CalculationResult) payload;
                    assertEquals("6", calculationResult.getResult());
                  }
                });
            session.send("/app/calculate", new CalculationQuery("2*3"));
          }
        };

    this.webSocketStompClient.connect(
        "ws://localhost:{port}/calculator-websocket", handler, this.port);
  }
}
