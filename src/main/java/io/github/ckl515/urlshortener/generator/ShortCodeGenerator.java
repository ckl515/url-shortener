package io.github.ckl515.urlshortener.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Random;

public class ShortCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ShortCodeGenerator.class);
    private static final char[] CHARS;
    private static final int CODE_LENGTH = 6;
    private final Random random = new SecureRandom();

    // set up all valid alphabets and digits
    static {
        CHARS = new char[62];
        int pos = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            CHARS[pos++] = c;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            CHARS[pos++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            CHARS[pos++] = c;
        }
    }

    public String generate() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARS[random.nextInt(CHARS.length)]);
        }
        logger.debug("Generated short code: {}", code);
        return code.toString();
    }
}
