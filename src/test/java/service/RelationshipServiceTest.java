package service;

import dao.RelationshipDao;
import model.Gender;
import model.User;
import org.junit.Before;
import org.junit.Test;
import service.impl.RelationshipServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;


public class RelationshipServiceTest {

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
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getCountAllFriends(currentUserID)).thenReturn(Optional.of(countFriends));

        assertEquals(Optional.of(countFriends), relationshipService.getCount(currentUserID,null));
    }

    @Test
    public void getCountOfFriendsWithName(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getCountSeveralFriends(currentUserID,"Mark")).thenReturn(Optional.of(countFriends));

        assertEquals(Optional.of(countFriends), relationshipService.getCount(currentUserID,"Mark"));
    }
    @Test
    public void getCountOfFriendsWithFullName(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getCountSeveralFriends(currentUserID,"Mark","Zuckerberg"))
                .thenReturn(Optional.of(countFriends));

        assertEquals(Optional.of(countFriends), relationshipService.getCount(currentUserID,"Mark Zuckerberg"));
    }

    @Test
    public void getFriendsWithoutFullName(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getSeveralFriends(currentUserID,start_num,countOnPage))
                .thenReturn(Optional.of(friends));

        assertEquals(Optional.of(friends), relationshipService.getFriends(currentUserID,null,start_num,countOnPage));
    }

    @Test
    public void getFriendsWithName(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getSeveralFriends(currentUserID,"Mark", start_num,countOnPage))
                .thenReturn(Optional.of(friends));

        assertEquals(Optional.of(friends),
                relationshipService.getFriends(currentUserID,"Mark",start_num,countOnPage));
    }

    @Test
    public void getFriendsWithFullName(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getSeveralFriends(currentUserID, "Mark", "Zuckerberg",start_num,countOnPage))
                .thenReturn(Optional.of(friends));

        assertEquals(Optional.of(friends),
                relationshipService.getFriends(currentUserID,"Mark Zuckerberg",start_num,countOnPage));
    }


    @Test
    public void getFriendsIfFullNameSplitOnThreePart(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getSeveralFriends(currentUserID, "Mark", "el Zuckerberg",start_num,countOnPage))
                .thenReturn(Optional.empty());

        assertEquals(Optional.empty(),
                relationshipService.getFriends(currentUserID,"Mark el Zuckerberg",start_num,countOnPage));
    }

    @Test
    public void getCountIfFullNameSplitOnThreePart(){
        RelationshipDao relationshipDao = mock(RelationshipDao.class);
        NotificationService notificationService = mock(NotificationService.class);
        RelationshipService relationshipService = new RelationshipServiceImpl(relationshipDao, notificationService);
        when(relationshipDao.getCountSeveralFriends(currentUserID, "Mark", "el Zuckerberg"))
                .thenReturn(Optional.of(0L));
        assertEquals(Optional.of(0L), relationshipService.getCount(currentUserID,"Mark el Zuckerberg"));
    }
}
