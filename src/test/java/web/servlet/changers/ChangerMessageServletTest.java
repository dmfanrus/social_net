package web.servlet.changers;

import model.Gender;
import model.RelationStatus;
import model.User;
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
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangerMessageServletTest {

    private final long currentUserID = 1;
    private final long otherUserID = 2;
    private final long conv_id = 1;
    private final long msg_id = 1;
    private User user;
    @Before
    public void initValues(){
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
    }

    @Test
    public void deleteConversation() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final ChangerMessageServlet changerMessageServlet = new ChangerMessageServlet(conversationService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("conv_id")).thenReturn(String.valueOf(conv_id));
        when(req.getParameter("action")).thenReturn("deleteConversation");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/message");

        changerMessageServlet.doPost(req,resp);

        verify(conversationService).deleteConversation(conv_id);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/message");
    }

    @Test
    public void deleteMessage() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final ChangerMessageServlet changerMessageServlet = new ChangerMessageServlet(conversationService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("msg_id")).thenReturn(String.valueOf(msg_id));
        when(req.getParameter("conv_id")).thenReturn(String.valueOf(conv_id));
        when(req.getParameter("action")).thenReturn("deleteMessage");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/message_1");

        changerMessageServlet.doPost(req,resp);

        verify(conversationService).deleteMessage(msg_id);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/message_1");

    }


    @Test
    public void wrongActionTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final ChangerMessageServlet changerMessageServlet = new ChangerMessageServlet(conversationService);
        HttpSession session = mock(HttpSession.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("action")).thenReturn("WrongAction");
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        changerMessageServlet.doGet(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }


    @Test
    public void doGetChangerTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        final ConversationService conversationService = mock(ConversationService.class);
        final ChangerMessageServlet changerMessageServlet = new ChangerMessageServlet(conversationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);
        changerMessageServlet.doGet(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);

    }
}
