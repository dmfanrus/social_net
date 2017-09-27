package web.servlet.changers;

import model.User;
import org.junit.Before;
import org.junit.Test;
import service.RelationshipService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangeFriendServletsTest {

    private final long currentUserID = 1;
    private final long otherUserID = 2;
    private final long numRow = 100;
    private User user;
    @Before
    public void initValues(){
        user = User.builder()
                .id(currentUserID)
                .firstName("testCurrentFullName")
                .lastName("testCurrentLastName")
                .build();
    }

    @Test
    public void sendFriendRequestTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("action")).thenReturn("sendRequest");
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/users");
    }

    @Test
    public void deleteFriendRequestFromMeTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getParameter("action")).thenReturn("deleteRequestFromMe");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(relationshipService).deleteFriendRequests(currentUserID,otherUserID);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/users");
    }

    @Test
    public void deleteFriendRequestFromOtherTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getParameter("action")).thenReturn("deleteRequestFromOther");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(relationshipService).deleteFriendRequests(currentUserID,otherUserID);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/users");
    }

    @Test
    public void confirmFriendRequestTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getParameter("action")).thenReturn("confirmRequest");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");


        changeRelationshipServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/users");
    }

    @Test
    public void deleteFriendFromFriendsPageTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("action")).thenReturn("deleteFriend");
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/friends/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/friends");
    }

    @Test
    public void blockUserTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getParameter("action")).thenReturn("blockUser");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/users");
    }

    @Test
    public void unblockUserTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getParameter("action")).thenReturn("unblockUser");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/users");
    }

    @Test
    public void wrongActionTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("otherUserID")).thenReturn(Long.toString(otherUserID));
        when(req.getParameter("action")).thenReturn("WrongAction");
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getServletPath()).thenReturn("/users/changer");

        changeRelationshipServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).sendRedirect("/contextPath/users");
    }


    @Test
    public void doGetChangerTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RelationshipService relationshipService = mock(RelationshipService.class);
        ChangeRelationshipServlet changeRelationshipServlet = new ChangeRelationshipServlet(relationshipService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);
        changeRelationshipServlet.doGet(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);

    }
}
