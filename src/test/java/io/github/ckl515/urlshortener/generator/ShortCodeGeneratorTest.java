package io.github.ckl515.urlshortener.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShortCodeGeneratorTest {
    ShortCodeGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ShortCodeGenerator();
    }

    @Test
    void generateCreatesNonEmptyCode() {
        String code = generator.generate();

        assertNotNull(code);
        assertFalse(code.isEmpty());
    }

    @Test
    void generateCreatesCorrectLength() {
        String code = generator.generate();

        assertEquals(6, code.length());
    }

    @Test
    void generateCreatesUniqueCodes() {
        Set<String> codes = generateCodes(100);

        assertEquals(100, codes.size());
    }

    @Test
    void generateUsesOnlyValidCharacters() {
        Set<String> codes = generateCodes(100);
        for (String code : codes) {
            assertTrue(code.matches("[a-zA-Z0-9]+"));
        }
    }

    private Set<String> generateCodes(int count) {
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < count; i++) {
            codes.add(generator.generate());
        }
        return codes;
    }
}