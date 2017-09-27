package service;

import model.Notification;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    void addNotification(Notification notification);
    void deleteAllNotifications();
    void deleteAllNotificationsMonthAgoAndMore();
    void deleteAllUserNotificationsMonthAgoAndMore(long recipientID);

    Optional<List<Notification>> getAllNotificationsByRecipient(long recipientID);
    Optional<List<Notification>> getAllNotificationsDuringDay(long recipientID);
    Optional<List<Notification>> getAllNotificationsDuringMonth(long recipientID);
    Optional<List<Notification>> getAllNotificationsBySender(long senderID);

    Optional<Long> getCountNotifications(long recipientID);
    Optional<List<Notification>> getNotificationsDuringDay(long recipientID, int start_num, int count);
    Optional<Long> getCountNotificationsDuringDay(long recipientID);
    Optional<List<Notification>> getNotificationsDuringMonth(long recipientID, int start_num, int count);
    Optional<Long> getCountNotificationsDuringMonth(long recipientID);
    Optional<List<Notification>> getNotificationsByDate(long recipientID, Timestamp ts, int start_num, int count);
    Optional<Long> getCountNotificationsByDate(long recipientID, Timestamp ts);
}
