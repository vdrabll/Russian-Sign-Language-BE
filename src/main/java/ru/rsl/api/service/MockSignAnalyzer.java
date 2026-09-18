package ru.rsl.api.service;

import org.springframework.stereotype.Component;
import ru.rsl.api.Model.SignErrorType;

@Component
public class MockSignAnalyzer {

    public SignAnalysis analyze(String recordingUrl) {
        if (recordingUrl == null) {
            return SignAnalysis.fail(SignErrorType.UNCLEAR);
        }
        String lower = recordingUrl.toLowerCase();
        if (lower.contains("unclear")) {
            return SignAnalysis.fail(SignErrorType.UNCLEAR);
        }
        if (lower.contains("fail")) {
            return SignAnalysis.fail(SignErrorType.WRONG_HANDSHAPE);
        }
        return SignAnalysis.ok();
    }
}
