package dao;

import model.Notification;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface NotificationDao {


    void AddNotification(Notification notification);
    void deleteAllNotifications();
    void deleteAllNotificationsMonthAgoAndMore();
    void deleteAllUserNotificationsMonthAgoAndMore(long recipient_id);

    Optional<List<Notification>> getNotifications(long recipient_id);
    Optional<List<Notification>> getNotificationsBySender(long sender_id);
    Optional<Long> getCountNotifications(long recipient_id);
    Optional<List<Notification>> getAllNotificationsDuringDay(long recipient_id);
    Optional<List<Notification>> getAllNotificationsDuringMonth(long recipient_id);
    Optional<List<Notification>> getNotificationsDuringDay(long recipient_id, int start_num, int count);
    Optional<Long> getCountNotificationsDuringDay(long recipient_id);
    Optional<List<Notification>> getNotificationsDuringMonth(long recipient_id, int start_num, int count);
    Optional<Long> getCountNotificationsDuringMonth(long recipient_id);
    Optional<List<Notification>> getNotificationsByDate(long recipient_id, Timestamp ts, int start_num, int count);
    Optional<Long> getCountNotificationsByDate(long recipient_id, Timestamp ts);

}
