package com.demo.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends HttpFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            String ip = getIp(req);
            Bucket bucket = buckets.computeIfAbsent(ip, k -> createBucket());
            if (bucket.tryConsume(1)) {
                chain.doFilter(req, res);
            } else {
                res.setStatus(429);
                res.setContentType("application/json");
                res.getWriter().write("{\"message\":\"Demasiadas solicitudes, inténtalo más tarde\"}");
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    private String getIp(HttpServletRequest req) {
        String xf = req.getHeader("X-Forwarded-For");
        if (xf == null) return req.getRemoteAddr();
        return xf.split(",")[0].trim();
    }
}
