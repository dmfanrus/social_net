package dao.impl;

import com.google.inject.Inject;
import dao.ConversationDao;
import model.Conversation;
import model.Message;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConversationDaoImpl implements ConversationDao {

    private final DataSource dataSource;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ConversationDaoImpl.class);

    @Inject
    public ConversationDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addMessage(Message message) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select1 = connection.prepareStatement(
                    "SELECT * FROM conversations " +
                            "WHERE id=?");
            select1.setLong(1, message.getConv_id());
            select1.executeQuery();
            final ResultSet resultSet1 = select1.getResultSet();
            if (resultSet1.next()) {
                final PreparedStatement insert2 = connection.prepareStatement(
                        "INSERT INTO messages(sender_id,message,ts_action,conv_id,isread) " +
                                "VALUES(?,?,?,?,?)");
                insert2.setLong(1, message.getSender_id());
                insert2.setString(2, message.getMessage());
                insert2.setTimestamp(3, message.getTs_action());
                insert2.setLong(4, message.getConv_id());
                insert2.setBoolean(5, false);
                insert2.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Failed to add message", e);
        }
    }


    @Override
    public Optional<Long> createConversation(long currentUserID, long otherUserID, Message message) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement insert1 = connection.prepareStatement(
                    "INSERT INTO conversations(first_user_id, second_user_id) " +
                            "VALUES(?,?) " +
                            "ON CONFLICT " +
                            "DO NOTHING RETURNING id");
            insert1.setLong(1, currentUserID);
            insert1.setLong(2, otherUserID);
            insert1.executeQuery();

            ResultSet resultSet = insert1.getResultSet();
            if (resultSet.next()) {
                long conv_id = resultSet.getLong(1);

                final PreparedStatement insert2 = connection.prepareStatement(
                        "INSERT INTO messages(sender_id,message,ts_action,conv_id,isread) " +
                                "VALUES(?,?,?,?,?)");
                insert2.setLong(1, message.getSender_id());
                insert2.setString(2, message.getMessage());
                insert2.setTimestamp(3, message.getTs_action());
                insert2.setLong(4, conv_id);
                insert2.setBoolean(5, false);
                insert2.executeUpdate();

                return Optional.of(resultSet.getLong(1));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            log.error("Failed to add message", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Message>> getListMessages(long conv_id, long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select1 = connection.prepareStatement(
                    "SELECT * FROM conversations " +
                            "WHERE id=?");
            select1.setLong(1, conv_id);
            select1.executeQuery();
            final ResultSet resultSet1 = select1.getResultSet();
            if (resultSet1.next()) {

                final PreparedStatement update = connection.prepareStatement(
                        "UPDATE messages SET isread=TRUE " +
                                "WHERE conv_id=? AND isread=FALSE AND sender_id<>?");
                update.setLong(1, conv_id);
                update.setLong(2, currentUserID);
                update.executeUpdate();

                final PreparedStatement select2 = connection.prepareStatement(
                        "SELECT M.id, M.ts_action, M.message ,U.id , U.firstName,U.lastName, M.conv_id " +
                                "FROM users U, messages M " +
                                "WHERE M.sender_id=U.id AND M.conv_id=?" +
                                "ORDER BY M.id ASC");
                select2.setLong(1, conv_id);
                select2.executeQuery();
                final ResultSet resultSet2 = select2.getResultSet();
                final List<Message> messages = new ArrayList<>();
                while (resultSet2.next()) {
                    messages.add(
                            Message.builder()
                                    .id(resultSet2.getLong(1))
                                    .ts_action(resultSet2.getTimestamp(2))
                                    .message(resultSet2.getString(3))
                                    .sender_id(resultSet2.getLong(4))
                                    .firstName(resultSet2.getString(5))
                                    .lastName(resultSet2.getString(6))
                                    .conv_id(resultSet2.getLong(7))
                                    .build());
                }
                return Optional.of(messages);
            }
        } catch (SQLException e) {
            log.error("Failed to get list of messages", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Conversation>> getListConversation(long currentUserID) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT C.id AS chat_id, U.id AS other_id, U.firstName, U.lastName, " +
                            "SUM(CASE WHEN M.isread = FALSE AND M.sender_id = U.id AND C.id=M.conv_id THEN 1 ELSE 0 END) " +
                            "FROM users U,conversations C, messages M " +
                            "WHERE CASE WHEN C.first_user_id = ? THEN C.second_user_id = U.id " +
                            "WHEN C.second_user_id = ? THEN C.first_user_id= U.id END " +
                            "GROUP BY other_id, chat_id, firstName, lastName");
            select.setLong(1, currentUserID);
            select.setLong(2, currentUserID);
            select.executeQuery();
            final ResultSet resultSet = select.getResultSet();
            final List<Conversation> conversations = new ArrayList<>();
            while (resultSet.next()) {
                conversations.add(Conversation.builder().id(resultSet.getLong(1))
                        .otherUserID(resultSet.getLong(2))
                        .firstNameUser(resultSet.getString(3))
                        .lastNameUser(resultSet.getString(4))
                        .countUnread(resultSet.getLong(5))
                        .build());

            }
            return Optional.of(conversations);
        } catch (SQLException e) {
            log.error("Failed to get list of conversations", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteMessage(long message_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM messages WHERE id=?");
            delete.setLong(1, message_id);
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to delete message", e);
        }
    }

    @Override
    public void deleteConversation(long conv_id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM conversations WHERE id=?");
            delete.setLong(1, conv_id);
            delete.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to delete conversation", e);
        }
    }

    @Override
    public Optional<Long> getConversationID(long currentUserID, long otherUserID) {
        try (Connection connection = dataSource.getConnection()) {
            long conv_id;
            final PreparedStatement select1 = connection.prepareStatement(
                    "SELECT id FROM conversations " +
                            "WHERE (first_user_id=? AND second_user_id=?) " +
                            "OR (first_user_id=? AND second_user_id=?)");
            select1.setLong(1, currentUserID);
            select1.setLong(2, otherUserID);
            select1.setLong(3, otherUserID);
            select1.setLong(4, currentUserID);
            select1.executeQuery();

            final ResultSet resultSet = select1.getResultSet();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to get ID conversation", e);
        }
        return Optional.empty();
    }
}
