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
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static junit.framework.Assert.assertEquals;

public class ProfileServletTest {

    private User currentUser;

    @Before
    public void initValues(){
        currentUser = User.builder()
                .id(1)
                .firstName("testCurrentFirstName")
                .lastName("testCurrentLastName")
                .login("testCurrentUserLogin")
                .email("current.current@gmail.com")
                .dateOfBirth(LocalDate.of(1987,7,12))
                .gender(Gender.MALE)
                .build();
    }

    @Test
    public void getCurrentUserFromID() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final ProfileServlet profileServlet = new ProfileServlet(userService);
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(req.getServletPath()).thenReturn("/profile_1");
        when(userService.getById(1)).thenReturn(Optional.of(currentUser));
        when(req.getRequestDispatcher("/WEB-INF/profile.jsp")).thenReturn(dispatcher);

        profileServlet.doGet(req,resp);

        verify(req).setAttribute("userInfo",currentUser);
        verify(req).setAttribute("owner",true);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getOtherUserFromID() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final ProfileServlet profileServlet = new ProfileServlet(userService);
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);


        User user = User.builder()
                .id(10)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testUserLogin")
                .email("current.current@gmail.com")
                .dateOfBirth(LocalDate.of(1995,10,12))
                .gender(Gender.MALE)
                .build();

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(req.getServletPath()).thenReturn("/profile_10");
        when(userService.getById(10)).thenReturn(Optional.of(user));
        when(req.getRequestDispatcher("/WEB-INF/profile.jsp")).thenReturn(dispatcher);

        profileServlet.doGet(req,resp);

        verify(req).setAttribute("userInfo",user);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUserWithNonExistenID() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final ProfileServlet profileServlet = new ProfileServlet(userService);
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(req.getServletPath()).thenReturn("/profile_41");
        when(userService.getById(41)).thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        profileServlet.doGet(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUserWithInvalidRequest1() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final ProfileServlet profileServlet = new ProfileServlet(userService);
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(req.getServletPath()).thenReturn("/profile-10");
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        profileServlet.doGet(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getUserWithInvalidRequest2() throws ServletException, IOException {
        final UserService userService = mock(UserService.class);
        final ProfileServlet profileServlet = new ProfileServlet(userService);
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(req.getServletPath()).thenReturn("/profile_10aa");
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        profileServlet.doGet(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }
}
