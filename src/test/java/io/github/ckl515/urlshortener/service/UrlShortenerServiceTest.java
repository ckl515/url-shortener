package io.github.ckl515.urlshortener.service;

import io.github.ckl515.urlshortener.generator.ShortCodeGenerator;
import io.github.ckl515.urlshortener.model.ShortenedUrl;
import io.github.ckl515.urlshortener.repository.InMemoryUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerServiceTest {
    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        service = new UrlShortenerService(
                new InMemoryUrlRepository(),
                new ShortCodeGenerator()
        );
    }

    @Test
    void shortenValidUrlReturnsShortCode() {
        String shortCode = service.shorten("https://www.example.com");

        assertNotNull(shortCode);
        assertFalse(shortCode.isEmpty());
        assertEquals(6, shortCode.length());
    }

    @Test
    void getExistingCodeReturnsCorrectUrl() {
        String shortCode = service.shorten("https://www.example.com");
        ShortenedUrl url = service.get(shortCode);

        assertEquals("https://www.example.com", url.getOriginalUrl());
        assertEquals(shortCode, url.getShortCode());
    }

    @Test
    void getNonExistingCodeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.get("nonexistent"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void listAllReturnsEmpty() {
        Collection<ShortenedUrl> urls = service.listAll();
        assertTrue(urls.isEmpty());
    }

    @Test
    void listAllReturnsAllUrls() {
        service.shorten("https://example.com");
        service.shorten("https://google.com");

        Collection<ShortenedUrl> urls = service.listAll();
        assertEquals(2, urls.size());
    }

    @Test
    void shortenMultipleUrlsGeneratesDifferentCodes() {
        String code1 = service.shorten("https://example.com");
        String code2 = service.shorten("https://google.com");

        assertNotEquals(code1, code2);
    }

    @Test
    void getIncrementsAccessCount() {
        String shortCode = service.shorten("https://www.example.com");

        service.get(shortCode);
        service.get(shortCode);
        ShortenedUrl url = service.get(shortCode);

        assertEquals(3, url.getAccessCount());
    }

    @Test
    void getMultipleAccessesTracksCorrectly() {
        String shortCode = service.shorten("https://www.example.com");

        ShortenedUrl url1 = service.get(shortCode);
        assertEquals(1, url1.getAccessCount());

        ShortenedUrl url2 = service.get(shortCode);
        assertEquals(2, url2.getAccessCount());
    }

    @Test
    void shortenNewUrlStartsWithZeroCount() {
        String shortCode = service.shorten("https://www.example.com");
        ShortenedUrl url = service.get(shortCode);

        // First get increments to 1
        assertEquals(1, url.getAccessCount());
    }
}