package dao;

import model.User;

import java.util.List;
import java.util.Optional;

public interface FriendDao {
    List<Optional<User>> getAllFriends(long currentUserID);
    Optional<List<User>> getFriends(long currentUserID, long start_num,long counts);
    Optional<List<User>> getFriends(long currentUserID, String name, long start_num, long counts);
    Optional<List<User>> getFriends(long currentUserID, String firstName, String lastName, long start_num, long counts);
    Optional<Long> getCount(long currentUserID);
    Optional<Long> getCount(long currentUserID, String name);
    Optional<Long> getCount(long currentUserID, String firstName, String lastName);
}
