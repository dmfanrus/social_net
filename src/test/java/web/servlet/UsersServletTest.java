package web.servlet;

import model.Gender;
import model.User;
import org.junit.Before;
import org.junit.Test;
import service.UserService;

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

public class UsersServletTest {

    private final Long countUsers = 56L;
    private final int countUsersOnPage = 10;
    private User user;
    private final List<User> users = new ArrayList<>();
    private final Long countPages = 6L;

    @Before
    public void initValues() {
        user = User.builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .gender(Gender.MALE)
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.of(1995, 11, 11))
                .build();
        for (int i = 0; i < 6; i++)
            users.add(User.builder()
                    .firstName("testFirstName")
                    .lastName("testLastName")
                    .login("testLogin")
                    .gender(Gender.MALE)
                    .email("test.test@test.test")
                    .dateOfBirth(LocalDate.of(1995, 11, 11))
                    .build());
    }

    @Test
    public void enterUsersPage() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn(null);
        when(req.getParameter("fullName")).thenReturn(null);
        when(userService.getCount(null)).thenReturn(Optional.of(countUsers));
        when(userService.getUsers(null, 0, countUsersOnPage)).thenReturn(Optional.of(users));
        when(req.getRequestDispatcher("/WEB-INF/users.jsp")).thenReturn(dispatcher);

        usersServlet.doGet(req, resp);

        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("page", 1L);
        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", users);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void secondUsersPage() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("2");
        when(req.getParameter("fullName")).thenReturn(null);
        when(userService.getCount(null)).thenReturn(Optional.of(countUsers));
        when(userService.getUsers(null, 10, countUsersOnPage)).thenReturn(Optional.of(users));
        when(req.getRequestDispatcher("/WEB-INF/users.jsp")).thenReturn(dispatcher);

        usersServlet.doGet(req, resp);

        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("page", 2L);
        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", users);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }


    @Test
    public void outOfRangeCountPages() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("20");
        when(req.getParameter("fullName")).thenReturn(null);
        when(userService.getCount(null)).thenReturn(Optional.of(countUsers));
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        usersServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getCountWithConnectionDBProblem() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(userService.getCount(null)).thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);

        usersServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersWithConnectionDBProblem() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("fullName")).thenReturn(null);
        when(req.getParameter("page")).thenReturn("3");
        when(req.getParameter("countPages")).thenReturn(Long.toString(countPages));
        when(userService.getCount(null)).thenReturn(Optional.of(countUsers));
        when(userService.getUsers(null,30, countUsersOnPage)).thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);

        usersServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersPageWithNegativeNumberOrZero() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("0");
        when(req.getParameter("fullName")).thenReturn(null);
        when(userService.getCount(null)).thenReturn(Optional.of(countUsers));
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        usersServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersUsingFullNameInput() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("fullName")).thenReturn("Mihail");
        when(userService.getCount("Mihail")).thenReturn(Optional.of(countUsers));
        when(userService.getUsers("Mihail", 0, countUsersOnPage)).thenReturn(Optional.of(users));
        when(req.getRequestDispatcher("/WEB-INF/users.jsp")).thenReturn(dispatcher);

        usersServlet.doPost(req, resp);

        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("page", 1L);
        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", users);
        verify(req).setAttribute("fullName", "Mihail");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUsersUsingEmptyFullNameInputOnSecondPage() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final UsersServlet usersServlet = new UsersServlet(userService);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("fullName")).thenReturn("");
        when(userService.getCount("")).thenReturn(Optional.of(countUsers));
        when(userService.getUsers("", 0, countUsersOnPage)).thenReturn(Optional.of(users));
        when(req.getRequestDispatcher("/WEB-INF/users.jsp")).thenReturn(dispatcher);

        usersServlet.doPost(req, resp);

        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("page", 1L);
        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 5L);
        verify(req).setAttribute("usersList", users);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }
}
