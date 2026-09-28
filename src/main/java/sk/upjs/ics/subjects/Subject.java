package sk.upjs.ics.subjects;

import sk.upjs.ics.users.User;

import java.util.Set;

public record Subject(
        Long id,
        String name,
        int yearOfStudy,
        User teacher,
        Set<User> students
) {
}
