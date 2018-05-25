package dao;

import model.ListNotifications;
import model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationDao {


    void addNotification(Notification notification);

    Optional<List<Notification>> getNotifications(long recipient_id);
    Optional<List<Notification>> getNotificationsBySender(long sender_id);

    Optional<ListNotifications> getNotificationsByInterval(long currentUserID, String s, long start_num, long counts);
    Optional<Long> getCountNotificationsByInterval(long currentUserID, String interval);
}
