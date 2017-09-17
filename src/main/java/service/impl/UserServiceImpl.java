package service.impl;

import com.google.inject.Inject;
import dao.UserDao;
import model.Credentials;
import model.User;
import service.SecurityService;
import service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final SecurityService securityService;

    @Inject
    public UserServiceImpl(UserDao userDao, SecurityService securityService) {
        this.userDao = userDao;
        this.securityService = securityService;
    }

    @Override
    public Optional<User> createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return userDao.getByLogin(login);
    }

    @Override
    public Optional<User> getById(long id) {
        return userDao.getById(id);
    }

    @Override
    public Optional<User> getByCredentials(Credentials credentials) {
        Optional<User> user = userDao.getByLogin(credentials.getLogin());
        if (user.isPresent() && securityService.validate(credentials.getPassword(), user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public boolean checkExistByLogin(String login) {
        return userDao.getByLogin(login).isPresent();
    }

    @Override
    public Optional<List<User>> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Optional<Long> getCount() {
        return userDao.getCount();
    }

    @Override
    public Optional<Long> getCount(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return userDao.getCount(names[0]);
            } else if (names.length == 2) {
                return userDao.getCount(names[0], names[1]);
            } else {
                return Optional.empty();
            }
        } else {
            return userDao.getCount();
        }
    }


    @Override
    public Optional<List<User>> getUsers(String fullName, long start_num, long counts) {
        if (fullName != null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return userDao.getUsers(names[0], start_num, counts);
            } else if (names.length == 2) {
                return userDao.getUsers(names[0], names[1], start_num, counts);
            } else {
                return Optional.empty();
            }
        } else {
            return userDao.getUsers(start_num, counts);
        }
    }

}
