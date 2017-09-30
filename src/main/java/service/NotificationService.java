package service;

import model.ListNotifications;
import model.Notification;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    void addNotification(Notification notification);

    Optional<List<Notification>> getAllNotificationsByRecipient(long recipientID);
    Optional<List<Notification>> getAllNotificationsBySender(long senderID);

    Optional<ListNotifications> getNotificationsByDifferentInterval(long currentUserID, String timeInterval, long start_num, long counts);
    Optional<Long> getCountNotificationsByDifferentInterval(long currentUserID, String timeInterval);
}
