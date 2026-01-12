package io.github.ckl515.urlshortener.service;

import io.github.ckl515.urlshortener.generator.ShortCodeGenerator;
import io.github.ckl515.urlshortener.model.ShortenedUrl;
import io.github.ckl515.urlshortener.repository.InMemoryUrlRepository;
import io.github.ckl515.urlshortener.validator.UrlValidator;
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
                new ShortCodeGenerator(),
                new UrlValidator()
        );
    }

    // helper methods
    private String shortenUrl(String url) {
        return service.shorten(url);
    }

    private String shortenUrl(String url, String customCode) {
        return service.shorten(url, customCode);
    }

    private ShortenedUrl getUrl(String code) {
        return service.get(code);
    }

    private void assertInvalidShorten(String url) {
        assertThrows(IllegalArgumentException.class,
                () -> shortenUrl(url));
    }

    private void assertInvalidShorten(String url, String customCode) {
        assertThrows(IllegalArgumentException.class,
                () -> shortenUrl(url, customCode));
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

    @Test
    void shortenInvalidUrlThrowsException() {
        assertInvalidShorten("not-a-url");
    }

    @Test
    void shortenFtpUrlThrowsException() {
        assertInvalidShorten("ftp://example.com");
    }

    @Test
    void shortenNoProtocolThrowsException() {
        assertInvalidShorten("example.com");
    }

    @Test
    void shortenValidUrlWithPathSucceeds() {
        String code = shortenUrl("https://example.com/path/to/page");
        assertNotNull(code);
    }

    // tests with preloaded example URL
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

        @Nested
        class CustomCodeTests {
            private final String customCode = "myLink";

            @Test
            void customCodeUsesProvidedCode() {
                String code = shortenUrl(exampleUrl, customCode);
                assertEquals(customCode, code);
            }

            @Test
            void duplicateCustomCodeThrowsException() {
                shortenUrl(exampleUrl, customCode);
                assertInvalidShorten("https://www.other.com", customCode);
            }

            @Test
            void invalidCustomCodeThrowsException() {
                assertInvalidShorten("https://example.com", "my Link"); // space invalid
            }

            @Test
            void emptyCustomCodeThrowsException() {
                assertInvalidShorten("https://example.com", "");
            }

            @Test
            void tooLongCustomCodeThrowsException() {
                String longCode = "a".repeat(10);
                assertInvalidShorten("https://example.com", longCode);
            }
        }
    }
}
