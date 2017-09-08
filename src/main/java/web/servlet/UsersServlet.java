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
        long countPages = 0;
        long currentPage;
        Optional<Long> countUsers = userService.getCount();
        if (req.getParameter("currentPage") == null) {
            req.setAttribute("currentPage", 1);
            currentPage = 1;
        } else {
            currentPage = Long.parseLong(req.getParameter("currentPage"));
            req.setAttribute("currentPage", currentPage);
        }

        if (countUsers.isPresent()) {
            countPages = (long) Math.ceil((float)countUsers.get()/10.0);
            log.debug("Count of users in DB - {}, div - {}, countpage - {}", countUsers.get(), (float)(countUsers.get()/10), countPages);
            req.setAttribute("countPages", countPages);
        } else {
            log.warn("getCounts() returned null");
            /*
             * Возможно перенаправлять на сообщение ошибки
             */
        }
        List<User> users = userService.getUsers((currentPage - 1) * 10, 10);

//        List<User> userList = new ArrayList<>();
//        for (Optional<User> user : users) {
//            if(user.isPresent())
//                userList.add(user.get());(
//        }
        log.debug("GET - countPages={}  currentPage={}", countPages, currentPage);
        req.setAttribute("usersList", users);
        req.getRequestDispatcher("/WEB-INF/users.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
