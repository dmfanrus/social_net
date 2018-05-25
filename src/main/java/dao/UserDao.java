package dao;

import model.Credentials;
import model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by Михаил on 06.01.2017.
 */

public interface UserDao {
    Optional<User> createUser(User user);
    void deleteUserById(long id);
    Optional<User> updateUserAllInfo(User user);
    Optional<User> updateProfile(User user);
    Optional<User> updatePassword(Credentials credentials);
    Optional<User> getCurrentUserWithAllInfoById(long id);
    Optional<User> getCurrentUserWithAllInfoByLogin(String login);
    Optional<User> getCurrentUserWithoutCredentialsByID(long id);
    Optional<User> getOtherUserWithAllInfoById(long currentUserID, long otherUserID);
    Optional<User> getOtherUserWithoutCredentialsByID(long currentUserID, long otherUserID);
    Optional<Credentials> getCredentialsById(long id);
    Optional<Credentials> getCredentialsByLogin(String login);

    Optional<List<User>> getAllUsers(long currentUserID);
    Optional<Long> getCountAllUsers();
    Optional<List<User>> getSeveralUsers(long currentUserID, long start_num, long counts);
    Optional<List<User>> getSeveralUsers(long currentUserID, String fullName, long start_num, long counts);
    Optional<Long> getCountSeveralUsers(String fullName);
    Optional<List<User>> getSeveralUsers(long currentUserID, String firstName, String lastName, long start_num, long counts);
    Optional<Long> getCountSeveralUsers(String firstName, String lastName);

}
