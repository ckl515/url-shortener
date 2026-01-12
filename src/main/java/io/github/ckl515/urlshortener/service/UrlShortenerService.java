package io.github.ckl515.urlshortener.service;

import io.github.ckl515.urlshortener.generator.ShortCodeGenerator;
import io.github.ckl515.urlshortener.model.ShortenedUrl;
import io.github.ckl515.urlshortener.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class UrlShortenerService {
    private static final int MAX_COLLISION_ATTEMPTS = 5;
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    private final UrlRepository repository;
    private final ShortCodeGenerator codeGenerator;

    public UrlShortenerService(UrlRepository repository, ShortCodeGenerator codeGenerator) {
        this.repository = repository;
        this.codeGenerator = codeGenerator;
    }

    public String shorten(String originalUrl) {
        String shortCode = generateUniqueCode();
        ShortenedUrl url = new ShortenedUrl(shortCode, originalUrl);
        repository.save(url);
        logger.info("Shortened URL: {} -> {}", shortCode, originalUrl);
        return shortCode;
    }

    public String shorten(String originalUrl, String customCode) {
        validateCustomCode(customCode);

        if (repository.exists(customCode)) {
            throw new IllegalArgumentException("Custom code '" + customCode + "' is already in use");
        }

        ShortenedUrl url = new ShortenedUrl(customCode, originalUrl);
        repository.save(url);
        logger.info("Shortened URL with custom code: {} -> {}", customCode, originalUrl);
        return customCode;
    }

    public ShortenedUrl get(String shortCode) {
        ShortenedUrl url = findShortenedUrl(shortCode);
        url.incrementAccessCount();
        logger.info("Retrieved URL: {}", shortCode);
        return url;
    }

    public ShortenedUrl getStats(String shortCode) {
        ShortenedUrl url = findShortenedUrl(shortCode);
        logger.debug("Retrieved stats for: {}", shortCode);
        return url;
    }

    public Collection<ShortenedUrl> listAll() {
        return repository.findAll();
    }

    private String generateUniqueCode() {
        for (int attempt = 0; attempt < MAX_COLLISION_ATTEMPTS; attempt++) {
            String code = codeGenerator.generate();
            if (!repository.exists(code)) {
                return code;
            }
            logger.warn("Collision detected for short code: {}, retrying (attempt {}/{})",
                    code, attempt + 1, MAX_COLLISION_ATTEMPTS);
        }
        throw new RuntimeException("Failed to generate unique code after " + MAX_COLLISION_ATTEMPTS);
    }

    // Validates that custom code is composed of only valid characters
    private void validateCustomCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Custom code cannot be empty");
        }
        if (code.length() > 6) {
            throw new IllegalArgumentException("Custom code too long (max 6 characters)");
        }
        if (!code.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Custom code can only contain letters and numbers");
        }
    }

    // Finds the ShortenedUrl by the short code, if not throw exception
    private ShortenedUrl findShortenedUrl(String code) {
        return repository.findByShortCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Short code not found: " + code));
    }
}
