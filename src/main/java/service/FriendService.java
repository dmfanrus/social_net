package service;

import model.Credentials;
import model.User;

import java.util.List;
import java.util.Optional;

public interface FriendService {
    List<Optional<User>> getFriends(long currentUserID);
    Optional<List<User>>  getFriends(long currentUserID, String fullName, long start_num, long counts);
    Optional<Long> getCount(long currentUserID);
    Optional<Long> getCount(long currentUserID, String fullName);
}
