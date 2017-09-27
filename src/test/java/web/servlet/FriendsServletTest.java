package web.servlet;

import model.Gender;
import model.RelationStatus;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FriendsServletTest {

    private final Long countUsers = 56L;
    private final int countUsersOnPage = 10;
    private User user;
    private final List<User> friends = new ArrayList<>();
    private final Long countPages = 6L;

    @Before
    public void initValues() {
        user = User.builder()
                .id(1)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .gender(Gender.MALE)
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.of(1995, 11, 11))
                .relationStatus(RelationStatus.ME)
                .build();
        for (int i = 0; i < 6; i++)
            friends.add(User.builder()
                    .firstName("testFirstName")
                    .lastName("testLastName")
                    .login("testLogin")
                    .gender(Gender.MALE)
                    .email("test.test@test.test")
                    .dateOfBirth(LocalDate.of(1995, 11, 11))
                    .relationStatus(RelationStatus.UNKNOW)
                    .build());
    }

    @Test
    public void enterUsersPage() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn(null);
        when(req.getParameter("fullName")).thenReturn(null);
        when(relationshipService.getCount(1,null)).thenReturn(Optional.of(countUsers));
        when(relationshipService.getFriends(1, null, 0, countUsersOnPage)).thenReturn(Optional.of(friends));
        when(req.getRequestDispatcher("/WEB-INF/friends.jsp")).thenReturn(dispatcher);

        friendsServlet.doGet(req, resp);

        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("fullName", null);
        verify(req).setAttribute("page", 1L);
        verify(req).setAttribute("usersList", friends);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void secondUsersPage() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("2");
        when(req.getParameter("fullName")).thenReturn(null);
        when(relationshipService.getCount(1,null)).thenReturn(Optional.of(countUsers));
        when(relationshipService.getFriends(1,null, 10, countUsersOnPage)).thenReturn(Optional.of(friends));
        when(req.getRequestDispatcher("/WEB-INF/friends.jsp")).thenReturn(dispatcher);

        friendsServlet.doGet(req, resp);

        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", friends);
        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("fullName", null);
        verify(req).setAttribute("page", 2L);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }


    @Test
    public void outOfRangeCountPages() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("20");
        when(req.getParameter("fullName")).thenReturn(null);
        when(relationshipService.getCount(1,null)).thenReturn(Optional.of(countUsers));
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        friendsServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getCountWithConnectionDBProblem() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(relationshipService.getCount(1,null)).thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);

        friendsServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersWithConnectionDBProblem() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("fullName")).thenReturn(null);
        when(req.getParameter("page")).thenReturn("3");
        when(req.getParameter("countPages")).thenReturn(Long.toString(countPages));
        when(relationshipService.getCount(1, null)).thenReturn(Optional.of(countUsers));
        when(relationshipService.getFriends(1, null,30, countUsersOnPage)).thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);

        friendsServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersPageWithNegativeNumberOrZero() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("0");
        when(req.getParameter("fullName")).thenReturn(null);
        when(relationshipService.getCount(1,null)).thenReturn(Optional.of(countUsers));
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        friendsServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersUsingFullNameInput() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("fullName")).thenReturn("Mihail");
        when(relationshipService.getCount(1, "Mihail")).thenReturn(Optional.of(countUsers));
        when(relationshipService.getFriends(1,"Mihail", 0, countUsersOnPage)).thenReturn(Optional.of(friends));
        when(req.getRequestDispatcher("/WEB-INF/friends.jsp")).thenReturn(dispatcher);

        friendsServlet.doPost(req, resp);

        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", friends);
        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("fullName", "Mihail");
        verify(req).setAttribute("page", 1L);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersUsingEmptyFullNameInputOnSecondPage() throws ServletException, IOException {
        final RelationshipService relationshipService = mock(RelationshipService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final FriendsServlet friendsServlet = new FriendsServlet(relationshipService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("fullName")).thenReturn("");
        when(relationshipService.getCount(1, "")).thenReturn(Optional.of(countUsers));
        when(relationshipService.getFriends(1, "", 0, countUsersOnPage)).thenReturn(Optional.of(friends));
        when(req.getRequestDispatcher("/WEB-INF/friends.jsp")).thenReturn(dispatcher);

        friendsServlet.doPost(req, resp);

        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", friends);
        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("fullName", "");
        verify(req).setAttribute("page", 1L);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }
}
