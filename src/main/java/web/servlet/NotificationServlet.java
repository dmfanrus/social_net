package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.ListNotifications;
import model.User;
import service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class NotificationServlet extends HttpServlet{

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationServlet.class);
    private static final int NOTIFICATIONSONPAGE = 10;
    private final NotificationService notificationService;
    private static final Map<String,String> mapTimeInterval = new HashMap<>();
    static{
        mapTimeInterval.put("day","1 day");
        mapTimeInterval.put("week","1 week");
        mapTimeInterval.put("month","1 month");
        mapTimeInterval.put("year","1 year");
    }

    @Inject
    public NotificationServlet(NotificationService notificationService) {
        this.notificationService = notificationService;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        final long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("GET - user[{}]", currentUserID);

        final String currPageS = req.getParameter("page");
        final String timeInterval = req.getParameter("setInterval");
        long currentPage = currPageS == null ? 1 : Long.parseLong(currPageS);

        if(!mapTimeInterval.containsKey(timeInterval)){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
            return;
        }

        Optional<Long> countNotifications = notificationService.getCountNotificationsByDifferentInterval(currentUserID, mapTimeInterval.get(timeInterval));

        if (countNotifications.isPresent()) {
            final long countPages = (long) Math.ceil((float) countNotifications.get() / NOTIFICATIONSONPAGE);
            if (countPages < currentPage || currentPage < 1) {
                if (countPages!=0 && currentPage!=1) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                    return;
                }
            }
            final Optional<ListNotifications> notifications = notificationService.getNotificationsByDifferentInterval(
                    currentUserID, mapTimeInterval.get(timeInterval), (currentPage-1) * NOTIFICATIONSONPAGE, NOTIFICATIONSONPAGE);

            if (notifications.isPresent()) {
                final long startPage = currentPage - 2 > 0 ? currentPage - 2 : 1;
                final long endPage = startPage + 4 <= countPages ? startPage + 4 : countPages;
                req.setAttribute("startPage", startPage);
                req.setAttribute("endPage", endPage);
                req.setAttribute("notificationsList", notifications.get());
                req.setAttribute("countPages", countPages);
                req.setAttribute("activeInterval", timeInterval);
                req.setAttribute("page", currentPage);
                resp.setStatus(HttpServletResponse.SC_OK);
                req.getRequestDispatcher("/WEB-INF/notifications.jsp")
                        .forward(req, resp);
            } else {
                log.warn("getNotifications() returned null");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp")
                        .forward(req, resp);
            }
        } else {
            log.warn("getCounts() returned null");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
        }
    }
}
