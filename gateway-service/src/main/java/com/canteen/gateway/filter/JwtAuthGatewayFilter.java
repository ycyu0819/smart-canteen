package com.canteen.gateway.filter;

import com.canteen.gateway.util.JwtUtils;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class JwtAuthGatewayFilter implements GlobalFilter, Ordered {

    private static final List<String> WHITELIST = List.of(
            "/api/user/register", "/api/user/login", "/api/user/sms", "/api/user/token",
            "/api/queue/screen");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (WHITELIST.stream().anyMatch(path::startsWith) || path.startsWith("/ws/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7);
            var claims = JwtUtils.parseToken(token);
            exchange = exchange.mutate()
                    .request(r -> r.header("X-User-Id", claims.get("userId"))
                                   .header("X-User-Role", claims.get("role")))
                    .build();
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() { return -1; }
}
