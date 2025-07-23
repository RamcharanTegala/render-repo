package com.example.quoteapi.controller;

import com.example.quoteapi.service.QuoteService;
import com.example.quoteapi.ratelimiter.RateLimiter;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
public class QuoteController {

    private final QuoteService quoteService;
    private final RateLimiter rateLimiter;

    public QuoteController(QuoteService quoteService, RateLimiter rateLimiter) {
        this.quoteService = quoteService;
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/quote")
    public ResponseEntity<?> getQuote(HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        if (!rateLimiter.isAllowed(ip)) {
            long waitTime = rateLimiter.getRetryAfter(ip);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Rate limit exceeded. Try again in " + waitTime + " seconds."));
        }

        return ResponseEntity.ok(Map.of("quote", quoteService.getRandomQuote()));
    }
}