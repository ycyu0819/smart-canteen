package com.canteen.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET = "smart-canteen-secret-key-32bytes!!";
    private static final long ACCESS_EXPIRATION = 7200_000L;
    private static final long REFRESH_EXPIRATION = 604800_000L;

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateAccessToken(Long userId, String role) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public static String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean isExpired(String token) {
        try {
            parseToken(token);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    public static String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }
}
