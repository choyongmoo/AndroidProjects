package com.loch.meetingplanner.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.loch.meetingplanner.security.JwtTokenProvider;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    try {
      // 쿼리 파라미터에서 토큰 추출
      String query = request.getURI().getQuery(); // ex: token=xxxx
      if (query == null || !query.startsWith("token="))
        return false;

      String token = query.substring("token=".length());

      // JWT 검증 및 사용자 ID 추출
      if (!jwtTokenProvider.validateToken(token))
        return false;

      String userId = jwtTokenProvider.getUsername(token);
      attributes.put("userId", userId); // WebSocketSession에 저장

      return true; // 연결 허용
    } catch (Exception e) {
      return false; // 연결 거부
    }
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {
    // Add any custom logic here if needed after the handshake
  }

}
