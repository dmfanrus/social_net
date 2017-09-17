package service.impl;

import com.google.inject.Inject;
import dao.FriendDao;
import model.User;
import service.FriendService;

import java.util.List;
import java.util.Optional;

public class FriendServiceImpl implements FriendService{

    private final FriendDao friendDao;

    @Inject
    public FriendServiceImpl(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    @Override
    public Optional<Long> getCount(long currentUserID) {
        return friendDao.getCount(currentUserID);
    }

    @Override
    public List<Optional<User>> getFriends(long currentUserID) {
        return friendDao.getFriends(currentUserID);
    }


    @Override
    public Optional<Long> getCount(long currentUserID, String fullName) {
        if(fullName!=null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return friendDao.getCount(currentUserID, names[0]);
            } else if (names.length == 2) {
                return friendDao.getCount(currentUserID, names[0], names[1]);
            } else {
                return Optional.empty();
            }
        } else {
            return friendDao.getCount(currentUserID);
        }
    }

    @Override
    public Optional<List<User>> getFriends(long currentUserID, String fullName, long start_num, long counts) {
        if(fullName!=null && !fullName.isEmpty()){
            String[] names = fullName.split(" ", 2);
            if (names.length == 1) {
                return friendDao.getFriends(currentUserID, names[0], start_num, counts);
            } else if (names.length == 2) {
                return friendDao.getFriends(currentUserID, names[0], names[1], start_num, counts);
            } else {
                return Optional.empty();
            }
        } else {
            return friendDao.getFriends(currentUserID, start_num, counts);
        }
    }
}
