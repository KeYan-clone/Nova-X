package com.novax.common.security.util;

import com.novax.common.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Utility
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /**
     * 生成 Token（默认有效期）
     */
    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, subject, jwtProperties.getExpiration());
    }

    /**
     * 生成 Token（自定义有效期）
     */
    public String generateToken(Map<String, Object> claims, long expireSeconds) {
        return generateToken(claims, null, expireSeconds);
    }

    /**
     * 生成 Token
     */
    public String generateToken(Map<String, Object> claims, String subject, long expireSeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expireSeconds);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(jwtProperties.getIssuer())
                .audience().add(jwtProperties.getAudience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析 Token
     */
    public Map<String, Object> parseToken(String token) {
        String rawToken = resolveToken(token);
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(rawToken)
                .getPayload();
        return new HashMap<>(claims);
    }

    /**
     * 校验 Token
     */
    public boolean validateToken(String token) {
        try {
            String rawToken = resolveToken(token);
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(rawToken)
                    .getPayload();
            return !isTokenExpired(claims.getExpiration());
        } catch (Exception ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * 获取 Token 中的 subject
     */
    public String getSubject(String token) {
        String rawToken = resolveToken(token);
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(rawToken)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        Object userId = claims.get("userId");
        return userId != null ? userId.toString() : null;
    }

    /**
     * 从 Token 中获取指定的 claim
     */
    public <T> T getClaimFromToken(String token, String claimKey, Class<T> clazz) {
        Map<String, Object> claims = parseToken(token);
        Object value = claims.get(claimKey);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        // 尝试类型转换
        if (clazz == String.class) {
            return clazz.cast(value.toString());
        }
        if (clazz == Long.class && value instanceof Number) {
            return clazz.cast(((Number) value).longValue());
        }
        if (clazz == Integer.class && value instanceof Number) {
            return clazz.cast(((Number) value).intValue());
        }
        return null;
    }

    /**
     * 获取所有 claims
     */
    public Claims getAllClaims(String token) {
        String rawToken = resolveToken(token);
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(rawToken)
                .getPayload();
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration != null && expiration.before(new Date());
    }

    private String resolveToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Token is empty");
        }
        String prefix = jwtProperties.getTokenPrefix();
        if (StringUtils.hasText(prefix) && token.startsWith(prefix)) {
            return token.substring(prefix.length()).trim();
        }
        return token.trim();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            keyBytes = sha256(keyBytes);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate JWT signing key", ex);
        }
    }
}
