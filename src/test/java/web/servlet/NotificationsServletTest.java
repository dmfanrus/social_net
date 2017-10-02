package web.servlet;

import model.*;
import org.junit.Before;
import org.junit.Test;
import service.NotificationService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationsServletTest{

    private final Long countNotifications = 21L;
    private final int countNotificationsOnPage = 10;
    private User user;
    private final List<Notification> notifications = new ArrayList<>();
    private final Long countPages = 3L;
    private final long currentUserID = 1;
    private ListNotifications listNotifications;
    private final List<Long> counters = Arrays.asList(0L,10L,10L,10L);
    private final Map<String,String> mapTimeInterval = new HashMap<>();

    @Before
    public void initValues() {
        user = User.builder()
                .id(1)
                .firstName("testFirstName")
                .lastName("testLastName")
                .login("testLogin")
                .gender(Gender.MALE)
                .email("test.test@test.test")
                .dateOfBirth(LocalDate.of(1995, 11, 11))
                .relationStatus(RelationStatus.ME)
                .build();
        for (int i = 0; i < 10; i++){
            notifications.add(Notification.builder()
                    .id(i)
                    .sender_id(i)
                    .recipient_id(i+1)
                    .firstName("testFirstName")
                    .lastName("testLastName")
                    .not_status(18)
                    .build());
        }
        listNotifications = ListNotifications.builder()
                .notificationList(notifications)
                .countrs(counters)
                .build();

        mapTimeInterval.put("day","1 day");
        mapTimeInterval.put("week","1 week");
        mapTimeInterval.put("month","1 month");
        mapTimeInterval.put("year","1 year");

    }

    @Test
    public void enterNotificationsPage() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet= new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn(null);
        when(req.getParameter("setInterval")).thenReturn("day");
        when(notificationService.getCountNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("day")))
                .thenReturn(Optional.of(countNotifications));
        when(notificationService.getNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("day"),0,10))
                .thenReturn(Optional.of(listNotifications));
        when(req.getRequestDispatcher("/WEB-INF/notifications.jsp")).thenReturn(dispatcher);

        notificationServlet.doGet(req, resp);

        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 3L);
        verify(req).setAttribute("notificationsList", listNotifications);
        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("activeInterval", "day");
        verify(req).setAttribute("page", 1L);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void selectWeekInterval() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet= new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("1");
        when(req.getParameter("setInterval")).thenReturn("week");
        when(notificationService.getCountNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week")))
                .thenReturn(Optional.of(countNotifications));
        when(notificationService.getNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week"),0,10))
                .thenReturn(Optional.of(listNotifications));
        when(req.getRequestDispatcher("/WEB-INF/notifications.jsp")).thenReturn(dispatcher);

        notificationServlet.doGet(req, resp);

        verify(req).setAttribute("startPage", 1L);
        verify(req).setAttribute("endPage", 3L);
        verify(req).setAttribute("notificationsList", listNotifications);
        verify(req).setAttribute("countPages", countPages);
        verify(req).setAttribute("activeInterval", "week");
        verify(req).setAttribute("page", 1L);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void selectWrongInterval() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet= new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);


        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("1");
        when(req.getParameter("setInterval")).thenReturn("wrongInterval");
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        notificationServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getCountWithConnectionDBProblem() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet= new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("1");
        when(req.getParameter("setInterval")).thenReturn("week");
        when(notificationService.getCountNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week")))
                .thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);


        notificationServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getNotificationsWithConnectionDBProblem() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet= new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("1");
        when(req.getParameter("setInterval")).thenReturn("week");
        when(notificationService.getCountNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week")))
                .thenReturn(Optional.of(countNotifications));
        when(notificationService.getNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week"),0,10))
                .thenReturn(Optional.empty());
        when(req.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);


        notificationServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void outOfRangeCountPages() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet= new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("20");
        when(req.getParameter("setInterval")).thenReturn("week");
        when(notificationService.getCountNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week")))
                .thenReturn(Optional.of(countNotifications));
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        notificationServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void getNotificationsPageWithNegativeNumberOrZero() throws ServletException, IOException {
        final NotificationService notificationService = mock(NotificationService.class);
        final NotificationServlet notificationServlet = new NotificationServlet(notificationService);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getParameter("page")).thenReturn("0");
        when(req.getParameter("setInterval")).thenReturn("week");
        when(notificationService.getCountNotificationsByDifferentInterval(currentUserID,mapTimeInterval.get("week")))
                .thenReturn(Optional.of(countNotifications));
        when(req.getRequestDispatcher("/WEB-INF/not_found.jsp")).thenReturn(dispatcher);

        notificationServlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(dispatcher).forward(req, resp);
    }
}
