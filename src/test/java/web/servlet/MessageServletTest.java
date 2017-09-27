package web.servlet;

import model.*;
import org.junit.Before;
import org.junit.Test;
import service.ConversationService;
import service.RelationshipService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageServletTest {


    private User user;
    private long currentUserID = 1;
    private final List<Conversation> conversations = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();
    private Message message;

    @Before
    public void initValues() {
        user = User.builder()
                .id(currentUserID)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .gender(Gender.MALE)
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.of(1995, 11, 11))
                .relationStatus(RelationStatus.ME)
                .build();

        for(int i=0;i<6;i++){
            conversations.add(Conversation.builder()
                    .id(i)
                    .firstNameUser("testOtherFirstName")
                    .lastNameUser("testOtherLastName")
                    .otherUserID(i)
                    .countUnread(i)
                    .build());
        }

        message = Message.builder()
                .sender_id(currentUserID)
                .firstName("testFirstName")
                .lastName("testLastName")
                .message("Hi")
                .build();

        for(int i=0;i<6;i++){
            messages.add(Message.builder()
                    .id(i)
                    .sender_id(currentUserID)
                    .firstName("testFirstName")
                    .lastName("testLastName")
                    .message(String.valueOf(i))
                    .build());
        }
    }

    @Test
    public void getMessagePage() throws ServletException, IOException {

        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final MessageServlet messageServlet = new MessageServlet(conversationService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getServletPath()).thenReturn("/message");
        when(conversationService.getListConversation(currentUserID)).thenReturn(Optional.of(conversations));
        when(req.getRequestDispatcher("/WEB-INF/message.jsp")).thenReturn(dispatcher);

        messageServlet.doGet(req, resp);

        verify(req).setAttribute("convList", conversations);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }


    @Test
    public void getConversation() throws ServletException, IOException {

        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final MessageServlet messageServlet = new MessageServlet(conversationService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getServletPath()).thenReturn("/message_1");
        when(conversationService.getListConversation(currentUserID)).thenReturn(Optional.of(conversations));
        when(conversationService.getListMessages(1, currentUserID)).thenReturn(Optional.of(messages));
        when(req.getRequestDispatcher("/WEB-INF/message.jsp")).thenReturn(dispatcher);

        messageServlet.doGet(req, resp);

        verify(req).setAttribute("convList", conversations);
        verify(req).setAttribute("msgList", messages);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }


    @Test
    public void sendMessage() throws ServletException, IOException {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final MessageServlet messageServlet = new MessageServlet(conversationService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(session.getAttribute("message")).thenReturn("Hi");
        when(req.getParameter("conv_id")).thenReturn("1");
        when(conversationService.getListConversation(currentUserID)).thenReturn(Optional.of(conversations));
        when(conversationService.getListMessages(1, currentUserID)).thenReturn(Optional.of(messages));
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/message_1");

        messageServlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/message_1");
    }

}
