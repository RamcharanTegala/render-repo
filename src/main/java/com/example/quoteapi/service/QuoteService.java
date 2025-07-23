package com.example.quoteapi.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class QuoteService {
    private final List<String> quotes = List.of(
        "The only way to do great work is to love what you do. - Steve Jobs",
        "Believe in yourself and all that you are. - Christian D. Larson",
        "Don't watch the clock; do what it does. Keep going. - Sam Levenson"
    );

    public String getRandomQuote() {
        Random rand = new Random();
        return quotes.get(rand.nextInt(quotes.size()));
    }
}