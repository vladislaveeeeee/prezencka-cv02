package sk.upjs.ics.attendances;

import sk.upjs.ics.subjects.Subject;
import sk.upjs.ics.users.User;

import java.time.LocalDateTime;
import java.util.Set;

public record Attendance(
        Long id,
        LocalDateTime datetime,
        Subject subject,
        Set<User> attendees
) {
}
