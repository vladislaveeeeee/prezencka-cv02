package sk.upjs.ics;

import java.time.LocalDateTime;
import java.util.List;

public record Attendance(
        LocalDateTime date,
        Subject subject,
        List<User> attendees
) {
}
