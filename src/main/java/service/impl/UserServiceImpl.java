package service.impl;

import com.google.inject.Inject;
import dao.UserDao;
import model.Credentials;
import model.User;
import service.NotificationService;
import service.RelationshipService;
import service.SecurityService;
import service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    private final SecurityService securityService;
    private final RelationshipService relationshipService;
    private final NotificationService notificationService;

    @Inject
    public UserServiceImpl(UserDao userDao, SecurityService securityService, RelationshipService relationshipService,
                           NotificationService notificationService) {
        this.userDao = userDao;
        this.securityService = securityService;
        this.relationshipService = relationshipService;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<User> createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public Optional<User> getCurrentUserByLogin(String login) {
        return userDao.getCurrentUserWithAllInfoByLogin(login);
    }

    @Override
    public Optional<User> getCurrentUserById(long currentUserID) {
        return userDao.getCurrentUserWithAllInfoById(currentUserID);
    }

    @Override
    public Optional<User> getUserById(long currentUserID, long otherUserID) {
        return userDao.getOtherUserWithoutCredentialsByID(currentUserID, otherUserID);
    }

    @Override
    public Optional<User> getCurrentUserByCredentials(Credentials credentials) {
        Optional<User> user = userDao.getCurrentUserWithAllInfoByLogin(credentials.getLogin());
        if (user.isPresent() && securityService.validate(credentials.getPassword(), user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public boolean validateUserByCurrentCredantials(Credentials credentials) {
        Optional<Credentials> currentCredentials = userDao.getCredentialsByLogin(credentials.getLogin());
        if (currentCredentials.isPresent() && securityService.validate(credentials.getPassword(),currentCredentials.get().getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkExistByLogin(String login) {
        return userDao.getCredentialsByLogin(login).isPresent();
    }

    @Override
    public Optional<Long> getCount() {
        return userDao.getCountAllUsers();
    }

    @Override
    public Optional<Long> getCount(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return userDao.getCountSeveralUsers(names[0]);
            } else if (names.length == 2) {
                return userDao.getCountSeveralUsers(names[0], names[1]);
            } else {
                return Optional.empty();
            }
        } else {
            return userDao.getCountAllUsers();
        }
    }

    @Override
    public Optional<User> updateProfile(User userIn) {
        return userDao.updateProfile(userIn);
    }

    @Override
    public Optional<User> updatePassword(Credentials credentials) {
        return userDao.updatePassword(credentials);
    }

    @Override
    public Optional<List<User>> getUsers(long currentUserID) {
        return userDao.getAllUsers(currentUserID);
    }

    @Override
    public Optional<List<User>> getUsers(long currentUserID, long start_num, long counts) {
        return userDao.getSeveralUsers(currentUserID, start_num, counts);
    }

    @Override
    public Optional<List<User>> getUsers(long currentUserID, String fullName, long start_num, long counts) {

        if (fullName != null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return userDao.getSeveralUsers(currentUserID, names[0], start_num, counts);
            } else if (names.length == 2) {
                return userDao.getSeveralUsers(currentUserID, names[0], names[1], start_num, counts);
            } else {
                return Optional.empty();
            }
        } else {
            return userDao.getSeveralUsers(currentUserID, start_num, counts);
        }
    }

}
