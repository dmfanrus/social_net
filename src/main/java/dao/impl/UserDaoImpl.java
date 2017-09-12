package dao.impl;

import dao.UserDao;
//import model.Gender;
import model.Gender;
import model.User;
import service.SecurityService;
import com.google.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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
                    "SELECT user_id, user_fullname, user_email, user_gender, user_login, user_hashpass, user_dateofbirth " +
                            "FROM users " +
                            "WHERE users.user_login=?");
            select.setString(1, login);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getInt(1))
                                .fullName(resultSet.getString(2))
                                .email(resultSet.getString(3))
                                .gender(Gender.valueOf(resultSet.getString(4)))
                                .login(resultSet.getString(5))
                                .hashPassword(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
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
                    "SELECT user_fullname, user_email, user_gender, user_login, user_hashpass, user_dateofbirth " +
                            "FROM users " +
                            "WHERE users.user_id=?");
            select.setLong(1, id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(id)
                                .fullName(resultSet.getString("user_fullname"))
                                .email(resultSet.getString("user_email"))
                                .gender(Gender.valueOf(resultSet.getString("user_gender")))
                                .login(resultSet.getString("user_login"))
                                .hashPassword(resultSet.getString("user_hashpass"))
                                .dateOfBirth(resultSet.getDate("user_dateofbirth").toLocalDate())
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
                            "(user_fullname, user_email, user_gender, user_login, user_hashpass, user_dateofbirth) " +
                            "VALUES(?, ?, ?, ?, ?, ?)", returning);
            insert.setString(1, user.getFullName());
            insert.setString(2, user.getEmail());
            insert.setString(3, user.getGender().name());
            insert.setString(4, user.getLogin());
            insert.setString(5, user.getHashPassword());
            insert.setDate(6, Date.valueOf(user.getDateOfBirth()));
            insert.executeUpdate();
            final ResultSet generatedKeys = insert.getGeneratedKeys();
            if (generatedKeys.next()) {
                return getById(generatedKeys.getLong(1));
            } else {
                log.warn("Create user returned null with request({},{})",insert,insert.getWarnings());
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to create user", e);
        }
        log.error("Failed to get DS Connection");
        return Optional.empty();
    }

    @Override
    public List<Optional<User>> getAllUsers() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_fullname, user_email, user_gender, user_login, user_dateofbirth " +
                            "FROM users");
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<Optional<User>> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(Optional.of(
                        User.builder()
                                .id(resultSet.getLong("user_id"))
                                .fullName(resultSet.getString("user_fullname"))
                                .email(resultSet.getString("user_email"))
                                .gender(Gender.valueOf(resultSet.getString("user_gender")))
                                .login(resultSet.getString("user_login"))
                                .dateOfBirth(resultSet.getDate("user_dateofbirth").toLocalDate())
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
    public Optional<List<User>> getUsers(long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT user_id, user_fullname, user_email, user_gender, user_login, user_dateofbirth " +
                            "FROM users LIMIT ? OFFSET ?");
            select.setLong(1, counts);
            select.setLong(2, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        User.builder()
                                .id(resultSet.getLong("user_id"))
                                .fullName(resultSet.getString("user_fullname"))
                                .email(resultSet.getString("user_email"))
                                .gender(Gender.valueOf(resultSet.getString("user_gender")))
                                .login(resultSet.getString("user_login"))
                                .dateOfBirth(resultSet.getDate("user_dateofbirth").toLocalDate())
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
    public Optional<Long> getCount() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) " +
                            "FROM users");
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if(resultSet.next()) {
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


}
