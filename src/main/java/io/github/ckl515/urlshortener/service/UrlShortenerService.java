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

    public ShortenedUrl get(String shortCode) {
        ShortenedUrl url = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("Short code not found: " + shortCode));

        logger.info("Retrieved URL: {}", shortCode);
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
}
