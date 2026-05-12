package com.canteen.gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET = "smart-canteen-secret-key-32bytes!!";

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static Map<String, String> parseToken(String token) {
        var claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Map.of("userId", claims.getSubject(),
                       "role", claims.get("role", String.class));
    }
}
