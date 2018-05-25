package dao.impl;

import dao.UserDao;
//import model.Gender;
import model.*;
import com.google.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
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
    public Optional<User> createUser(User user) {
        try (Connection connection = dataSource.getConnection()) {
            final String[] returning = {"id"};
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO users " +
                            "(firstname, lastname, login, hashpass, email, dateofbirth, gender, ts_create) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?)", returning);
            insert.setString(1, user.getFirstName());
            insert.setString(2, user.getLastName());
            insert.setString(3, user.getLogin());
            insert.setString(4, user.getPassword());
            insert.setString(5, user.getEmail());
            insert.setDate(6, Date.valueOf(user.getDateOfBirth()));
            insert.setString(7, user.getGender().name());
            insert.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            insert.executeUpdate();
            final ResultSet generatedKeys = insert.getGeneratedKeys();
            if (generatedKeys.next()) {
                return getCurrentUserWithAllInfoById(generatedKeys.getLong(1));
            } else {
                log.warn("Create user returned null with request({},{})", insert, insert.getWarnings());
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to create user", e);
            return Optional.empty();
        }
    }


    @Override
    public void deleteUserById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?");
            delete.setLong(1, id);
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }

    @Override
    public Optional<User> updateUserAllInfo(User user) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE users " +
                            "SET firstname=?, lastname=?, login=?, hashpass=?, email=?, " +
                            "dateofbirth=?, gender=? " +
                            "WHERE id=? RETURNING *");
            update.setString(1, user.getFirstName());
            update.setString(2, user.getLastName());
            update.setString(3, user.getLogin());
            update.setString(4, user.getPassword());
            update.setString(5, user.getEmail());
            update.setDate(6, Date.valueOf(user.getDateOfBirth()));
            update.setString(7, user.getGender().name());
            update.setLong(8, user.getId());
            update.executeUpdate();
            final ResultSet generatedKeys = update.getGeneratedKeys();
            if (generatedKeys.next()) {
                return Optional.of(
                        User.builder()
                                .id(generatedKeys.getLong(1))
                                .firstName(generatedKeys.getString(2))
                                .lastName(generatedKeys.getString(3))
                                .login(generatedKeys.getString(4))
                                .password(generatedKeys.getString(5))
                                .email(generatedKeys.getString(6))
                                .dateOfBirth(generatedKeys.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(generatedKeys.getString(8)))
                                .timeCreate(generatedKeys.getTimestamp(9))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateProfile(User user) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE users " +
                            "SET firstname=?, lastname=?, email=?, " +
                            "dateofbirth=?, gender=? " +
                            "WHERE login=? RETURNING *;");
            update.setString(1, user.getFirstName());
            update.setString(2, user.getLastName());
            update.setString(3, user.getEmail());
            update.setDate(4, Date.valueOf(user.getDateOfBirth()));
            update.setString(5, user.getGender().name());
            update.setString(6, user.getLogin());
            update.executeQuery();
            final ResultSet resultSet = update.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .password(resultSet.getString(5))
                                .email(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(8)))
                                .timeCreate(resultSet.getTimestamp(9))
                                .build());
            }

        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updatePassword(Credentials credentials) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE users SET hashpass=? WHERE login=? RETURNING *;");
            update.setString(1, credentials.getPassword());
            update.setString(2, credentials.getLogin());
            update.executeQuery();
            final ResultSet resultSet = update.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .password(resultSet.getString(5))
                                .email(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(8)))
                                .timeCreate(resultSet.getTimestamp(9))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUserWithAllInfoById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM users WHERE id=?");
            select.setLong(1, id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .password(resultSet.getString(5))
                                .email(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(8)))
                                .timeCreate(resultSet.getTimestamp(9))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUserWithAllInfoByLogin(String login) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM users WHERE login=?");
            select.setString(1, login);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .password(resultSet.getString(5))
                                .email(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(8)))
                                .timeCreate(resultSet.getTimestamp(9))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUserWithoutCredentialsByID(long id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, u.ts_create " +
                            "FROM users u WHERE u.id=?");
            select.setLong(1, id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        User.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .timeCreate(resultSet.getTimestamp(7))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }


    @Override
    public Optional<User> getOtherUserWithAllInfoById(long currentUserID, long otherUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.login, u.hashpass, u.email, " +
                            "u.dateofbirth, u.gender, u.ts_create, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE u.id=?");
            select.setLong(1, currentUserID);
            select.setLong(2, otherUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                long getId = resultSet.getLong(1);
                return Optional.of(
                        User.builder()
                                .id(getId)
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .login(resultSet.getString(4))
                                .password(resultSet.getString(5))
                                .email(resultSet.getString(6))
                                .dateOfBirth(resultSet.getDate(7).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(8)))
                                .timeCreate(resultSet.getTimestamp(9))
                                .relationStatus(getRelStatusByString(resultSet.getString(10), currentUserID, getId))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getOtherUserWithoutCredentialsByID(long currentUserID, long otherUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, u.ts_create, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE u.id=?");
            select.setLong(1, currentUserID);
            select.setLong(2, otherUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                long getId = resultSet.getLong(1);
                return Optional.of(
                        User.builder()
                                .id(getId)
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .timeCreate(resultSet.getTimestamp(7))
                                .relationStatus(getRelStatusByString(resultSet.getString(8), currentUserID, getId))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }


    @Override
    public Optional<Credentials> getCredentialsById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT id, login, hashpass FROM users WHERE id=?");
            select.setLong(1, id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        Credentials.builder()
                                .id(resultSet.getLong(1))
                                .login(resultSet.getString(2))
                                .password(resultSet.getString(3))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Credentials> getCredentialsByLogin(String login) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT id, login, hashpass FROM users WHERE login=?");
            select.setString(1, login);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        Credentials.builder()
                                .id(resultSet.getLong(1))
                                .login(resultSet.getString(2))
                                .password(resultSet.getString(3))
                                .build());
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }


    @Override
    public Optional<List<User>> getAllUsers(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=?");
            select.setLong(1, currentUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                long getId = resultSet.getLong(1);
                users.add(
                        User.builder()
                                .id(getId)
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .relationStatus(getRelStatusByString(resultSet.getString(7), currentUserID, getId))
                                .build());
            }
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountAllUsers() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users u");
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }


    @Override
    public Optional<List<User>> getSeveralUsers(long currentUserID, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, currentUserID);
            select.setLong(2, counts);
            select.setLong(3, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                long getId = resultSet.getLong(1);
                users.add(
                        User.builder()
                                .id(getId)
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .relationStatus(getRelStatusByString(resultSet.getString(7), currentUserID, getId))
                                .build());
            }
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getSeveralUsers(long currentUserID, String fullName, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE (u.firstname LIKE ? OR u.lastname LIKE ?) " +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, currentUserID);
            select.setString(2, fullName + "%");
            select.setString(3, fullName + "%");
            select.setLong(4, counts);
            select.setLong(5, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                long getId = resultSet.getLong(1);
                users.add(
                        User.builder()
                                .id(getId)
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .relationStatus(getRelStatusByString(resultSet.getString(7), currentUserID, getId))
                                .build());
            }
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountSeveralUsers(String fullName) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users u " +
                            "WHERE (u.firstname LIKE ? OR u.lastname LIKE ?) ");
            select.setString(1, fullName + "%");
            select.setString(2, fullName + "%");
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getSeveralUsers(long currentUserID, String firstName, String lastName, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE (u.firstname LIKE ? AND u.lastname LIKE ?) OR " +
                            "(u.lastname LIKE ? AND u.firstname LIKE ?) " +
                            "LIMIT ? OFFSET ?");
            select.setLong(1, currentUserID);
            select.setString(2, firstName + "%");
            select.setString(3, lastName);
            select.setString(4, lastName);
            select.setString(5, firstName + "%");
            select.setLong(6, counts);
            select.setLong(7, start_num);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                long getId = resultSet.getLong(1);
                users.add(
                        User.builder()
                                .id(getId)
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .email(resultSet.getString(4))
                                .dateOfBirth(resultSet.getDate(5).toLocalDate())
                                .gender(Gender.valueOf(resultSet.getString(6)))
                                .relationStatus(getRelStatusByString(resultSet.getString(7), currentUserID, getId))
                                .build());
            }
            return Optional.of(users);
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCountSeveralUsers(String firstName, String lastName) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users u " +
                            "WHERE (u.firstname LIKE ? AND u.lastname LIKE ?) OR " +
                            "(u.lastname LIKE ? AND u.firstname LIKE ?) ");
            select.setString(1, firstName + "%");
            select.setString(2, lastName);
            select.setString(3, lastName);
            select.setString(4, firstName + "%");
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
        }
        return Optional.empty();
    }


    private RelationStatus getRelStatusByString(String relationStatus, long currentUserID, long getUserID) {
        if (relationStatus == null) {
            if (currentUserID == getUserID) {
                return RelationStatus.ME;
            } else {
                return RelationStatus.UNKNOW;
            }
        } else {
            return RelationStatus.valueOf(relationStatus);
        }
    }


}
