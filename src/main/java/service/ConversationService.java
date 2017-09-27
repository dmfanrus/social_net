package service;

import model.Conversation;
import model.Message;

import java.util.List;
import java.util.Optional;

public interface ConversationService {

    void createConversation(long currentUserID, long otherUserID);
    void addMessage(Message message);
    Optional<List<Message>> getListMessages(long conv_id, long currentUserID);
    Optional<List<Conversation>> getListConversation(long currentUserID);
    void deleteMessage(long message_id);
    void deleteConversation(long conv_id);

}
