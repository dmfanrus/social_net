package service;

import model.Credentials;
import model.Gender;
import model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> createUser(User user);
    Optional<User> getByLogin(String login);
    boolean checkExistByLogin(String login);
    Optional<User> getByCredentials(Credentials credentials);
    Optional<User> getById(long id);
    Optional<List<User>> getUsers();
    Optional<List<User>>  getUsers(String fullName, long start_num, long counts);
    Optional<Long> getCount();
    Optional<Long> getCount(String fullName);
}
