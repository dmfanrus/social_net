package model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ListNotifications {

    List<Notification> notificationList;
    List<Long> countrs;

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public List<Long> getCountrs() {
        return countrs;
    }

    @Override
    public String toString() {
        return "ListNotifications{" +
                "notificationList=" + notificationList +
                ", countrs=" + countrs +
                '}';
    }
}
