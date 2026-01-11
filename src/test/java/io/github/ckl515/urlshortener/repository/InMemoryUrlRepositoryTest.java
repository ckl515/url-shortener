package io.github.ckl515.urlshortener.repository;

import io.github.ckl515.urlshortener.model.ShortenedUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUrlRepositoryTest {
    private UrlRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUrlRepository();
    }

    @Test
    void saveStoresUrl() {
        ShortenedUrl url = new ShortenedUrl("abc123", "https://example.com");
        repository.save(url);

        Optional<ShortenedUrl> found = repository.findByShortCode("abc123");
        assertTrue(found.isPresent());
        assertEquals("https://example.com", found.get().getOriginalUrl());
    }

    @Test
    void findByShortCodeReturnsEmptyIfNonExistent() {
        Optional<ShortenedUrl> found = repository.findByShortCode("nonexistent");
        assertFalse(found.isPresent());
    }

    @Test
    void existsReturnsTrueForSavedCode() {
        ShortenedUrl url = new ShortenedUrl("abc123", "https://example.com");
        repository.save(url);

        assertTrue(repository.exists("abc123"));
    }

    @Test
    void existsReturnsFalseIfNonExistent() {
        assertFalse(repository.exists("nonexistent"));
    }

    @Test
    void findAllReturnsEmptyCollectionForEmptyRepo() {
        Collection<ShortenedUrl> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void findAllReturnsAllUrls() {
        repository.save(new ShortenedUrl("abc123", "https://example.com"));
        repository.save(new ShortenedUrl("xyz789", "https://google.com"));

        Collection<ShortenedUrl> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void saveNullUrlThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }
}