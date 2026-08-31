package sk.upjs.ics.subjects;

import sk.upjs.ics.users.User;

public record Subject(
        Long id,
        String name,
        int yearOfStudy,
        Set<User>
) {
}
