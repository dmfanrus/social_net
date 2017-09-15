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
import java.util.ArrayList;
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
        log.debug("GET - user[{}]", ((User) req.getSession(false).getAttribute("user")).getId());
        long countPages;
        long currentPage;
        Optional<List<User>> users;
        String fullName = req.getParameter("fullName");
        if (req.getParameter("countPages") == null || req.getParameter("page") == null) {
            currentPage = 1;
            Optional<Long> countUsers = userService.getCount();
            if (countUsers.isPresent()) {
                countPages = (long) Math.ceil((float) countUsers.get() / USERSONPAGE);
            } else {
                log.warn("getCounts() returned null");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
                return;
            }
        } else {
            countPages = Long.parseLong(req.getParameter("countPages"));
            currentPage = Long.parseLong(req.getParameter("page"));
            if (countPages < currentPage || currentPage < 1) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                return;
            }
        }

        req.setAttribute("countPages", countPages);
        req.setAttribute("page", currentPage);

        if (fullName == null) {
            users = userService.getUsers((currentPage - 1) * USERSONPAGE, USERSONPAGE);
        } else {
            req.setAttribute("fullName",fullName);
            users = userService.getUsers(fullName, (currentPage - 1) * USERSONPAGE, USERSONPAGE);
        }

        if (users.isPresent()) {
            long startPage = currentPage - 2 > 0 ? currentPage - 2 : 1;
            long endPage = startPage + 4 <= countPages ? startPage + 4 : countPages;
            req.setAttribute("startPage", startPage);
            req.setAttribute("endPage", endPage);
            req.setAttribute("usersList", users.get());
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("POST - user[{}]", ((User) req.getSession(false).getAttribute("user")).getId());
        long countPages;
        long currentPage;
        Optional<List<User>> users;
        String fullName = req.getParameter("fullName");
        if (fullName == null || fullName.isEmpty()) {
            countPages = Long.parseLong(req.getParameter("countPages"));
            currentPage = Long.parseLong(req.getParameter("page"));
            if (countPages < currentPage || currentPage < 1) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                return;
            }
            users = userService.getUsers((currentPage - 1) * USERSONPAGE, USERSONPAGE);
        } else {
            Optional<Long> countUsers = userService.getCount(fullName);
            if (countUsers.isPresent()) {
                currentPage = 1;
                countPages = (long) Math.ceil((float) countUsers.get() / USERSONPAGE);
                users = userService.getUsers(fullName,(currentPage - 1) * USERSONPAGE, USERSONPAGE);
                req.setAttribute("fullName",fullName);
            } else {
                log.warn("getCount() returned null");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp")
                        .forward(req, resp);
                return;
            }
        }

        req.setAttribute("countPages", countPages);
        req.setAttribute("page", currentPage);
        if (users.isPresent()) {
            long startPage = currentPage - 2 > 0 ? currentPage - 2 : 1;
            long endPage = startPage + 4 <= countPages ? startPage + 4 : countPages;
            req.setAttribute("startPage", startPage);
            req.setAttribute("endPage", endPage);
            req.setAttribute("usersList", users.get());
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
    }
}
