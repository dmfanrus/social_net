package dao;

import model.Relationship;
import model.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RelationshipDao {
    void addRelationships(Relationship relationship);
    void updateRelatioships(Relationship relationship);
    void deleteRelationships(long firstUserID, long secondUserID);
    Optional<List<Relationship>> getAllRelationships();
    Optional<List<Relationship>> getCurrentUserRelationship(long currentUserID);
    Optional<List<Relationship>> getRelationshipWithTwoUsers(long firstUserID, long secondUserID);


    Optional<List<User>> getAllFriends(long currentUserID);
    Optional<Long> getCountAllFriends(long currentUserID);
    Optional<List<User>> getSeveralFriends(long currentUserID, long start_num, long counts);
    Optional<List<User>> getSeveralFriends(long currentUserID, String fullName, long start_num, long counts);
    Optional<Long> getCountSeveralFriends(long currentUserID, String fullName);
    Optional<List<User>> getSeveralFriends(long currentUserID, String firstName, String lastName, long start_num, long counts);
    Optional<Long> getCountSeveralFriends(long currentUserID, String firstName, String lastName);

}
