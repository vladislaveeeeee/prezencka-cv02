package sk.upjs.ics.attendances;

import lombok.With;
import sk.upjs.ics.subjects.Subject;
import sk.upjs.ics.users.User;

import java.time.LocalDateTime;
import java.util.Set;

@With
public record Attendance(
        Long id,
        LocalDateTime datetime,
        Subject subject,
        Set<User> attendees,
        boolean active
) {
    public Attendance(Long id, LocalDateTime datetime, Subject subject, Set<User> attendees) {
        this(id, datetime, subject, attendees, true);
    }
}
