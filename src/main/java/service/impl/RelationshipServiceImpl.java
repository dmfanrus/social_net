package service.impl;

import com.google.inject.Inject;
import dao.RelationshipDao;
import model.Notification;
import model.RelationStatus;
import model.Relationship;
import model.User;
import service.NotificationService;
import service.RelationshipService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class RelationshipServiceImpl implements RelationshipService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RelationshipServiceImpl.class);
    private final RelationshipDao relationshipDao;
    private final NotificationService notificationService;

    @Inject
    public RelationshipServiceImpl(RelationshipDao relationshipDao, NotificationService notificationService) {
        this.relationshipDao = relationshipDao;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<Long> getCount(long currentUserID) {
        return relationshipDao.getCountAllFriends(currentUserID);
    }

    @Override
    public Optional<List<User>> getFriends(long currentUserID) {
        return relationshipDao.getAllFriends(currentUserID);
    }


    @Override
    public Optional<Long> getCount(long currentUserID, String fullName) {
        if(fullName!=null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return relationshipDao.getCountSeveralFriends(currentUserID, names[0]);
            } else if (names.length == 2) {
                return relationshipDao.getCountSeveralFriends(currentUserID, names[0], names[1]);
            } else {
                return Optional.empty();
            }
        } else {
            return relationshipDao.getCountAllFriends(currentUserID);
        }
    }

    @Override
    public Optional<RelationStatus> getRelationStatus(long currentUserID, long otherUserID) {
        return relationshipDao.getRelationStatusWithTwoUsers(currentUserID,otherUserID);
    }

    @Override
    public void addFriendRequestFromCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action) {
        notificationService.addNotification(Notification.builder()
                .sender_id(currentUserID)
                .recipient_id(otherUserID)
                .not_status(16)
                .ts_action(ts_action)
                .build());
        relationshipDao.addRelationships(Relationship.builder()
                .firstUserID(currentUserID)
                .secondUserID(otherUserID)
                .relationStatus(RelationStatus.NOTCONFIRMBYOTHER)
                .ts_action(ts_action)
                .build());
        relationshipDao.addRelationships(Relationship.builder()
                .firstUserID(otherUserID)
                .secondUserID(currentUserID)
                .relationStatus(RelationStatus.NOTCONFIRMBYME)
                .ts_action(ts_action)
                .build());
    }

    @Override
    public void deleteFriendRequests(long currentUserID, long otherUserID) {
        relationshipDao.deleteRelationships(currentUserID,otherUserID);
    }

    @Override
    public void confirmFriendRequestByCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action) {
        notificationService.addNotification(Notification.builder()
                .sender_id(currentUserID)
                .recipient_id(otherUserID)
                .not_status(17)
                .ts_action(ts_action)
                .build());
        relationshipDao.updateRelatioships(Relationship.builder()
                .firstUserID(currentUserID)
                .secondUserID(otherUserID)
                .relationStatus(RelationStatus.FRIEND)
                .ts_action(ts_action)
                .build());
        relationshipDao.updateRelatioships(Relationship.builder()
                .firstUserID(otherUserID)
                .secondUserID(currentUserID)
                .relationStatus(RelationStatus.FRIEND)
                .ts_action(ts_action)
                .build());
    }


    @Override
    public void blockedOtherUserByCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action) {
        notificationService.addNotification(Notification.builder()
                .sender_id(currentUserID)
                .recipient_id(otherUserID)
                .not_status(19)
                .ts_action(ts_action)
                .build());
        relationshipDao.addRelationships(Relationship.builder()
                .firstUserID(currentUserID)
                .secondUserID(otherUserID)
                .relationStatus(RelationStatus.BLOCKEDBYME)
                .ts_action(ts_action)
                .build());
        relationshipDao.addRelationships(Relationship.builder()
                .firstUserID(otherUserID)
                .secondUserID(currentUserID)
                .relationStatus(RelationStatus.BLOCKEDBYOTHER)
                .ts_action(ts_action)
                .build());
    }

    @Override
    public void unblockedOtherUserByCurrentUser(long currentUserID, long otherUserID, Timestamp ts_action) {
        notificationService.addNotification(Notification.builder()
                .sender_id(currentUserID)
                .recipient_id(otherUserID)
                .not_status(20)
                .ts_action(ts_action)
                .build());
        relationshipDao.deleteRelationships(currentUserID,otherUserID);
    }
    @Override
    public void deleteFriendRelationships(long currentUserID, long otherUserID, Timestamp ts_action) {
        notificationService.addNotification(Notification.builder()
                .sender_id(currentUserID)
                .recipient_id(otherUserID)
                .not_status(20)
                .ts_action(ts_action)
                .build());
        relationshipDao.deleteRelationships(currentUserID, otherUserID);
    }

    @Override
    public void deleteRelationships(long currentUserID, long otherUserID) {
        relationshipDao.deleteRelationships(currentUserID, otherUserID);
    }

    @Override
    public Optional<RelationStatus> getRelationStatusByConvID(long conv_id) {
        return relationshipDao.getRelationshipWithTwoUsersByConvID(conv_id);
    }


    @Override
    public Optional<List<User>> getFriends(long currentUserID, String fullName, long start_num, long counts) {
        if(fullName!=null && !fullName.isEmpty()){
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return relationshipDao.getSeveralFriends(currentUserID, names[0], start_num, counts);
            } else if (names.length == 2) {
                return relationshipDao.getSeveralFriends(currentUserID, names[0], names[1], start_num, counts);
            } else {
                return Optional.empty();
            }
        } else {
            return relationshipDao.getSeveralFriends(currentUserID, start_num, counts);
        }
    }
}
