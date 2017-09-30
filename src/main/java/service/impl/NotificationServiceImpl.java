package service.impl;

import com.google.inject.Inject;
import dao.NotificationDao;
import model.ListNotifications;
import model.Notification;
import service.NotificationService;

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
    public Optional<List<Notification>> getAllNotificationsByRecipient(long recipientID) {
        return notificationDao.getNotifications(recipientID);
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsBySender(long senderID) {
        return notificationDao.getNotificationsBySender(senderID);
    }

    @Override
    public Optional<ListNotifications> getNotificationsByDifferentInterval(long currentUserID, String timeInterval, long start_num, long counts) {
        switch (timeInterval){
            case "day":{
                return notificationDao.getNotificationsByInterval(currentUserID, "1 day", start_num, counts);
            }
            case "week":{
                return notificationDao.getNotificationsByInterval(currentUserID, "1 week", start_num, counts);
            }
            case "month":{
                return notificationDao.getNotificationsByInterval(currentUserID, "1 month", start_num, counts);
            }
            case "year":{
                return notificationDao.getNotificationsByInterval(currentUserID, "1 year", start_num, counts);
            }
            default:{
                return Optional.empty();
            }
        }
    }


    @Override
    public Optional<Long> getCountNotificationsByDifferentInterval(long currentUserID, String timeInterval) {
        switch (timeInterval){
            case "day":{
                return notificationDao.getCountNotificationsByInterval(currentUserID, "1 day");
            }
            case "week":{
                return notificationDao.getCountNotificationsByInterval(currentUserID, "1 week");
            }
            case "month":{
                return notificationDao.getCountNotificationsByInterval(currentUserID, "1 month");
            }
            case "year":{
                return notificationDao.getCountNotificationsByInterval(currentUserID, "1 year");
            }
            default:{
                return Optional.empty();
            }
        }
    }

}
