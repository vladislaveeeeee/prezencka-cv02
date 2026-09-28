package sk.upjs.ics.attendances;

import sk.upjs.ics.subjects.Subject;
import sk.upjs.ics.subjects.SubjectService;
import sk.upjs.ics.users.User;
import sk.upjs.ics.users.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AttendanceService {
    private static final String CSV_RESOURCE = "/sk/upjs/ics/testdata/attendances.csv";

    public static List<Attendance> loadFromCsv() {
        var usersById = UserService.loadFromCsv().stream()
                .collect(Collectors.toUnmodifiableMap(User::id, user -> user));

        var subjectsById = SubjectService.loadFromCsv().stream()
                .collect(Collectors.toUnmodifiableMap(Subject::id, subject -> subject));

        try (var stream = AttendanceService.class.getResourceAsStream(CSV_RESOURCE);
             var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
             var lines = reader.lines()) {
            return lines.skip(1).map(line -> toAttendance(line, usersById, subjectsById)).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load attendances from " + CSV_RESOURCE, e);
        }
    }

    private static Attendance toAttendance(String line, Map<Long, User> usersById, Map<Long, Subject> subjectsById) {
        var parts = line.split(",", -1);
        return new Attendance(
                Long.valueOf(parts[0]),
                LocalDateTime.parse(parts[1]),
                subjectsById.get(Long.valueOf(parts[2])),
                toAttendees(parts[3], usersById)
        );
    }

    private static Set<User> toAttendees(String attendeeIds, Map<Long, User> usersById) {
        if (attendeeIds.isBlank()) {
            return Set.of();
        }

        var attendees = new HashSet<User>();
        for (var attendeeId : attendeeIds.split("_")) {
            attendees.add(usersById.get(Long.valueOf(attendeeId)));
        }

        return Set.copyOf(attendees);
    }
}
