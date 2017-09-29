package service.impl;

import com.google.inject.Inject;
import dao.ConversationDao;
import model.Conversation;
import model.Message;
import service.ConversationService;

import java.util.List;
import java.util.Optional;

public class ConversationServiceImpl implements ConversationService{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final ConversationDao conversationDao;

    @Inject
    public ConversationServiceImpl(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }


    @Override
    public Optional<Long> createConversation(long currentUserID, long otherUserID, Message message) {
        return conversationDao.createConversation(currentUserID, otherUserID, message);
    }

    @Override
    public void addMessage(Message message) {
        conversationDao.addMessage(message);
    }

    @Override
    public Optional<List<Message>> getListMessages(long conv_id, long currentUserID) {
        return conversationDao.getListMessages(conv_id, currentUserID);
    }

    @Override
    public Optional<List<Conversation>> getListConversation(long currentUserID) {
        return conversationDao.getListConversation(currentUserID);
    }

    @Override
    public void deleteMessage(long message_id) {
        conversationDao.deleteMessage(message_id);
    }

    @Override
    public void deleteConversation(long conv_id) {
        conversationDao.deleteConversation(conv_id);
    }

    @Override
    public Optional<Long> getConversationID(long currentUserID, long otherUserID) {
        return conversationDao.getConversationID(currentUserID,otherUserID);
    }
}
