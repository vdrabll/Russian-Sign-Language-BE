package ru.rsl.api.domain;

import org.junit.jupiter.api.Test;
import ru.rsl.api.Model.UserType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTypeTest {

    @Test
    void parsesHearingAndDeaf() {
        assertEquals(UserType.HEARING, UserType.fromJson("hearing"));
        assertEquals(UserType.DEAF, UserType.fromJson("deaf"));
        assertEquals("hearing", UserType.HEARING.json());
    }
}
