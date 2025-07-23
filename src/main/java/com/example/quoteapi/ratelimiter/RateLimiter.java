package com.example.quoteapi.ratelimiter;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW_MS = 60 * 1000;

    private final ConcurrentHashMap<String, List<Long>> requestLogs = new ConcurrentHashMap<>();

    public synchronized boolean isAllowed(String ip) {
        long currentTime = System.currentTimeMillis();
        requestLogs.putIfAbsent(ip, new ArrayList<>());

        List<Long> timestamps = requestLogs.get(ip);
        timestamps.removeIf(t -> currentTime - t > TIME_WINDOW_MS);

        if (timestamps.size() < MAX_REQUESTS) {
            timestamps.add(currentTime);
            return true;
        }

        return false;
    }

    public long getRetryAfter(String ip) {
        List<Long> timestamps = requestLogs.get(ip);
        if (timestamps == null || timestamps.isEmpty()) return 0;

        long first = timestamps.get(0);
        return (TIME_WINDOW_MS - (System.currentTimeMillis() - first)) / 1000;
    }
}