package web.servlet.changers;

import model.Credentials;
import model.Gender;
import model.User;
import org.junit.Before;
import org.junit.Test;
import service.NotificationService;
import service.SecurityService;
import service.UserService;
import service.impl.SecurityServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangerProfileServletTest {


    private User currentUserInSession;

    @Before
    public void initValues(){
        final SecurityService securityService = new SecurityServiceImpl();
        currentUserInSession = User.builder()
                .firstName("TestCurrfirstname")
                .lastName("TestCurrlastname")
                .login("testCurrLogin")
                .password(securityService.encrypt("testCurrPassword"))
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .timeCreate(Timestamp.valueOf("2017-08-19 10:30:00"))
                .build();
    }

    @Test
    public void UpdateProfileWithValidData() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final NotificationService notificationService = mock(NotificationService.class);
        final ChangerProfileServlet changerProfileServlet = new ChangerProfileServlet(securityService, userService, notificationService);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUserInSession);

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("testLogin");
        when(req.getParameter("currentPassword")).thenReturn("testCurrPassword");
        when(req.getParameter("email")).thenReturn("test.test@test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("1995-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        final Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testCurrPassword")
                .build();

        final User userIn = User.builder()
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testLogin")
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .build();
        final Optional<User> userOut = Optional.of(User.builder()
                .id(1)
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testLogin")
                .email("test.test@test.test")
                .password(securityService.encrypt("testCurrPassword"))
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .timeCreate(Timestamp.valueOf("2017-08-19 10:30:00"))
                .build());
        when(userService.validateUserByCurrentCredantials(credentials)).thenReturn(true);
        when(userService.updateProfile(userIn)).thenReturn(userOut);
        when(req.getRequestDispatcher("/WEB-INF/update_profile.jsp")).thenReturn(dispatcher);
        changerProfileServlet.doPost(req,resp);

        verify(session).setAttribute("user", userOut.get());
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req,resp);
    }


    @Test
    public void UpdateWithInvalidForm() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final NotificationService notificationService = mock(NotificationService.class);
        final ChangerProfileServlet changerProfileServlet = new ChangerProfileServlet(securityService, userService, notificationService);
        final HashMap<String,String> errors = new HashMap<>();
        final HashMap<String,String> fields = new HashMap<>();

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("111testLogin");
        when(req.getParameter("currentPassword")).thenReturn("testCurrPassword");
        when(req.getParameter("email")).thenReturn("test.test.test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("2010-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        final Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testCurrPassword")
                .build();

        fields.put("firstName","Testfirstname");
        fields.put("lastName","Testlastname");
        fields.put("login","111testLogin");
        fields.put("currentPassword","testCurrPassword");
        fields.put("email","test.test.test.test");
        fields.put("dateOfBirth","2010-11-11");
        fields.put("gender","M");

        errors.put("login","loginWrong");
        errors.put("email","emailWrong");
        errors.put("dateOfBirth","tooYoung");
        errors.put("password","wrongPassword");


        when(req.getSession(false)).thenReturn(session);
        when(userService.validateUserByCurrentCredantials(credentials)).thenReturn(false);
        when(session.getAttribute("user")).thenReturn(currentUserInSession);
        when(req.getRequestDispatcher("/WEB-INF/update_profile.jsp")).thenReturn(dispatcher);
        changerProfileServlet.doPost(req,resp);

        verify(req).setAttribute("errors", errors);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req,resp);
    }

    @Test
    public void updateProfileWithProblemWithConnectionDB() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final NotificationService notificationService = mock(NotificationService.class);
        final ChangerProfileServlet changerProfileServlet = new ChangerProfileServlet(securityService, userService, notificationService);

        when(req.getParameter("firstName")).thenReturn("Testfirstname");
        when(req.getParameter("lastName")).thenReturn("Testlastname");
        when(req.getParameter("username")).thenReturn("testLogin");
        when(req.getParameter("currentPassword")).thenReturn("testCurrPassword");
        when(req.getParameter("email")).thenReturn("test.test@test.test");
        when(req.getParameter("dateOfBirth")).thenReturn("1995-11-11");
        when(req.getParameter("gender")).thenReturn("M");
        final Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testCurrPassword")
                .build();
        final User userIn = User.builder()
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testLogin")
                .email("test.test@test.test")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .build();

        when(req.getSession(false)).thenReturn(session);
        when(userService.validateUserByCurrentCredantials(credentials)).thenReturn(true);
        when(session.getAttribute("user")).thenReturn(currentUserInSession);
        when(userService.updateProfile(userIn)).thenReturn(Optional.empty());

        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);
        changerProfileServlet.doPost(req,resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req,resp);
    }
}
