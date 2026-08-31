package sk.upjs.ics.users;

import lombok.With;

import java.time.LocalDate;

@With
public record User(
        Long id,
        String email,
        String name,
        String surname,
        Gender gender,
        LocalDate birthDate,
        Role role
) {
    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN
    }

    public enum Role {
        STUDENT,
        TEACHER
    }
}
