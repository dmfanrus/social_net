package dao.impl;

import com.google.inject.Inject;
import dao.FriendDao;
import model.Gender;
import model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendDaoImpl implements FriendDao{

    private final DataSource dataSource;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserDaoImpl.class);

    @Inject
    public FriendDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Optional<User>> getAllFriends(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id");
            select.setLong(1, currentUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<Optional<User>> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(Optional.of(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .email(resultSet.getString(5))
                                .dateOfBirth(resultSet.getDate(6).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(7)))
                                .build()));
            }
            return users;
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        log.error("Failed to get DS Connection");
        return new ArrayList<>();
    }

    @Override
    public Optional<List<User>> getFriends(long currentUserID, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id " +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, currentUserID);
            select.setLong(2, counts);
            select.setLong(3, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .email(resultSet.getString(5))
                                .dateOfBirth(resultSet.getDate(6).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(7)))
                                .build());
            }
            log.debug("Return array of users[{}]", users.size());
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get users by count", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getFriends(long currentUserID, String name, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id " +
                            "WHERE user_firstname LIKE ? OR user_lastname LIKE ? LIMIT ? OFFSET ?");
            select.setLong(1, currentUserID);
            select.setString(2, name + '%');
            select.setString(3, name + '%');
            select.setLong(4, counts);
            select.setLong(5, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .email(resultSet.getString(5))
                                .dateOfBirth(resultSet.getDate(6).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(7)))
                                .build());
            }
            log.debug("Return array of users[{}]", users.size());
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get users by string", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }


    @Override
    public Optional<List<User>> getFriends(long currentUserID, String firstName, String lastName, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id " +
                            "WHERE " +
                            "(user_firstname LIKE ? AND user_lastname LIKE ?) OR " +
                            "(user_lastname LIKE ? AND user_firstname LIKE ?) LIMIT ? OFFSET ?");
            select.setLong(1, currentUserID);
            select.setString(2, firstName + '%');
            select.setString(3, lastName);
            select.setString(4, firstName);
            select.setString(5, lastName + '%');
            select.setLong(6, counts);
            select.setLong(7, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .email(resultSet.getString(5))
                                .dateOfBirth(resultSet.getDate(6).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(7)))
                                .build());
            }
            log.debug("Return array of users[{}]", users.size());
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get users by string", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id ");
            select.setLong(1, currentUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get count of all users", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(long currentUserID, String name) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id " +
                            "WHERE user_firstname LIKE ? OR user_lastname LIKE ?");
            select.setLong(1, currentUserID);
            select.setString(2, name + '%');
            select.setString(3, name + '%');
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(long currentUserID, String firstName, String lastName) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users u " +
                            "INNER JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f ON f.friend_id=u.user_id " +
                            "WHERE " +
                            "(user_firstname LIKE ? AND user_lastname LIKE ?) OR " +
                            "(user_lastname LIKE ? AND user_firstname LIKE ?)");
            select.setLong(1, currentUserID);
            select.setString(2, firstName + '%');
            select.setString(3, lastName);
            select.setString(4, firstName);
            select.setString(5, lastName + '%');
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }
}
