package service;

import model.Credentials;
import model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> createUser(User user);
    Optional<User> getUserById(long currentUserID, long otherUserID);
    Optional<User> getCurrentUserById(long currentUserID);
    Optional<User> getCurrentUserByLogin(String login);
    boolean checkExistByLogin(String login);
    Optional<User> getCurrentUserByCredentials(Credentials credentials);
    boolean validateUserByCurrentCredantials(Credentials credentials);
    Optional<List<User>> getUsers(long currentUserID);
    Optional<List<User>>  getUsers(long currentUserID, long start_num, long counts);
    Optional<List<User>>  getUsers(long currentUserID, String fullName, long start_num, long counts);
    Optional<Long> getCount();
    Optional<Long> getCount(String fullName);

    Optional<User> updateProfile(User userIn);
    Optional<User> updatePassword(Credentials credentials);
}
