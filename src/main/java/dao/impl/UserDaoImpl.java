package dao.impl;

import dao.UserDao;
//import model.Gender;
import model.Gender;
import model.User;
import com.google.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Михаил on 06.01.2017.
 */
public class UserDaoImpl implements UserDao {

    private final DataSource dataSource;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserDaoImpl.class);


    @Inject
    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> getByLogin(String login) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_hashpass, user_email, user_dateofbirth, user_gender " +
                            "FROM users " +
                            "WHERE users.user_login=?");
            select.setString(1, login);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getInt(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .password(resultSet.getString(5))
                                .email(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(8)))
                                .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get user by Login", e);
            e.printStackTrace();
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users " +
                            "WHERE users.user_id=?");
            select.setLong(1, id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(id)
                                .firstName(resultSet.getString(1))
                                .lastName(resultSet.getString(2))
                                .login(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get user by ID", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<User> createUser(User user) {
        try (Connection connection = dataSource.getConnection()) {
            final String[] returning = {"user_id"};
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO users " +
                            "(user_firstname, user_lastname, user_login, user_hashpass, user_email, user_dateofbirth, user_gender) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)", returning);
            insert.setString(1, user.getFirstName());
            insert.setString(2, user.getLastName());
            insert.setString(3, user.getLogin());
            insert.setString(4, user.getPassword());
            insert.setString(5, user.getEmail());
            insert.setDate(6, Date.valueOf(user.getDateOfBirth()));
            insert.setString(7, user.getGender().name());
            insert.executeUpdate();
            final ResultSet generatedKeys = insert.getGeneratedKeys();
            if (generatedKeys.next()) {
                return getById(generatedKeys.getLong(1));
            } else {
                log.warn("Create user returned null with request({},{})", insert, insert.getWarnings());
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to create user", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getUsers() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users");
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
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getUsers(long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users LIMIT ? OFFSET ?");
            select.setLong(1, counts);
            select.setLong(2, start_num);
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
    public Optional<List<User>> getUsers(String name, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users WHERE user_firstname LIKE ? OR user_lastname LIKE ? LIMIT ? OFFSET ?");
            select.setString(1, name + '%');
            select.setString(2, name + '%');
            select.setLong(3, counts);
            select.setLong(4, start_num);
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
    public Optional<List<User>> getUsers(String firstName, String lastName, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_firstname, user_lastname, user_login, user_email, user_dateofbirth, user_gender " +
                            "FROM users WHERE " +
                            "(user_firstname LIKE ? AND user_lastname LIKE ?) OR " +
                            "(user_lastname LIKE ? AND user_firstname LIKE ?) LIMIT ? OFFSET ?");
            select.setString(1, firstName + '%');
            select.setString(2, lastName);
            select.setString(3, firstName);
            select.setString(4, lastName + '%');
            select.setLong(5, counts);
            select.setLong(6, start_num);
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
    public Optional<Long> getCount() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users");
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
    public Optional<Long> getCount(String name) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users WHERE " +
                            "user_firstname LIKE ? OR user_lastname LIKE ?");

            select.setString(1, name + '%');
            select.setString(2, name + '%');
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
    public Optional<Long> getCount(String firstName, String lastName) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users WHERE " +
                            "(user_firstname LIKE ? AND user_lastname LIKE ?) OR " +
                            "(user_lastname LIKE ? AND user_firstname LIKE ?)");

            select.setString(1, firstName + '%');
            select.setString(2, lastName);
            select.setString(3, firstName);
            select.setString(4, lastName + '%');
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
