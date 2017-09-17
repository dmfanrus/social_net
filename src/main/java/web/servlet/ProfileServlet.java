package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import service.UserService;
import web.servlet.utils.FormValidation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Singleton
public class ProfileServlet extends HelpServlet {

    private final UserService userService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProfileServlet.class);
    private static final String SERVLET_PATTERN =
            "/profile_([0-9])+";

    @Inject
    public ProfileServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]", ((User) req.getSession(false).getAttribute("user")).getId());
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        String servletPath = req.getServletPath();
        if (servletPath.matches(SERVLET_PATTERN)) {
            String idS = servletPath.substring(servletPath.lastIndexOf("_") + 1);
            long id = Long.parseLong(servletPath.substring(servletPath.lastIndexOf("_") + 1));
                Optional<User> user = userService.getById(id);
                if (user.isPresent()) {
                    req.setAttribute("userInfo", user.get());
                    if(currentUserID == id)
                        req.setAttribute("owner", true);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    req.getRequestDispatcher("/WEB-INF/profile.jsp")
                            .forward(req, resp);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                            .forward(req, resp);
                }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");
    }
}
