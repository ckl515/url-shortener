package io.github.ckl515.urlshortener.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Random;

public class ShortCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ShortCodeGenerator.class);
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final Random random = new SecureRandom();

    public String generate() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        logger.debug("Generated short code: {}", code);
        return code.toString();
    }
}
