package dao.impl;

import com.google.inject.Inject;
import dao.NotificationDao;
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
    public void AddNotification(Notification notification) {
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
        }
    }

    @Override
    public void deleteAllNotifications() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM notifications");
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }

    @Override
    public void deleteAllNotificationsMonthAgoAndMore() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM notifications WHERE (CURRENT_DATE-ts_action)>'1 month'::INTERVAL");
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }

    @Override
    public void deleteAllUserNotificationsMonthAgoAndMore(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM notifications WHERE recipient_id=? AND (CURRENT_DATE-ts_action)>'1 month'::INTERVAL");
            delete.setLong(1, recipient_id);
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }


    @Override
    public Optional<List<Notification>> getNotifications(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE recipient_id=?");
            select.setLong(1, recipient_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getNotificationsBySender(long sender_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE sender_id=?");
            select.setLong(1, sender_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountNotifications(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM relationships " +
                            "WHERE recipient_id=?");
            select.setLong(1, recipient_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsDuringDay(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<'1 day'::INTERVAL");
            select.setLong(1, recipient_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getAllNotificationsDuringMonth(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<'1 month'::INTERVAL");
            select.setLong(1, recipient_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getNotificationsDuringDay(long recipient_id, int start_num, int count) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<'1 day'::INTERVAL " +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, recipient_id);
            select.setInt(2, count);
            select.setInt(3, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountNotificationsDuringDay(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM relationships " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<'1 day'::INTERVAL");
            select.setLong(1, recipient_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getNotificationsDuringMonth(long recipient_id, int start_num, int count) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<'1 month'::INTERVAL " +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, recipient_id);
            select.setInt(2, count);
            select.setInt(3, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountNotificationsDuringMonth(long recipient_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM relationships " +
                            "WHERE recipient_id=? AND (CURRENT_DATE-ts_action)<'1 month'::INTERVAL");
            select.setLong(1, recipient_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Notification>> getNotificationsByDate(long recipient_id, Timestamp ts, int start_num, int count) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE recipient_id=? AND ts_action<?" +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, recipient_id);
            select.setTimestamp(2, ts);
            select.setInt(3, count);
            select.setInt(4, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
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
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountNotificationsByDate(long recipient_id, Timestamp ts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM relationships " +
                            "WHERE recipient_id=? AND ts_action<?");
            select.setLong(1, recipient_id);
            select.setTimestamp(2, ts);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }
}
