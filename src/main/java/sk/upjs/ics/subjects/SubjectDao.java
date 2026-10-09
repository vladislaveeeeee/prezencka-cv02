package sk.upjs.ics.subjects;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.util.List;

public interface SubjectDao {

    Subject create(Subject subject);

    Subject update(Subject subject) throws BadRequestException, NotFoundException;

    Subject findById(Long id) throws NotFoundException;

    List<Subject> findAll();

    void deactivate(Long id);

    Subject reactivate(Long id) throws NotFoundException;
}
