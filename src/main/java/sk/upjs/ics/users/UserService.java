package sk.upjs.ics.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private final List<User> database;


    public UserService(List<User> database) {
        this.database = database;
    }

    public Map<User.Gender, Integer> countByGender() {
        var result = new HashMap<User.Gender, Integer>();

        for (var gender: User.Gender.values()) {
            result.put(gender, 0);
        }

        for (var user: this.database) {
            var genderCount = result.get(user.gender());
            result.put(user.gender(), genderCount + 1);
        }

        return result;
    }

    public Map<User.Gender, Double> genderRatios() {
        var counts = this.countByGender();
        var totalCount = this.database.size();

        var result = new HashMap<User.Gender, Double>();
        for (var count: counts.entrySet()) {
            result.put(count.getKey(), (double) count.getValue() / totalCount);
        }

        return result;
    }

    private static final String CSV_RESOURCE = "/sk/upjs/ics/testdata/users.csv";

    public static List<User> loadFromCsv() {
        try (var stream = UserService.class.getResourceAsStream(CSV_RESOURCE);
             var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
             var lines = reader.lines()) {
            return lines.skip(1).map(UserService::toUser).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load users from " + CSV_RESOURCE, e);
        }
    }

    private static User toUser(String line) {
        var parts = line.split(",", -1);
        return new User(
                Long.valueOf(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                User.Gender.valueOf(parts[4]),
                LocalDate.parse(parts[5]),
                User.Role.valueOf(parts[6])
        );
    }
}
