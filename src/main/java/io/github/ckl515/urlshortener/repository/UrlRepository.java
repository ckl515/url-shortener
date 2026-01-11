package io.github.ckl515.urlshortener.repository;

import io.github.ckl515.urlshortener.model.ShortenedUrl;

import java.util.Collection;
import java.util.Optional;

public interface UrlRepository {
    void save(ShortenedUrl url);

    Optional<ShortenedUrl> findByShortCode(String shortCode);

    boolean exists(String shortCode);

    Collection<ShortenedUrl> findAll();
}
