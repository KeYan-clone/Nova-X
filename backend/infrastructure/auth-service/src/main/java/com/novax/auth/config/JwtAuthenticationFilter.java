package com.novax.auth.config;

import com.novax.common.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 拦截请求并验证 JWT Token
 *
 * @author Nova-X
 * @since 2026-01-25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    try {
      // 从请求头获取 Token
      String token = extractToken(request);

      if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
        // 从 Token 中提取用户信息
        String userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getClaimFromToken(token, "username", String.class);
        String userType = jwtUtil.getClaimFromToken(token, "userType", String.class);

        // 创建认证对象
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userId,
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType)));

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 设置到 Security 上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("JWT 认证成功: userId={}, username={}", userId, username);
      }
    } catch (Exception e) {
      log.error("JWT 认证失败: {}", e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  /**
   * 从请求头提取 Token
   */
  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
