package dao.impl;

import com.google.inject.Inject;
import dao.RelationshipDao;
import model.Gender;
import model.RelationStatus;
import model.Relationship;
import model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelationshipDaoImpl implements RelationshipDao {

    private final DataSource dataSource;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserDaoImpl.class);

    @Inject
    public RelationshipDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void addRelationships(Relationship relationship) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO relationships(first_user_id, second_user_id, rel_status, ts_action) " +
                            "VALUES(?,?,?::relationship_status,?) ON CONFLICT DO NOTHING");
            insert.setLong(1, relationship.getFirstUserID());
            insert.setLong(2, relationship.getSecondUserID());
            insert.setString(3, relationship.getRelationStatus().name());
            insert.setTimestamp(4, relationship.getTimeAction());
            insert.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }

    @Override
    public void deleteRelationships(long firstUserID, long secondUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM relationships " +
                            "WHERE (first_user_id = ? AND second_user_id = ?) " +
                            "OR (first_user_id = ? AND second_user_id = ?)");
            delete.setLong(1, firstUserID);
            delete.setLong(2, secondUserID);
            delete.setLong(3, secondUserID);
            delete.setLong(4, firstUserID);
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }

    @Override
    public void updateRelatioships(Relationship relationship) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE relationships " +
                            "SET rel_status=?::relationship_status, ts_action=? " +
                            "WHERE first_user_id=? AND second_user_id=?");
            update.setString(1, relationship.getRelationStatus().name());
            update.setTimestamp(2, relationship.getTimeAction());
            update.setLong(3, relationship.getFirstUserID());
            update.setLong(4, relationship.getSecondUserID());
            update.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
    }

    @Override
    public Optional<List<Relationship>> getAllRelationships() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships");
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<Relationship> relationships = new ArrayList<>();
            while (resultSet.next()) {
                relationships.add(
                        Relationship.builder()
                                .firstUserID(resultSet.getLong(1))
                                .secondUserID(resultSet.getLong(2))
                                .relationStatus(RelationStatus.valueOf(resultSet.getString(3)))
                                .ts_action(resultSet.getTimestamp(4))
                                .build());
            }
            return Optional.of(relationships);
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Relationship>> getCurrentUserRelationship(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships WHERE first_user_id=?");
            select.setLong(1, currentUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<Relationship> relationships = new ArrayList<>();
            while (resultSet.next()) {
                relationships.add(
                        Relationship.builder()
                                .firstUserID(resultSet.getLong(1))
                                .secondUserID(resultSet.getLong(2))
                                .relationStatus(RelationStatus.valueOf(resultSet.getString(3)))
                                .ts_action(resultSet.getTimestamp(4))
                                .build());
            }
            return Optional.of(relationships);
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Relationship>> getRelationshipWithTwoUsers(long firstUserID, long secondUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM relationships " +
                            "WHERE (first_user_id=? AND second_user_id=?) OR (first_user_id=? AND second_user_id=?)");
            select.setLong(1, firstUserID);
            select.setLong(2, secondUserID);
            select.setLong(3, secondUserID);
            select.setLong(4, firstUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<Relationship> relationships = new ArrayList<>();
            while (resultSet.next()) {
                relationships.add(
                        Relationship.builder()
                                .firstUserID(resultSet.getLong(1))
                                .secondUserID(resultSet.getLong(2))
                                .relationStatus(RelationStatus.valueOf(resultSet.getString(3)))
                                .ts_action(resultSet.getTimestamp(4))
                                .build());
            }
            return Optional.of(relationships);
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }


    @Override
    public Optional<List<User>> getAllFriends(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "LEFT JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? AND r.rel_status='FRIEND'");
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
    public Optional<Long> getCountAllFriends(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM  relationships " +
                            "WHERE first_user_id=? AND rel_status='FRIEND'");
            select.setLong(1, currentUserID);
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
    public Optional<List<User>> getSeveralFriends(long currentUserID, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "INNER JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? AND r.rel_status='FRIEND' " +
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
    public Optional<List<User>> getSeveralFriends(long currentUserID, String fullName, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "INNER JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE r.rel_status='FRIEND' " +
                            "AND (u.firstname LIKE ? OR u.lastname LIKE ?) " +
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
    public Optional<Long> getCountSeveralFriends(long currentUserID, String fullName) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users u " +
                            "INNER JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE r.rel_status='FRIEND' " +
                            "AND (u.firstname LIKE ? OR u.lastname LIKE ?) ");
            select.setLong(1, currentUserID);
            select.setString(2, fullName + "%");
            select.setString(3, fullName + "%");
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
    public Optional<List<User>> getSeveralFriends(long currentUserID, String firstName, String lastName, long start_num, long counts) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT u.id, u.firstname, u.lastname, u.email, " +
                            "u.dateofbirth, u.gender, r.rel_status, r.ts_action " +
                            "FROM users u " +
                            "INNER JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE r.rel_status='FRIEND' " +
                            "AND ((u.firstname LIKE ? AND u.lastname LIKE ?) OR " +
                            "(u.lastname LIKE ? AND u.firstname LIKE ?)) " +
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
    public Optional<Long> getCountSeveralFriends(long currentUserID, String firstName, String lastName) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users u " +
                            "INNER JOIN relationships r " +
                            "ON u.id=r.second_user_id AND r.first_user_id=? " +
                            "WHERE r.rel_status='FRIEND' " +
                            "AND ((u.firstname LIKE ? AND u.lastname LIKE ?) OR " +
                            "(u.lastname LIKE ? AND u.firstname LIKE ?)) ");
            select.setLong(1, currentUserID);
            select.setString(2, firstName + "%");
            select.setString(3, lastName);
            select.setString(4, lastName);
            select.setString(5, firstName + "%");
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
    public Optional<RelationStatus> getRelationStatusWithTwoUsers(long currentUserID, long otherUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT rel_status FROM relationships " +
                            "WHERE (first_user_id=? AND second_user_id=?)");
            select.setLong(1, currentUserID);
            select.setLong(2, otherUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(RelationStatus.valueOf(resultSet.getString(1)));
            } else {
                return Optional.of(RelationStatus.UNKNOW);
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<RelationStatus> getRelationshipWithTwoUsersByConvID(long conv_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT R.rel_status FROM relationships R, conversations C " +
                            "WHERE C.id = ? AND R.second_user_id = C.second_user_id");
            select.setLong(1, conv_id);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            if (resultSet.next()) {
                return Optional.of(RelationStatus.valueOf(resultSet.getString(1)));
            } else {
                return Optional.of(RelationStatus.UNKNOW);
            }
        } catch (SQLException e) {
            log.error("Failed to get count of users with fullname", e);
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
