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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static junit.framework.Assert.assertEquals;

public class UserServiceTest {

    //Бессмысленный тест
    @Test
    public void testCreateUser(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        UserService userService = new UserServiceImpl(userDao,securityService);

        User userIn = User.builder()
                .fullName("testFullName")
                .login("testLogin")
                .hashPassword(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build();

        Optional<User> userOut = Optional.of(User.builder()
                .id(100)
                .fullName("testFullName")
                .login("testLogin")
                .hashPassword(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build());
        when(userDao.createUser(userIn)).thenReturn(userOut);

        assertEquals(userOut,userService.createUser(userIn));
    }

    //Бессмысленный тест
    @Test
    public void testGetByCredentials(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        UserService userService = new UserServiceImpl(userDao,securityService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testPassword")
                .build();

        Optional<User> userOut = Optional.of(User.builder()
                .id(100)
                .fullName("testFullName")
                .login("testLogin")
                .hashPassword(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build());
        when(userDao.getByLogin(credentials.getLogin())).thenReturn(userOut);

        assertEquals(userOut,userService.getByCredentials(credentials));
    }


    @Test
    public void testCredentialsWithWrongPassword(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        UserService userService = new UserServiceImpl(userDao,securityService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testWrongPassword")
                .build();

        Optional<User> userOut = Optional.of(User.builder()
                .id(100)
                .fullName("testFullName")
                .login("testLogin")
                .hashPassword(securityService.encrypt("testPassword"))
                .dateOfBirth(LocalDate.of(1998,11,19))
                .gender(Gender.MALE)
                .build());
        when(userDao.getByLogin(credentials.getLogin())).thenReturn(userOut);

        assertEquals(Optional.empty(),userService.getByCredentials(credentials));
    }

    @Test
    public void testCredentialsWithNonExistenLogin(){
        UserDao userDao = mock(UserDao.class);
        SecurityService securityService = new SecurityServiceImpl();
        UserService userService = new UserServiceImpl(userDao,securityService);

        Credentials credentials = Credentials.builder()
                .login("testLogin")
                .password("testWrongPassword")
                .build();
        when(userDao.getByLogin(credentials.getLogin())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(),userService.getByCredentials(credentials));
    }

}
