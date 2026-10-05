package sk.upjs.ics.subjects;

import lombok.With;
import sk.upjs.ics.users.User;

import java.util.Set;

@With
public record Subject(
        Long id,
        String name,
        int yearOfStudy,
        User teacher,
        Set<User> students,
        boolean active
) {
    public Subject(Long id, String name, int yearOfStudy, User teacher, Set<User> students) {
        this(id, name, yearOfStudy, teacher, students, true);
    }
}
