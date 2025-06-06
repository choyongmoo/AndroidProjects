package com.loch.meetingplanner.domain.sample;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SampleHandler extends TextWebSocketHandler {

  private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

  public SampleHandler(SampleService sampleService) {
    // 주기적으로 메시지 전송하는 스케줄러
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      String message = sampleService.getMessage().getMessage();
      sendMessageToAll(message);
    }, 0, 100, TimeUnit.MILLISECONDS);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
  }

  private void sendMessageToAll(String message) {
    for (WebSocketSession session : sessions) {
      if (session.isOpen()) {
        try {
          session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
