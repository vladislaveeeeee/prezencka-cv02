package sk.upjs.ics.attendances;

import org.junit.jupiter.api.Test;

import sk.upjs.ics.users.User;
import sk.upjs.ics.users.UserService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceServiceTest {

    private static Attendance attendance(long id) {
        return AttendanceService.loadFromCsv().stream()
                .filter(attendance -> attendance.id() == id)
                .findFirst()
                .orElseThrow();
    }

    @Test
    void loadFromCsvReadsAllAttendances() {
        var attendances = AttendanceService.loadFromCsv();

        assertEquals(20, attendances.size());
        assertEquals(1L, attendances.get(1).id());
        assertEquals(20L, attendances.get(0).id());
    }

    @Test
    void loadFromCsvResolvesSubjectAndAttendees() {
        var users = UserService.loadFromCsv();
        var paZ1c = attendance(1);

        assertEquals(LocalDateTime.of(2026, 9, 20, 8, 0), paZ1c.datetime());
        assertEquals("PAZ1c", paZ1c.subject().name());
        assertEquals(12, paZ1c.attendees().size());
        assertTrue(paZ1c.attendees().contains(users.get(0)));
        assertTrue(paZ1c.attendees().contains(users.get(13)));
        assertFalse(paZ1c.attendees().contains(users.get(20)));

        var single = attendance(18);
        assertEquals("KOPR", single.subject().name());
        assertEquals(Set.of(users.get(13)), single.attendees());
    }

    @Test
    void loadFromCsvHandlesMissingAttendeesAsEmptySet() {
        var subjectWithoutAttendees = attendance(20);
        var subjectWithAttendees = attendance(19);

        assertEquals("KOPR", subjectWithoutAttendees.subject().name());
        assertEquals("KOPR", subjectWithAttendees.subject().name());
        assertEquals(0, subjectWithoutAttendees.attendees().size());
        assertEquals(0, subjectWithAttendees.attendees().size());
    }
}
