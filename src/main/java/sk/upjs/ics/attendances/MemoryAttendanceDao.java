package sk.upjs.ics.attendances;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class MemoryAttendanceDao implements AttendanceDao {

    private final List<Attendance> attendances = new ArrayList<>();
    private long sequence = 0;

    @Override
    public Attendance create(Attendance attendance) {
        if (attendance.id() != null && attendance.id() != 0) {
            throw new IllegalArgumentException("Attendance must not have an id assigned when being created.");
        }

        var created = attendance.withId(++sequence);
        attendances.add(created);
        return created;
    }

    @Override
    public Attendance update(Attendance attendance) throws BadRequestException, NotFoundException {
        if (attendance.id() == null || attendance.id() == 0) {
            throw new BadRequestException("Attendance must have an id assigned.");
        }

        var index = IntStream.range(0, attendances.size())
                .filter(i -> attendances.get(i).id().equals(attendance.id()))
                .findFirst()
                .orElse(-1);
        if (index < 0) {
            throw new NotFoundException("Attendance with id " + attendance.id() + " does not exist.");
        }

        attendances.set(index, attendance);
        return attendance;
    }

    @Override
    public Attendance findById(Long id) throws NotFoundException {
        return attendances.stream()
                .filter(attendance -> attendance.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Attendance with id " + id + " does not exist."));
    }

    @Override
    public List<Attendance> findAll() {
        return Collections.unmodifiableList(attendances);
    }

    @Override
    public void deactivate(Long id) {
        if (id == null) {
            return;
        }

        var index = IntStream.range(0, attendances.size())
                .filter(i -> attendances.get(i).id().equals(id))
                .findFirst()
                .orElse(-1);
        if (index >= 0) {
            attendances.set(index, attendances.get(index).withActive(false));
        }
    }

    @Override
    public Attendance reactivate(Long id) throws NotFoundException {
        var index = id == null ? -1 : IntStream.range(0, attendances.size())
                .filter(i -> attendances.get(i).id().equals(id))
                .findFirst()
                .orElse(-1);
        if (index < 0) {
            throw new NotFoundException("Attendance with id " + id + " does not exist.");
        }

        var reactivated = attendances.get(index).withActive(true);
        attendances.set(index, reactivated);
        return reactivated;
    }
}
