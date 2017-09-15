package service.impl;

import com.google.inject.Inject;
import dao.UserDao;
import model.Credentials;
import model.Gender;
import model.User;
import service.SecurityService;
import service.UserService;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    private final UserDao userDao;
    private final SecurityService securityService;

    @Inject
    public UserServiceImpl(UserDao userDao, SecurityService securityService) {
        this.userDao = userDao;
        this.securityService = securityService;
    }

    @Override
    public Optional<User> createUser(User user) { return userDao.createUser(user); }

    @Override
    public Optional<User> getByLogin(String login) { return userDao.getByLogin(login); }

    @Override
    public Optional<User> getById(long id) { return userDao.getById(id); }

    @Override
    public boolean checkExistByLogin(String login) {
        return userDao.getByLogin(login).isPresent();
    }

    @Override
    public List<Optional<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public Optional<List<User>> getUsers(long start_num, long counts) {
        return userDao.getUsers(start_num,counts);
    }

    @Override
    public Optional<Long> getCount() {
        return userDao.getCount();
    }

    @Override
    public Optional<Long> getCount(String fullName) {
        return userDao.getCount(fullName);
    }

    @Override
    public Optional<List<User>> getUsers(String fullName, long start_num, long counts) {
        return userDao.getUsers(fullName, start_num, counts);
    }

    @Override
    public Optional<User> getByCredentials(Credentials credentials) {
        Optional<User> user = userDao.getByLogin(credentials.getLogin());
        if(user.isPresent() && securityService.validate(credentials.getPassword(),user.get().getHashPassword())){
            return user;
        }
        return Optional.empty();
    }
}
