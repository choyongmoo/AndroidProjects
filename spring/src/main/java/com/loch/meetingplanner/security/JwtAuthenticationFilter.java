package com.loch.meetingplanner.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final List<String> WHITELIST = List.of(
      "/api/hello",
      "/api/users/login",
      "/api/users/signup",
      "/ws/");

  private final JwtTokenProvider jwtTokenProvider;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String path = request.getRequestURI();

    // 화이트리스트에 포함된 경로는 필터 통과
    if (WHITELIST.stream().anyMatch(path::startsWith)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = resolveToken(request);
    if (token != null && jwtTokenProvider.validateToken(token)) {
      String username = jwtTokenProvider.getUserId(token);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
          Collections.emptyList());

      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
