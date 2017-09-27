package service;

import dao.UserDao;
import model.Credentials;
import model.Gender;
import model.User;
import org.junit.Test;
import service.impl.SecurityServiceImpl;
import service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static junit.framework.Assert.assertEquals;

public class UserServiceTest {

    @Test
    public void testCreateUser(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        RelationshipService relationshipService = mock(RelationshipService.class);
        NotificationService notificationService = mock(NotificationService.class);
        UserService userService = new UserServiceImpl(userDao,securityService, relationshipService, notificationService);

        User userIn = User.builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build();

        Optional<User> userOut = Optional.of(User.builder()
                .id(100)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build());
        when(userDao.createUser(userIn)).thenReturn(userOut);

        assertEquals(userOut,userService.createUser(userIn));
    }

    @Test
    public void testGetByCredentials(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        RelationshipService relationshipService = mock(RelationshipService.class);
        NotificationService notificationService = mock(NotificationService.class);
        UserService userService = new UserServiceImpl(userDao,securityService, relationshipService, notificationService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testPassword")
                .build();

        Optional<User> userOut = Optional.of(User.builder()
                .id(100)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build());
        when(userDao.getCurrentUserWithAllInfoByLogin(credentials.getLogin())).thenReturn(userOut);

        assertEquals(userOut,userService.getCurrentUserByCredentials(credentials));
    }


    @Test
    public void testCredentialsWithWrongPassword(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        RelationshipService relationshipService = mock(RelationshipService.class);
        NotificationService notificationService = mock(NotificationService.class);
        UserService userService = new UserServiceImpl(userDao,securityService, relationshipService, notificationService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testWrongPassword")
                .build();

        Optional<User> userOut = Optional.of(User.builder()
                .id(100)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .password(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build());
        when(userDao.getCurrentUserWithAllInfoByLogin(credentials.getLogin())).thenReturn(userOut);

        assertEquals(Optional.empty(),userService.getCurrentUserByCredentials(credentials));
    }

    @Test
    public void testCredentialsWithNonExistenLogin(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        RelationshipService relationshipService = mock(RelationshipService.class);
        NotificationService notificationService = mock(NotificationService.class);
        UserService userService = new UserServiceImpl(userDao,securityService, relationshipService, notificationService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testWrongPassword")
                .build();
        when(userDao.getCurrentUserWithAllInfoByLogin(credentials.getLogin())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(),userService.getCurrentUserByCredentials(credentials));
    }


    @Test
    public void getUsersBy(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        RelationshipService relationshipService = mock(RelationshipService.class);
        NotificationService notificationService = mock(NotificationService.class);
        UserService userService = new UserServiceImpl(userDao,securityService, relationshipService, notificationService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testWrongPassword")
                .build();
        when(userDao.getCurrentUserWithAllInfoByLogin(credentials.getLogin())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(),userService.getCurrentUserByCredentials(credentials));
    }

}
