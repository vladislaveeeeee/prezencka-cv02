package sk.upjs.ics;

import java.time.LocalDate;

public record User(
        Long id,
        String name,
        Gender gender,
        String surname,
        LocalDate birthday,
        Role role


) {
    public enum Role {
        STUDENT,
        TEACHER,
        ADMIN
    }

    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN,
        OTHER
    }
}