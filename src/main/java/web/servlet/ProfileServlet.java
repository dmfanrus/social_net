package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import service.UserService;
import web.servlet.utils.FormValidation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Singleton
public class ProfileServlet extends HelpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProfileServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]", ((User) req.getSession().getAttribute("user")).getId());
        req.getRequestDispatcher("/WEB-INF/profile.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");

    }
}
