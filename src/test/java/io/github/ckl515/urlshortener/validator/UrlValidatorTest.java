package io.github.ckl515.urlshortener.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {
    private UrlValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UrlValidator();
    }

    // helper methods
    private void assertValid(String url) {
        assertDoesNotThrow(() -> validator.validate(url));
    }

    private void assertInvalid(String url) {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(url));
    }

    @Test
    void validHttpUrlPasses() {
        assertValid("http://example.com");
    }

    @Test
    void validHttpsUrlPasses() {
        assertValid("https://example.com");
    }

    @Test
    void validUrlWithPathPasses() {
        assertValid("https://example.com/path/to/page");
    }

    @Test
    void validUrlWithQueryPasses() {
        assertValid("https://example.com?key=value&foo=bar");
    }

    @Test
    void nullUrlThrowsException() {
        assertInvalid(null);
    }

    @Test
    void emptyUrlThrowsException() {
        assertInvalid("");
    }

    @Test
    void invalidUrlThrowsException() {
        assertInvalid("not-a-url");
    }

    @Test
    void otherProtocolThrowsException() {
        assertInvalid("ftp://example.com");
    }

    @Test
    void noProtocolThrowsException() {
        assertInvalid("example.com");
    }

    @Test
    void urlTooLongThrowsException() {
        String longUrl = "https://example.com/" + "a".repeat(2100);
        assertInvalid(longUrl);
    }
}