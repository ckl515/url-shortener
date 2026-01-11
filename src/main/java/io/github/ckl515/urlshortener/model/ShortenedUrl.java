package io.github.ckl515.urlshortener.model;

import java.time.Instant;
import java.util.Objects;

public class ShortenedUrl {
    private final String shortCode;
    private final String originalUrl;
    private final Instant createdAt;
    private long accessCount;

    public ShortenedUrl(String shortCode, String originalUrl) {
        this.shortCode = Objects.requireNonNull(shortCode, "Short code cannot be null");
        this.originalUrl = Objects.requireNonNull(originalUrl, "Original URL cannot be null");
        this.createdAt = Instant.now();
        this.accessCount  = 0;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShortenedUrl)) {
            return false;
        }
        ShortenedUrl thatUrl = (ShortenedUrl) o;
        return shortCode.equals(thatUrl.shortCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortCode);
    }
}
