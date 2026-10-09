package sk.upjs.ics.attendances;

import org.junit.jupiter.api.Test;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;
import sk.upjs.ics.subjects.Subject;
import sk.upjs.ics.users.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAttendanceDaoTest {

    private static final User STUDENT = new User(2L, "demeter.horvath@student.upjs.sk", "Demeter", "Horváth",
            User.Gender.MALE, LocalDate.of(2007, 1, 15), User.Role.STUDENT);

    private static final Subject PAZ1C = new Subject(1L, "PAZ1c", 2,
            new User(1L, "juraj.novak@upjs.sk", "Juraj", "Novák",
                    User.Gender.MALE, LocalDate.of(1985, 3, 14), User.Role.TEACHER),
            Set.of(STUDENT));

    @Test
    void createReadUpdateDeactivateAndReactivateCoverFullCrudLifecycle() throws BadRequestException, NotFoundException {
        AttendanceDao dao = new MemoryAttendanceDao();

        var created = dao.create(new Attendance(null, LocalDateTime.of(2026, 9, 20, 8, 0), PAZ1C, Set.of(STUDENT)));
        assertNotNull(created.id(), "create must assign an id");
        assertNotEquals(0L, created.id());
        assertTrue(created.active(), "created attendance is active");
        assertEquals(created, dao.findById(created.id()));

        var second = dao.create(new Attendance(null, LocalDateTime.of(2026, 9, 21, 8, 0), PAZ1C, Set.of()));
        assertNotEquals(created.id(), second.id(), "each create gets a distinct id");

        var rescheduled = created.withDatetime(LocalDateTime.of(2026, 10, 1, 9, 0));
        assertEquals(rescheduled, dao.update(rescheduled), "update returns the stored entity");
        assertEquals(rescheduled, dao.findById(created.id()), "update is persisted");
        assertEquals(2, dao.findAll().size());

        dao.deactivate(created.id());
        var deactivated = rescheduled.withActive(false);
        assertEquals(deactivated, dao.findById(created.id()), "findById sees the deactivated record");
        assertEquals(2, dao.findAll().size(), "findAll lists also deactivated records");
        assertFalse(dao.findById(created.id()).active());

        assertEquals(deactivated.withActive(true), dao.reactivate(created.id()), "reactivate returns the reactivated entity");
        assertTrue(dao.findById(created.id()).active(), "reactivate is persisted");

        var all = dao.findAll();
        assertThrows(UnsupportedOperationException.class, all::clear, "findAll returns an unmodifiable list");

        assertThrows(IllegalArgumentException.class, () -> dao.create(second.withId(99L)),
                "create rejects an entity with an already assigned id");
        assertThrows(BadRequestException.class, () -> dao.update(second.withId(null)),
                "update rejects a null id");
        assertThrows(BadRequestException.class, () -> dao.update(second.withId(0L)),
                "update rejects a zero id");
        assertThrows(NotFoundException.class, () -> dao.update(second.withId(999L)),
                "update rejects an unknown id");
        assertThrows(NotFoundException.class, () -> dao.findById(999L));
        assertThrows(NotFoundException.class, () -> dao.reactivate(999L));

        dao.deactivate(999L);
        dao.deactivate(null);
        assertEquals(2, dao.findAll().size(), "deactivate of an unknown or null id changes nothing");
    }
}
