package com.example.quoteapi.ratelimiter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterTest {

    @Test
    void testAllowsFiveRequests() {
        RateLimiter limiter = new RateLimiter();
        String ip = "127.0.0.1";
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.isAllowed(ip));
        }
        assertFalse(limiter.isAllowed(ip));
    }

    @Test
    void testRetryAfterReturnsPositiveValue() {
        RateLimiter limiter = new RateLimiter();
        String ip = "127.0.0.2";
        for (int i = 0; i < 5; i++) {
            limiter.isAllowed(ip);
        }
        long waitTime = limiter.getRetryAfter(ip);
        assertTrue(waitTime > 0);
    }
}