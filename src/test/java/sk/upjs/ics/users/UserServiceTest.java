package sk.upjs.ics.users;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.upjs.ics.users.User.Gender;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(
                List.of(
                        new User(1L, "hanka@upjs.sk", "Hanka", "Kováčová", Gender.FEMALE,
                                LocalDate.of(2000, 5, 12), User.Role.STUDENT),
                        new User(2L, "jano@upjs.sk", "Jano", "Horvát", Gender.MALE,
                                LocalDate.of(1999, 1, 30), User.Role.STUDENT),
                        new User(3L, "juraj@upjs.sk", "Juraj", "Novák", Gender.MALE,
                                LocalDate.of(1998, 11, 3), User.Role.TEACHER),
                        new User(4L, "evka@upjs.sk", "Evka", "Szabová", Gender.UNKNOWN,
                                LocalDate.of(2001, 7, 20), User.Role.STUDENT)
                )
        );
    }

    @Test
    void countByGenderCountsEveryGenderAndKeepsZeroes() {
        Map<Gender, Integer> counts = userService.countByGender();

        assertEquals(User.Gender.values().length, counts.size());
        assertEquals(2, counts.get(Gender.MALE));
        assertEquals(1, counts.get(Gender.FEMALE));
        assertEquals(1, counts.get(Gender.UNKNOWN));
        assertEquals(Map.of(Gender.MALE, 2, Gender.FEMALE, 1, Gender.UNKNOWN, 1), counts);

        Map<Gender, Integer> emptyCounts = new UserService(List.of()).countByGender();

        assertEquals(User.Gender.values().length, emptyCounts.size());
        assertEquals(0, emptyCounts.get(Gender.MALE));
        assertEquals(0, emptyCounts.get(Gender.FEMALE));
        assertEquals(0, emptyCounts.get(Gender.UNKNOWN));
    }

    @Test
    void genderRatiosDivideCountsByTotalUsers() {
        Map<Gender, Double> ratios = userService.genderRatios();

        assertEquals(User.Gender.values().length, ratios.size());
        assertEquals(0.5, ratios.get(Gender.MALE), 1e-9);
        assertEquals(0.25, ratios.get(Gender.FEMALE), 1e-9);
        assertEquals(0.25, ratios.get(Gender.UNKNOWN), 1e-9);

        double sum = ratios.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(1.0, sum, 1e-9);

        // No users -> division by zero yields NaN for every gender (no exception thrown).
        Map<Gender, Double> emptyRatios = new UserService(List.of()).genderRatios();

        assertEquals(User.Gender.values().length, emptyRatios.size());
        for (Gender gender : User.Gender.values()) {
            assertTrue(emptyRatios.get(gender).isNaN(), "expected NaN ratio for " + gender);
        }
    }

}
