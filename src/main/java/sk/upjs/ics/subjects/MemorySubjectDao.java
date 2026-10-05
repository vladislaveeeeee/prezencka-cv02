package sk.upjs.ics.subjects;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class MemorySubjectDao implements SubjectDao {

    private final List<Subject> subjects = new ArrayList<>();
    private long sequence = 0;

    @Override
    public Subject create(Subject subject) {
        if (subject.id() != null && subject.id() != 0) {
            throw new IllegalArgumentException("Subject must not have an id assigned when being created.");
        }

        var created = subject.withId(++sequence);
        subjects.add(created);
        return created;
    }

    @Override
    public Subject update(Subject subject) throws BadRequestException, NotFoundException {
        if (subject.id() == null || subject.id() == 0) {
            throw new BadRequestException("Subject must have an id assigned.");
        }

        var index = IntStream.range(0, subjects.size())
                .filter(i -> subjects.get(i).id().equals(subject.id()))
                .findFirst()
                .orElse(-1);
        if (index < 0) {
            throw new NotFoundException("Subject with id " + subject.id() + " does not exist.");
        }

        subjects.set(index, subject);
        return subject;
    }

    @Override
    public Subject findById(Long id) throws NotFoundException {
        return subjects.stream()
                .filter(subject -> subject.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Subject with id " + id + " does not exist."));
    }

    @Override
    public List<Subject> findAll() {
        return Collections.unmodifiableList(subjects);
    }

    @Override
    public void deactivate(Long id) {
        if (id == null) {
            return;
        }

        var index = IntStream.range(0, subjects.size())
                .filter(i -> subjects.get(i).id().equals(id))
                .findFirst()
                .orElse(-1);
        if (index >= 0) {
            subjects.set(index, subjects.get(index).withActive(false));
        }
    }

    @Override
    public Subject reactivate(Long id) throws NotFoundException {
        var index = id == null ? -1 : IntStream.range(0, subjects.size())
                .filter(i -> subjects.get(i).id().equals(id))
                .findFirst()
                .orElse(-1);
        if (index < 0) {
            throw new NotFoundException("Subject with id " + id + " does not exist.");
        }

        var reactivated = subjects.get(index).withActive(true);
        subjects.set(index, reactivated);
        return reactivated;
    }
}
