package sk.upjs.ics.users;

import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.NotFoundException;

import java.util.List;

public interface UserDao {

    User create(User user);

    User update(User user) throws BadRequestException, NotFoundException;

    User findById(Long id) throws NotFoundException;

    User findByEmail(String email) throws NotFoundException;

    List<User> findAll();

    void deactivate(Long id);

    User reactivate(Long id) throws NotFoundException;
}
