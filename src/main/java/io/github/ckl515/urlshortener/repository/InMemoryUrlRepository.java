package io.github.ckl515.urlshortener.repository;

import io.github.ckl515.urlshortener.model.ShortenedUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryUrlRepository implements UrlRepository {
    private final Map<String, ShortenedUrl> storage = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(InMemoryUrlRepository.class);

    @Override
    public void save(ShortenedUrl url) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        storage.put(url.getShortCode(), url);
        logger.debug("Saved URL with short code: {}", url.getShortCode());
    }

    @Override
    public Optional<ShortenedUrl> findByShortCode(String shortCode) {
        return Optional.ofNullable(storage.get(shortCode));
    }

    @Override
    public boolean exists(String shortCode) {
        return storage.containsKey(shortCode);
    }

    @Override
    public Collection<ShortenedUrl> findAll() {
        return Collections.unmodifiableCollection(storage.values());
    }
}
