package io.github.ckl515.urlshortener.model;

import java.util.Objects;

public class ShortenedUrl {
    private final String shortCode;
    private final String originalUrl;

    public ShortenedUrl(String shortCode, String originalUrl) {
        this.shortCode = Objects.requireNonNull(shortCode, "Short code cannot be null");
        this.originalUrl = Objects.requireNonNull(originalUrl, "Original URL cannot be null");
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
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
