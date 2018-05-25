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

import static org.mockito.Mockito.*;

public class ChangerPasswordServletTest {


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
    public void UpdatePasswordWithValidData() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final NotificationService notificationService = mock(NotificationService.class);
        final ChangerPasswordServlet changerPasswordServlet = new ChangerPasswordServlet(securityService, userService, notificationService);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUserInSession);

        when(req.getParameter("currentPassword")).thenReturn("testCurrPassword");
        when(req.getParameter("newPassword")).thenReturn("testNewPassword");
        final Credentials currCredentials = Credentials.builder()
                .login("testCurrLogin")
                .password("testCurrPassword")
                .build();
        final Credentials newCredentials = Credentials.builder()
                .login("testCurrLogin")
                .password(securityService.encrypt("testNewPassword"))
                .build();
        final Optional<User> userOut = Optional.of(User.builder()
                .id(1)
                .firstName("Testfirstname")
                .lastName("Testlastname")
                .login("testCurrLogin")
                .email("test.test@test.test")
                .password(securityService.encrypt("testNewPassword"))
                .dateOfBirth(LocalDate.parse("1995-11-11"))
                .gender(Gender.MALE)
                .timeCreate(Timestamp.valueOf("2017-08-19 10:30:00"))
                .build());
        when(userService.validateUserByCurrentCredantials(currCredentials)).thenReturn(true);
        when(userService.updatePassword(newCredentials)).thenReturn(userOut);
        when(req.getRequestDispatcher("/WEB-INF/update_password.jsp")).thenReturn(dispatcher);
        changerPasswordServlet.doPost(req,resp);

        verify(session).setAttribute("user", userOut.get());
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req,resp);
    }


    @Test
    public void UpdatePasswordWithInvalidForm() throws ServletException, IOException {
        final SecurityService securityService = new SecurityServiceImpl();
        final UserService userService = mock(UserService.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final NotificationService notificationService = mock(NotificationService.class);
        final ChangerPasswordServlet changerPasswordServlet = new ChangerPasswordServlet(securityService, userService, notificationService);
        final HashMap<String,String> errors = new HashMap<>();
        final HashMap<String,String> fields = new HashMap<>();

        when(req.getParameter("currentPassword")).thenReturn("testCurrPassword");
        when(req.getParameter("newPassword")).thenReturn("testNewPassword");
        final Credentials credentials = Credentials.builder()
                .login("111testLogin")
                .password("testCurrPassword")
                .build();

        fields.put("login","testCurrLogin");
        fields.put("currentPassword","testCurrPassword");
        fields.put("newPassword","testNewPassword");

        errors.put("currentPassword","wrongPassword");


        when(req.getSession(false)).thenReturn(session);
        when(userService.validateUserByCurrentCredantials(credentials)).thenReturn(false);
        when(session.getAttribute("user")).thenReturn(currentUserInSession);
        when(req.getRequestDispatcher("/WEB-INF/update_password.jsp")).thenReturn(dispatcher);
        changerPasswordServlet.doPost(req,resp);

//        verify(req).setAttribute("fields", fields);
        verify(req).setAttribute("errors", errors);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req,resp);
    }

}
