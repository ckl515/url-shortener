package io.github.ckl515.urlshortener.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlValidator {
    private static final Logger logger = LoggerFactory.getLogger(UrlValidator.class);
    private static final Pattern VALID_SCHEMES = Pattern.compile("^https?$", Pattern.CASE_INSENSITIVE);
    private static final int MAX_URL_LENGTH = 2048; // max length of URL for Google Chrome

    public void validate(String url) throws IllegalArgumentException {
        logger.debug("Validating URL: {}", url);

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        if (url.length() > MAX_URL_LENGTH) {
            throw new IllegalArgumentException("URL exceeds maximum length of " + MAX_URL_LENGTH);
        }

        try {
            URL urlObject = new URL(url);

            if (!VALID_SCHEMES.matcher(urlObject.getProtocol()).matches()) {
                logger.warn("Invalid protocol rejected: {}", urlObject.getProtocol());
                throw new IllegalArgumentException("URL must use HTTP or HTTPS protocol");
            }

            urlObject.toURI(); // convert to URI object to enforce stricter rules

            if (urlObject.getHost() == null || urlObject.getHost().isEmpty()) {
                throw new IllegalArgumentException("URL must have a valid host");
            }

            logger.debug("URL validated successfully");

        } catch (MalformedURLException e) {
            logger.warn("Invalid URL format: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid URL format: " + e.getMessage());
        } catch (URISyntaxException e) {
            logger.warn("Invalid URI syntax: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid URI syntax: " + e.getMessage());
        }
    }
}