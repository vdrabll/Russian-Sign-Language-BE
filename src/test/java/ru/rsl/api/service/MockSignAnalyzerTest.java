package ru.rsl.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rsl.api.Model.SignErrorType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockSignAnalyzerTest {

    private MockSignAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new MockSignAnalyzer();
    }

    @Test
    void successWhenUrlLooksNormal() {
        SignAnalysis result = analyzer.analyze("https://cdn.example.com/recordings/ok.mp4");
        assertTrue(result.success());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void failKeywordYieldsWrongHandshape() {
        SignAnalysis result = analyzer.analyze("https://cdn.example.com/recordings/fail-take-1.mp4");
        assertFalse(result.success());
        assertEquals(SignErrorType.WRONG_HANDSHAPE, result.errors().getFirst());
    }

    @Test
    void unclearKeywordYieldsUnclear() {
        SignAnalysis result = analyzer.analyze("https://cdn.example.com/recordings/unclear.mp4");
        assertFalse(result.success());
        assertEquals(SignErrorType.UNCLEAR, result.errors().getFirst());
    }
}
