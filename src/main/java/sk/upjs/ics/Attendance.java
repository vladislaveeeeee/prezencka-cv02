package sk.upjs.ics;

import java.time.LocalDateTime;
import java.util.List;

public record Attendance(
        LocalDateTime data,
        Subject subject,
        List<User> attendees
) {
}
