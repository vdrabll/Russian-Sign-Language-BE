package ru.rsl.api.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignErrorType {
    WRONG_HANDSHAPE("wrong_handshape"),
    WRONG_LOCATION("wrong_location"),
    WRONG_MOVEMENT("wrong_movement"),
    WRONG_ORIENTATION("wrong_orientation"),
    UNCLEAR("unclear");

    private final String json;

    SignErrorType(String json) {
        this.json = json;
    }

    @JsonValue
    public String json() {
        return json;
    }

    @JsonCreator
    public static SignErrorType fromJson(String value) {
        for (SignErrorType type : values()) {
            if (type.json.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown sign error: " + value);
    }
}
