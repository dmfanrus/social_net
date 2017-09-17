package service;

import dao.FriendDao;
import model.Gender;
import model.User;
import org.junit.Before;
import org.junit.Test;
import service.impl.FriendServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;


public class FriendServiceTest {

    private final int start_num = 0;
    private final int countOnPage = 10;
    private final Long countFriends = 40L;
    private final long currentUserID = 1;
    private final List<User> friends = new ArrayList<>();

    @Before
    public void initValues() {
        for (int i = 0; i < 6; i++)
            friends.add(User.builder()
                    .firstName("testFirstName")
                    .lastName("testLastName")
                    .login("testLogin")
                    .gender(Gender.MALE)
                    .email("test.test@test.test")
                    .dateOfBirth(LocalDate.of(1995, 11, 11))
                    .build());
    }

    @Test
    public void getCountOfFriendsWithNullFullName(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getCount(currentUserID)).thenReturn(Optional.of(countFriends));

        assertEquals(Optional.of(countFriends),friendService.getCount(currentUserID,null));
    }

    @Test
    public void getCountOfFriendsWithName(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getCount(currentUserID,"Mark")).thenReturn(Optional.of(countFriends));

        assertEquals(Optional.of(countFriends),friendService.getCount(currentUserID,"Mark"));
    }
    @Test
    public void getCountOfFriendsWithFullName(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getCount(currentUserID,"Mark","Zuckerberg")).thenReturn(Optional.of(countFriends));

        assertEquals(Optional.of(countFriends),friendService.getCount(currentUserID,"Mark Zuckerberg"));
    }

    @Test
    public void getFriendsWithoutFullName(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getFriends(currentUserID,start_num,countOnPage)).thenReturn(Optional.of(friends));

        assertEquals(Optional.of(friends),friendService.getFriends(currentUserID,null,start_num,countOnPage));
    }

    @Test
    public void getFriendsWithName(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getFriends(currentUserID,"Mark", start_num,countOnPage)).thenReturn(Optional.of(friends));

        assertEquals(Optional.of(friends),
                friendService.getFriends(currentUserID,"Mark",start_num,countOnPage));
    }

    @Test
    public void getFriendsWithFullName(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getFriends(currentUserID, "Mark", "Zuckerberg",start_num,countOnPage))
                .thenReturn(Optional.of(friends));

        assertEquals(Optional.of(friends),
                friendService.getFriends(currentUserID,"Mark Zuckerberg",start_num,countOnPage));
    }


    @Test
    public void getFriendsIfFullNameSplitOnThreePart(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getFriends(currentUserID, "Mark", "el Zuckerberg",start_num,countOnPage))
                .thenReturn(Optional.empty());

        assertEquals(Optional.empty(),
                friendService.getFriends(currentUserID,"Mark el Zuckerberg",start_num,countOnPage));
    }

    @Test
    public void getCountIfFullNameSplitOnThreePart(){
        FriendDao friendDao = mock(FriendDao.class);
        FriendService friendService = new FriendServiceImpl(friendDao);
        when(friendDao.getCount(currentUserID, "Mark", "el Zuckerberg"))
                .thenReturn(Optional.of(0L));
        assertEquals(Optional.of(0L),friendService.getCount(currentUserID,"Mark el Zuckerberg"));
    }
}
