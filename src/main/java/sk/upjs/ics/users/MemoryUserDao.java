package sk.upjs.ics.users;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class MemoryUserDao implements UserDao {

    private final List<User> users = new ArrayList<>();
    private long sequence = 0;

    @Override
    public User create(User user) {
        if (user.id() != null && user.id() != 0) {
            throw new IllegalArgumentException("User must not have an id assigned when being created.");
        }

        var created = user.withId(++sequence);
        users.add(created);
        return created;
    }

    @Override
    public User update(User user) throws BadRequestException, NotFoundException {
        if (user.id() == null || user.id() == 0) {
            throw new BadRequestException("User must have an id assigned.");
        }

        var index = IntStream.range(0, users.size())
                .filter(i -> users.get(i).id().equals(user.id()))
                .findFirst()
                .orElse(-1);
        if (index < 0) {
            throw new NotFoundException("User with id " + user.id() + " does not exist.");
        }

        users.set(index, user);
        return user;
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        return users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with id " + id + " does not exist."));
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        return users.stream()
                .filter(user -> user.email().equals(email))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with email " + email + " does not exist."));
    }

    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public void deactivate(Long id) {
        if (id == null) {
            return;
        }

        var index = IntStream.range(0, users.size())
                .filter(i -> users.get(i).id().equals(id))
                .findFirst()
                .orElse(-1);
        if (index >= 0) {
            users.set(index, users.get(index).withActive(false));
        }
    }

    @Override
    public User reactivate(Long id) throws NotFoundException {
        var index = id == null ? -1 : IntStream.range(0, users.size())
                .filter(i -> users.get(i).id().equals(id))
                .findFirst()
                .orElse(-1);
        if (index < 0) {
            throw new NotFoundException("User with id " + id + " does not exist.");
        }

        var reactivated = users.get(index).withActive(true);
        users.set(index, reactivated);
        return reactivated;
    }
}
