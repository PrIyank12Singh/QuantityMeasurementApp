package com.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseHeadersGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            exchange.getResponse().getHeaders().add("Cross-Origin-Opener-Policy", "same-origin-allow-popups");
            exchange.getResponse().getHeaders().add("Cross-Origin-Embedder-Policy", "require-corp");
            exchange.getResponse().getHeaders().add("X-Content-Type-Options", "nosniff");
            exchange.getResponse().getHeaders().add("X-Frame-Options", "DENY");
            exchange.getResponse().getHeaders().add("X-XSS-Protection", "1; mode=block");
            exchange.getResponse().getHeaders().add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
            exchange.getResponse().getHeaders().add("Cache-Control", "no-cache,no-store,must-revalidate");
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
