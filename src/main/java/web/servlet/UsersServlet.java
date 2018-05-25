package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Singleton
public class UsersServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UsersServlet.class);
    private static final int USERSONPAGE = 10;
    private final UserService userService;

    @Inject
    public UsersServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("GET - user[{}]", currentUserID);
        Optional<List<User>> users;
        String fullName = req.getParameter("fullName");
        String currPageS = req.getParameter("page");
        long currentPage = currPageS == null ? 1 : Long.parseLong(currPageS);
        Optional<Long> countUsers = userService.getCount(fullName);

        if (countUsers.isPresent()) {
            long countPages = (long) Math.ceil((float) countUsers.get() / USERSONPAGE);
            if (countPages < currentPage || currentPage < 1) {
                if (countPages!=0 && currentPage!=1) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                    return;
                }
            }
            users = userService.getUsers(currentUserID, fullName, (currentPage - 1) * USERSONPAGE, USERSONPAGE);
            if (users.isPresent()) {
                long startPage = currentPage - 2 > 0 ? currentPage - 2 : 1;
                long endPage = startPage + 4 <= countPages ? startPage + 4 : countPages;
                req.setAttribute("startPage", startPage);
                req.setAttribute("endPage", endPage);
                req.setAttribute("usersList", users.get());
                req.setAttribute("countPages", countPages);
                req.setAttribute("fullName", fullName);
                req.setAttribute("page", currentPage);
                resp.setStatus(HttpServletResponse.SC_OK);
                log.debug("GET - countPages={}  currentPage={}", countPages, currentPage);
                req.getRequestDispatcher("/WEB-INF/users.jsp")
                        .forward(req, resp);
            } else {
                log.warn("getUsers() returned null");
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("POST - user[{}]", currentUserID);
        long countPages;
        long currentPage = 1;
        Optional<List<User>> users;
        String fullName = req.getParameter("fullName");
        Optional<Long> countUsers = userService.getCount(fullName);

        if (countUsers.isPresent()) {
            countPages = (long) Math.ceil((float) countUsers.get() / USERSONPAGE);
            users = userService.getUsers(currentUserID, fullName, (currentPage - 1) * USERSONPAGE, USERSONPAGE);
            if (users.isPresent()) {
                long startPage = currentPage - 2 > 0 ? currentPage - 2 : 1;
                long endPage = startPage + 4 <= countPages ? startPage + 4 : countPages;
                req.setAttribute("startPage", startPage);
                req.setAttribute("endPage", endPage);
                req.setAttribute("usersList", users.get());
                req.setAttribute("fullName", fullName);
                req.setAttribute("countPages", countPages);
                req.setAttribute("page", currentPage);
                resp.setStatus(HttpServletResponse.SC_OK);
                log.debug("POST - countPages={}  currentPage={}", countPages, currentPage);
                req.getRequestDispatcher("/WEB-INF/users.jsp")
                        .forward(req, resp);
            } else {
                log.warn("getUsers() returned null");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp")
                        .forward(req, resp);
            }
        } else {
            log.warn("getCount() returned null");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.getRequestDispatcher("/WEB-INF/error.jsp")
                    .forward(req, resp);
        }

    }
}
