package dao;

import model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by Михаил on 06.01.2017.
 */

public interface UserDao {
    Optional<User> getByLogin(String loginName);
    Optional<User> getById(long id);
    Optional<User> createUser(User user);
    Optional<List<User>> getUsers();
    Optional<List<User>> getUsers(long start_num,long counts);
    Optional<List<User>> getUsers(String name, long start_num, long counts);
    Optional<List<User>> getUsers(String firstName, String lastName, long start_num, long counts);
    Optional<Long> getCount();
    Optional<Long> getCount(String name);
    Optional<Long> getCount(String firstName, String lastName);
}
