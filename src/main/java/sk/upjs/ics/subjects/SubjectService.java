package sk.upjs.ics.subjects;

import sk.upjs.ics.users.User;
import sk.upjs.ics.users.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubjectService {
    private static final String CSV_RESOURCE = "/sk/upjs/ics/testdata/subjects.csv";

    public static List<Subject> loadFromCsv() {
        var usersById = UserService.loadFromCsv().stream()
                .collect(Collectors.toUnmodifiableMap(User::id, user -> user));

        try (var stream = SubjectService.class.getResourceAsStream(CSV_RESOURCE);
             var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
             var lines = reader.lines()) {
            return lines.skip(1).map(line -> toSubject(line, usersById)).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load subjects from " + CSV_RESOURCE, e);
        }
    }

    private static Subject toSubject(String line, Map<Long, User> usersById) {
        var parts = line.split(",", -1);
        var students = new HashSet<User>();

        for (var studentId : parts[4].split("_")) {
            students.add(usersById.get(Long.valueOf(studentId)));
        }

        return new Subject(
                Long.valueOf(parts[0]),
                parts[1],
                Integer.valueOf(parts[2]),
                usersById.get(Long.valueOf(parts[3])),
                Set.copyOf(students)
        );
    }
}
