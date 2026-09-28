package sk.upjs.ics.subjects;

import org.junit.jupiter.api.Test;

import sk.upjs.ics.users.UserService;

import static org.junit.jupiter.api.Assertions.*;

class SubjectServiceTest {

    @Test
    void loadFromCsvReadsAllSubjects() {
        var subjects = SubjectService.loadFromCsv();

        assertEquals(4, subjects.size());
        assertEquals(1L, subjects.get(0).id());
        assertEquals(4L, subjects.get(3).id());
    }

    @Test
    void loadFromCsvResolvesTeacherAndStudentsToUsers() {
        var subjects = SubjectService.loadFromCsv();
        var users = UserService.loadFromCsv();

        var paz1c = subjects.get(0);
        assertEquals("PAZ1c", paz1c.name());
        assertEquals(2, paz1c.yearOfStudy());
        assertEquals("juraj.novak@upjs.sk", paz1c.teacher().email());
        assertEquals(12, paz1c.students().size());
        assertTrue(paz1c.students().contains(users.get(0)));
        assertTrue(paz1c.students().contains(users.get(13)));
        assertFalse(paz1c.students().contains(users.get(20)));

        var kopr = subjects.get(3);
        assertEquals("KOPR", kopr.name());
        assertEquals(3, kopr.yearOfStudy());
        assertEquals("juraj.novak@upjs.sk", kopr.teacher().email());
        assertEquals(5, kopr.students().size());
    }
}
