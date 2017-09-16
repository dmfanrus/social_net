package web.servlet;

import model.Gender;
import model.User;
import org.junit.Test;
import service.SecurityService;
import service.UserService;
import service.impl.SecurityServiceImpl;

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

public class RegistrationServletTest {

    @Test
    public void RegistrationWithValidData() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final RegistrationServlet registrationServlet = new RegistrationServlet(userService,securityService);

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("testLogin");
        when(req.getParameter("password")).thenReturn("testPassword");
        when(req.getParameter("email")).thenReturn("test.test@test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("1995-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        when(req.getSession(false)).thenReturn(session);

        final User userIn = User.builder()
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testLogin")
                .email("test.test@test.test")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .build();
        final Optional<User> userOut = Optional.of(User.builder()
                .id(1)
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testLogin")
                .email("test.test@test.test")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .build());

        when(userService.createUser(userIn)).thenReturn(userOut);
        when(req.getContextPath()).thenReturn("/contextPath");
        when(req.getSession(true)).thenReturn(session);
        registrationServlet.doPost(req,resp);

        verify(session).setAttribute("user", userOut.get());
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).sendRedirect("/contextPath/profile");
    }

    @Test
    public void RegistrationWithAlreadyExistLogin() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final HashMap<String,String> errors = new HashMap<>();
        final RegistrationServlet registrationServlet = new RegistrationServlet(userService,securityService);

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("testExistingLogin");
        when(req.getParameter("password")).thenReturn("testPassword");
        when(req.getParameter("email")).thenReturn("test.test@test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("1995-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        when(req.getSession(false)).thenReturn(session);
        when(req.getRequestDispatcher("/WEB-INF/registration.jsp")).thenReturn(dispatcher);


        final HashMap<String,String> fields = new HashMap<>();
        fields.put("firstName","Testfirstname");
        fields.put("lastName","Testlastname");
        fields.put("login","testExistingLogin");
        fields.put("password","testPassword");
        fields.put("email","test.test@test.test");
        fields.put("dateOfBirth","1995-11-11");
        fields.put("gender","M");

        when(userService.checkExistByLogin("testExistingLogin")).thenReturn(true);
        errors.put("login","loginNotUsed");
        registrationServlet.doPost(req,resp);

        verify(req).setAttribute("fields", fields);
        verify(req).setAttribute("errors", errors);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(req,resp);
    }

    @Test
    public void RegistrationWithInvalidForm() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final HashMap<String,String> errors = new HashMap<>();
        final RegistrationServlet registrationServlet = new RegistrationServlet(userService,securityService);

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("111testLogin");
        when(req.getParameter("password")).thenReturn("");
        when(req.getParameter("email")).thenReturn("test.test.test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("2010-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        when(req.getSession(false)).thenReturn(session);


        final HashMap<String,String> fields = new HashMap<>();
        fields.put("firstName","Testfirstname");
        fields.put("lastName","Testlastname");
        fields.put("login","111testLogin");
        fields.put("password","");
        fields.put("email","test.test.test.test");
        fields.put("dateOfBirth","2010-11-11");
        fields.put("gender","M");

        errors.put("login","loginWrong");
        errors.put("email","emailWrong");
        errors.put("password","empty");
        errors.put("dateOfBirth","tooYoung");
        when(req.getRequestDispatcher("/WEB-INF/registration.jsp")).thenReturn(dispatcher);
        registrationServlet.doPost(req,resp);

        verify(req).setAttribute("fields", fields);
        verify(req).setAttribute("errors", errors);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(req,resp);
    }

    @Test
    public void RegistrationWithValidDataAndProblemsWithConnectionDB() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final RegistrationServlet registrationServlet = new RegistrationServlet(userService,securityService);

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("testLogin");
        when(req.getParameter("password")).thenReturn("testPassword");
        when(req.getParameter("email")).thenReturn("test.test@test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("1995-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        when(req.getSession(false)).thenReturn(session);

        final User userIn = User.builder()
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testLogin")
                .email("test.test@test.test")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .build();

        when(userService.createUser(userIn)).thenReturn(Optional.empty());

        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);
        registrationServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req,resp);
    }
}
