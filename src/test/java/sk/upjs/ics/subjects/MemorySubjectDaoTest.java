package sk.upjs.ics.subjects;

import org.junit.jupiter.api.Test;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;
import sk.upjs.ics.users.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemorySubjectDaoTest {

    private static final User TEACHER = new User(1L, "juraj.novak@upjs.sk", "Juraj", "Novák",
            User.Gender.MALE, LocalDate.of(1985, 3, 14), User.Role.TEACHER);

    private static final User STUDENT = new User(2L, "demeter.horvath@student.upjs.sk", "Demeter", "Horváth",
            User.Gender.MALE, LocalDate.of(2007, 1, 15), User.Role.STUDENT);

    @Test
    void createReadUpdateDeactivateAndReactivateCoverFullCrudLifecycle() throws BadRequestException, NotFoundException {
        SubjectDao dao = new MemorySubjectDao();

        var created = dao.create(new Subject(null, "PAZ1c", 2, TEACHER, Set.of(STUDENT)));
        assertNotNull(created.id(), "create must assign an id");
        assertNotEquals(0L, created.id());
        assertTrue(created.active(), "created subject is active");
        assertEquals(created, dao.findById(created.id()));

        var second = dao.create(new Subject(null, "KOPR", 3, TEACHER, Set.of()));
        assertNotEquals(created.id(), second.id(), "each create gets a distinct id");

        var revisedYear = created.withYearOfStudy(3);
        assertEquals(revisedYear, dao.update(revisedYear), "update returns the stored entity");
        assertEquals(revisedYear, dao.findById(created.id()), "update is persisted");
        assertEquals(2, dao.findAll().size());

        dao.deactivate(created.id());
        var deactivated = revisedYear.withActive(false);
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
