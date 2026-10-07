package sk.upjs.ics;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {



    private static User user(User.Gender gender) {
        return new User(1L, "Meno", gender, "Priezvisko",
                LocalDate.of(2005, 1, 1), User.Role.STUDENT);
    }


    @Test
    void emptyInput() {
        UserService service = new UserService(List.of());

        GenderRatio result = service.calculateGenderRatio();

        assertEquals(new GenderRatio(0, 0, 0, 0), result);
    }


    @Test
    void nullInput() {
        UserService service = new UserService(null);

        GenderRatio result = service.calculateGenderRatio();

        assertEquals(new GenderRatio(0, 0, 0, 0), result);
    }


    @Test
    void happyPath() {
        List<User> users = List.of(
                user(User.Gender.MALE),
                user(User.Gender.MALE),
                user(User.Gender.FEMALE),
                user(User.Gender.OTHER)
        );
        UserService service = new UserService(users);

        GenderRatio result = service.calculateGenderRatio();

        assertEquals(0.5, result.boys(), 1e-9);
        assertEquals(0.25, result.girls(), 1e-9);
        assertEquals(0.0, result.unknown(), 1e-9);
        assertEquals(0.25, result.other(), 1e-9);
    }


    @Test
    void nullUserInList() {
        List<User> users = Arrays.asList(user(User.Gender.FEMALE), null);
        UserService service = new UserService(users);

        GenderRatio result = service.calculateGenderRatio();

        assertEquals(0.5, result.girls(), 1e-9);
        assertEquals(0.5, result.unknown(), 1e-9);
    }
}