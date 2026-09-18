package ru.rsl.api.service;

import ru.rsl.api.Model.SignErrorType;

import java.util.List;

public record SignAnalysis(boolean success, List<SignErrorType> errors) {

    public static SignAnalysis ok() {
        return new SignAnalysis(true, List.of());
    }

    public static SignAnalysis fail(SignErrorType... errors) {
        return new SignAnalysis(false, List.of(errors));
    }
}
