package sk.upjs.ics.attendances;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.util.List;

public interface AttendanceDao {

    Attendance create(Attendance attendance);

    Attendance update(Attendance attendance) throws BadRequestException, NotFoundException;

    Attendance findById(Long id) throws NotFoundException;

    List<Attendance> findAll();

    List<Attendance> findAllSortedByDate();

    void deactivate(Long id);

    Attendance reactivate(Long id) throws NotFoundException;
}
