package service.impl;

import com.google.inject.Inject;
import dao.NotificationDao;
import model.Notification;
import service.NotificationService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class NotificationServiceImpl implements NotificationService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationDao notificationDao;

    @Inject
    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public void addNotification(Notification notification) {
        notificationDao.AddNotification(notification);
    }

    @Override
    public void deleteAllNotifications() {
        notificationDao.deleteAllNotifications();
    }

    @Override
    public void deleteAllNotificationsMonthAgoAndMore() {
        notificationDao.deleteAllNotificationsMonthAgoAndMore();
    }

    @Override
    public void deleteAllUserNotificationsMonthAgoAndMore(long recipientID) {
        notificationDao.deleteAllUserNotificationsMonthAgoAndMore(recipientID);
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsByRecipient(long recipientID) {
        return notificationDao.getNotifications(recipientID);
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsDuringDay(long recipientID) {
        return notificationDao.getAllNotificationsDuringDay(recipientID);
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsDuringMonth(long recipientID) {
        return notificationDao.getAllNotificationsDuringMonth(recipientID);
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsBySender(long senderID) {
        return notificationDao.getNotificationsBySender(senderID);
    }

    @Override
    public Optional<Long> getCountNotifications(long recipientID) {
        return notificationDao.getCountNotifications(recipientID);
    }

    @Override
    public Optional<List<Notification>> getNotificationsDuringDay(long recipientID, int start_num, int count) {
        return notificationDao.getNotificationsDuringDay(recipientID,start_num,count);
    }

    @Override
    public Optional<Long> getCountNotificationsDuringDay(long recipientID) {
        return notificationDao.getCountNotificationsDuringDay(recipientID);
    }

    @Override
    public Optional<List<Notification>> getNotificationsDuringMonth(long recipientID, int start_num, int count) {
        return notificationDao.getNotificationsDuringMonth(recipientID, start_num, count);
    }

    @Override
    public Optional<Long> getCountNotificationsDuringMonth(long recipientID) {
        return notificationDao.getCountNotificationsDuringMonth(recipientID);
    }

    @Override
    public Optional<List<Notification>> getNotificationsByDate(long recipientID, Timestamp ts, int start_num, int count) {
        return notificationDao.getNotificationsByDate(recipientID,ts,start_num,count);
    }

    @Override
    public Optional<Long> getCountNotificationsByDate(long recipientID, Timestamp ts) {
        return notificationDao.getCountNotificationsByDate(recipientID,ts);
    }


}
