package service;

import model.Credentials;
import model.Gender;
import model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByLogin(String login);
    Optional<User> getByCredentials(Credentials credentials);
    Optional<User> getById(long id);
    Optional<User> createUser(User user);
    boolean checkExistByLogin(String login);
    List<Optional<User>> getAllUsers();
    Optional<List<User>>  getUsers(long start_num, long counts);
    Optional<List<User>>  getUsers(String fullName, long start_num, long counts);
    Optional<Long> getCount();
    Optional<Long> getCount(String fullName);
}
