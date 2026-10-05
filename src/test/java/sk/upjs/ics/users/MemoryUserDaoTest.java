package sk.upjs.ics.users;

import org.junit.jupiter.api.Test;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUserDaoTest {

    private static User user(String email) {
        return new User(null, email, "Jano", "Horvát", User.Gender.MALE,
                LocalDate.of(1999, 1, 30), User.Role.STUDENT);
    }

    @Test
    void createReadUpdateDeactivateAndReactivateCoverFullCrudLifecycle() throws BadRequestException, NotFoundException {
        UserDao dao = new MemoryUserDao();

        var created = dao.create(user("jano@upjs.sk"));
        assertNotNull(created.id(), "create must assign an id");
        assertNotEquals(0L, created.id());
        assertTrue(created.active(), "created user is active");
        assertEquals(created, dao.findById(created.id()));
        assertEquals(created, dao.findByEmail("jano@upjs.sk"));

        var second = dao.create(user("hanka@upjs.sk"));
        assertNotEquals(created.id(), second.id(), "each create gets a distinct id");

        var renamed = created.withName("Juro");
        assertEquals(renamed, dao.update(renamed), "update returns the stored entity");
        assertEquals(renamed, dao.findById(created.id()), "update is persisted");
        assertEquals(2, dao.findAll().size());

        dao.deactivate(created.id());
        var deactivated = renamed.withActive(false);
        assertEquals(deactivated, dao.findById(created.id()), "findById sees the deactivated record");
        assertEquals(deactivated, dao.findByEmail("jano@upjs.sk"), "findByEmail sees the deactivated record");
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
        assertThrows(NotFoundException.class, () -> dao.findByEmail("nikto@upjs.sk"));
        assertThrows(NotFoundException.class, () -> dao.reactivate(999L));

        dao.deactivate(999L);
        dao.deactivate(null);
        assertEquals(2, dao.findAll().size(), "deactivate of an unknown or null id changes nothing");
    }
}
