package io.github.ckl515.urlshortener.service;

import io.github.ckl515.urlshortener.generator.ShortCodeGenerator;
import io.github.ckl515.urlshortener.model.ShortenedUrl;
import io.github.ckl515.urlshortener.repository.InMemoryUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    // helper methods
    private String shortenUrl(String url) {
        return service.shorten(url);
    }

    private ShortenedUrl getUrl(String code) {
        return service.get(code);
    }

    @Test
    void listAllReturnsEmpty() {
        Collection<ShortenedUrl> urls = service.listAll();
        assertTrue(urls.isEmpty());
    }

    @Test
    void getNonExistingCodeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> getUrl("nonexistent"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    // tests with preloaded URL
    @Nested
    class WithExampleUrl {
        private String exampleUrl;
        private String exampleShortCode;

        @BeforeEach
        void setUpExampleUrl() {
            exampleUrl = "https://www.example.com";
            exampleShortCode = shortenUrl(exampleUrl);
        }

        @Test
        void shortenValidUrlReturnsShortCode() {
            assertNotNull(exampleShortCode);
            assertFalse(exampleShortCode.isEmpty());
            assertEquals(6, exampleShortCode.length());
        }

        @Test
        void getExistingCodeReturnsCorrectUrl() {
            ShortenedUrl url = getUrl(exampleShortCode);
            assertEquals(exampleUrl, url.getOriginalUrl());
            assertEquals(exampleShortCode, url.getShortCode());
        }

        @Test
        void listAllReturnsAllUrls() {
            shortenUrl("https://google.com");
            Collection<ShortenedUrl> urls = service.listAll();
            assertEquals(2, urls.size());
        }

        @Test
        void shortenMultipleUrlsGeneratesDifferentCodes() {
            String code2 = shortenUrl("https://google.com");
            assertNotEquals(exampleShortCode, code2);
        }

        @Test
        void getIncrementsAccessCount() {
            getUrl(exampleShortCode);
            getUrl(exampleShortCode);
            ShortenedUrl url = getUrl(exampleShortCode);
            assertEquals(3, url.getAccessCount());
        }

        @Test
        void getMultipleAccessesTracksCorrectly() {
            ShortenedUrl url1 = getUrl(exampleShortCode);
            assertEquals(1, url1.getAccessCount());

            ShortenedUrl url2 = getUrl(exampleShortCode);
            assertEquals(2, url2.getAccessCount());
        }

        @Test
        void shortenNewUrlStartsWithZeroCount() {
            ShortenedUrl url = getUrl(exampleShortCode);
            assertEquals(1, url.getAccessCount()); // first get increments to 1
        }
    }
}
