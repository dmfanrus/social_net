package service;

import model.RelationStatus;
import model.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RelationshipService {
    Optional<List<User>> getFriends(long currentUserID);
    Optional<List<User>>  getFriends(long currentUserID, String fullName, long start_num, long counts);
    Optional<Long> getCount(long currentUserID);
    Optional<Long> getCount(long currentUserID, String fullName);
    Optional<RelationStatus> getRelationStatus(long currentUserID, long otherUserID);

    void addFriendRequestFromCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action);
    void deleteFriendRequests(long currentUserID, long otherUserID);
    void confirmFriendRequestByCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action);
    void blockedOtherUserByCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action);
    void unblockedOtherUserByCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action);
    void deleteFriendRelationships(long currentUserID, long otherUserID, Timestamp ts_action);
    void deleteRelationships(long currentUserID, long otherUserID);

    Optional<RelationStatus> getRelationStatusByConvID(long conv_id);
}
