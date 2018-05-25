package dao.impl;

import com.google.inject.Inject;
import dao.NotificationDao;
import model.ListNotifications;
import model.Notification;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificationDaoImpl implements NotificationDao {


    private final DataSource dataSource;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationDaoImpl.class);

    @Inject
    public NotificationDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void addNotification(Notification notification) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO notifications(sender_id, recipient_id, action_id, ts_action) " +
                            "VALUES(?,?,?,?)");
            insert.setLong(1, notification.getSender_id());
            insert.setLong(2, notification.getRecipient_id());
            insert.setInt(3, notification.getNot_status());
            insert.setTimestamp(4, notification.getTs_action());
            insert.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        } finally {

        }
    }


    @Override
    public Optional<List<Notification>> getNotifications(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM notifications " +
                            "WHERE recipient_id=?");
            select.setLong(1, recipient_id);
            final ResultSet resultSet = select.executeQuery();
            final List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                notifications.add(
                        Notification.builder()
                                .sender_id(resultSet.getLong(1))
                                .recipient_id(resultSet.getLong(2))
                                .not_status(resultSet.getInt(3))
                                .ts_action(resultSet.getTimestamp(4))
                                .build());
            }
            return Optional.of(notifications);
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        } finally {
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getNotificationsBySender(long sender_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT n.sender_id, n.recipient_id, u.firstname, u.lastname, n.action_id, n.ts_action " +
                            "FROM notifications n, users u " +
                            "WHERE n.sender_id=? AND u.id=n.sender_id");
            select.setLong(1, sender_id);
            final ResultSet resultSet = select.executeQuery();
            final List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                notifications.add(
                        Notification.builder()
                                .sender_id(resultSet.getLong(1))
                                .recipient_id(resultSet.getLong(2))
                                .firstName(resultSet.getString(3))
                                .lastName(resultSet.getString(4))
                                .not_status(resultSet.getInt(5))
                                .ts_action(resultSet.getTimestamp(6))
                                .build());
            }
            return Optional.of(notifications);
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        } finally {

        }
        return Optional.empty();
    }


    @Override
    public Optional<ListNotifications> getNotificationsByInterval(long currentUserID, String interval, long start_num, long count) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select1 = connection.prepareStatement(
                    "SELECT SUM(CASE WHEN (CURRENT_DATE-ts_action)<'1 day'::INTERVAL THEN 1 ELSE 0 END), " +
                            "SUM(CASE WHEN (CURRENT_DATE-ts_action)<'1 week'::INTERVAL THEN 1 ELSE 0 END), " +
                            "SUM(CASE WHEN (CURRENT_DATE-ts_action)<'1 month'::INTERVAL THEN 1 ELSE 0 END), " +
                            "COUNT(*) " +
                            " FROM notifications " +
                            "WHERE recipient_id=?");
            select1.setLong(1, currentUserID);
            final ResultSet resultSet1 = select1.executeQuery();
            final List<Long> counters = new ArrayList<>();
            if (resultSet1.next()) {
                counters.add(resultSet1.getLong(1));
                counters.add(resultSet1.getLong(2));
                counters.add(resultSet1.getLong(3));
                counters.add(resultSet1.getLong(4));
            } else {
                return Optional.empty();
            }


            final PreparedStatement select2 = connection.prepareStatement(
                    "SELECT n.sender_id, n.recipient_id, u.firstname, u.lastname, n.action_id, n.ts_action " +
                            "FROM notifications n, users u " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<?::INTERVAL " +
                            "AND u.id=n.sender_id " +
                            "ORDER BY ts_action DESC " +
                            "LIMIT ? OFFSET ?");
            select2.setLong(1, currentUserID);
            select2.setString(2, interval);
            select2.setLong(3, count);
            select2.setLong(4, start_num);
            final ResultSet resultSet2 = select2.executeQuery();
            final List<Notification> notifications = new ArrayList<>();
            while (resultSet2.next()) {
                notifications.add(
                        Notification.builder()
                                .sender_id(resultSet2.getLong(1))
                                .recipient_id(resultSet2.getLong(2))
                                .firstName(resultSet2.getString(3))
                                .lastName(resultSet2.getString(4))
                                .not_status(resultSet2.getInt(5))
                                .ts_action(resultSet2.getTimestamp(6))
                                .build());
            }

            return Optional.of(ListNotifications
                    .builder()
                    .notificationList(notifications)
                    .countrs(counters)
                    .build());
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountNotificationsByInterval(long currentUserID, String interval) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM notifications " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<?::INTERVAL");
            select.setLong(1, currentUserID);
            select.setString(2, interval);
            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

}
