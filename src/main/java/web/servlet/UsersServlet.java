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
    private final UserService userService;

    @Inject
    public UsersServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]", ((User) req.getSession(false).getAttribute("user")).getId());
        Optional<Long> countUsers = userService.getCount();
        if (countUsers.isPresent()) {
            long countPages = (long) Math.ceil((float) countUsers.get() / 10.0);
            req.setAttribute("countPages", countPages);

            long currentPage = 1;
            if (req.getParameter("page") == null) {
                req.setAttribute("page", 1L);
            } else {
                currentPage = Long.parseLong(req.getParameter("page"));
                if (countPages < currentPage || currentPage<1 ) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                            .forward(req, resp);
                    return;
                }
                req.setAttribute("page", currentPage);
            }
            Optional<List<User>> users = userService.getUsers((currentPage - 1) * 10, 10);

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
        } else {
            log.warn("getCounts() returned null");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.getRequestDispatcher("/WEB-INF/error.jsp")
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
