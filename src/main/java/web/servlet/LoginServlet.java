package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Credentials;
import model.User;
import service.UserService;
import web.servlet.utils.FormValidation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Singleton
public class LoginServlet extends HttpServlet {


    private final UserService userService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginServlet.class);

    @Inject
    public LoginServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET");
        req.setAttribute("data",Credentials.builder().build());
        req.getRequestDispatcher("/WEB-INF/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");
        log.debug("Parameters: login - {}", req.getParameter("username"));
        Credentials credentials = Credentials.builder()
                .login(req.getParameter("username"))
                .password(req.getParameter("password"))
                .build();

        FormValidation validate = formValidation(credentials);

        if (validate.isValid()) {
            Optional<User> user = userService.getCurrentUserByCredentials(credentials);
            if (user.isPresent()) {
                log.debug("User: {}", user);
                req.getSession(true).setAttribute("user", user.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(req.getContextPath() + "/profile_" + user.get().getId());
                return;
            } else {
                /*
                 * Как вариант, перенаправлять на страницу ошибки
                 */
                req.setAttribute("loginPass", "invalid");
                req.setAttribute("data",credentials);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
                return;
            }
        }

        req.setAttribute("data",credentials);
        req.setAttribute("errors", validate.getErrors());
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }


    private FormValidation formValidation(Credentials credentials) {
        FormValidation validate = new FormValidation();
        validate.validateCredentials(credentials);
        return validate;
    }
}
