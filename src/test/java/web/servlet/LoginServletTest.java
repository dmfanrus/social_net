package web.servlet;

import model.Credentials;
import model.Gender;
import model.User;
import org.junit.Test;
import service.UserService;
import web.servlet.utils.FormValidation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServletTest {

    @Test
    public void LoginWithValidCredentials() throws ServletException, IOException {

        final UserService userService = mock(UserService.class);
        final Credentials credentials = Credentials.builder().login("testLogin").password("testPassword").build();
        final Optional<User> user = Optional.of(User.builder()
                .fullName("Test Test Test")
                .login("testLogin")
                .gender(Gender.MALE)
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.of(1995,11,11))
                .build());
        when(userService.getByCredentials(credentials)).thenReturn(user);

        final LoginServlet loginServlet = new LoginServlet(userService);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);

        final HttpSession session = mock(HttpSession.class);
        when(req.getSession(true)).thenReturn(session);
        when(req.getParameter("username")).thenReturn("testLogin");
        when(req.getParameter("password")).thenReturn("testPassword");
        when(req.getContextPath()).thenReturn("/contextPath");
        loginServlet.doPost(req,resp);

        verify(session).setAttribute("user",user.get());
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/profile");
    }

    @Test
    public void LoginWithInvalidCredentials() throws ServletException, IOException {

        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final LoginServlet loginServlet = new LoginServlet(userService);
        final Credentials credentials = Credentials.builder().login("testWrongLogin").password("testWrongPassword").build();

        when(userService.getByCredentials(credentials)).thenReturn(Optional.empty());
        when(req.getParameter("username")).thenReturn("testWrongLogin");
        when(req.getParameter("password")).thenReturn("testWrongPassword");
        when(req.getRequestDispatcher("/WEB-INF/login.jsp")).thenReturn(dispatcher);
        loginServlet.doPost(req,resp);

        verify(req).setAttribute("data",credentials);
        verify(req).setAttribute("loginPass","invalid");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(req,resp);
    }

    @Test
    public void LoginWithInvalidForm() throws ServletException, IOException {

        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HashMap<String,String> errors = new HashMap<>();
        final LoginServlet loginServlet = new LoginServlet(userService);
        final Credentials credentials = Credentials.builder().login("11testWrongLogin").password("testWrongPassword").build();

        when(req.getParameter("username")).thenReturn("11testWrongLogin");
        when(req.getParameter("password")).thenReturn("testWrongPassword");
        when(req.getRequestDispatcher("/WEB-INF/login.jsp")).thenReturn(dispatcher);
        errors.put("login","loginWrong");
        loginServlet.doPost(req,resp);
        verify(req).setAttribute("data",credentials);
        verify(req).setAttribute("errors",errors);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(req,resp);
    }

}
