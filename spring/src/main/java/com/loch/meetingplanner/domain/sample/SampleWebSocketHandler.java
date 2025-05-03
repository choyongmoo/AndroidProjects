package com.loch.meetingplanner.domain.sample;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SampleWebSocketHandler implements WebSocketHandler {

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // Handle connection established event
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    // Handle text message event
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    // Handle transport error event
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    // Handle connection closed event
  }

  @Override
  public boolean supportsPartialMessages() {
    return false; // Return true if partial messages are supported
  }

}
