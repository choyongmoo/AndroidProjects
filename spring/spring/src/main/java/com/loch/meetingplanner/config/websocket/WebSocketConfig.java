package com.loch.meetingplanner.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.loch.meetingplanner.domain.sample.SampleHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final JwtHandshakeInterceptor interceptor;
  private final SampleHandler sampleHandler;

  public WebSocketConfig(SampleHandler sampleHandler,
      JwtHandshakeInterceptor interceptor) {
    this.sampleHandler = sampleHandler;
    this.interceptor = interceptor;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(sampleHandler, "/ws/sample")
        // .addInterceptors(interceptor)
        .setAllowedOrigins("*");
  }
}
