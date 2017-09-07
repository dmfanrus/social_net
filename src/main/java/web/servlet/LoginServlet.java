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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class LoginServlet extends HttpServlet {


    private final UserService userService;
    private FormValidation validate;
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

        formValidation(credentials);

        if (validate.isValid()) {
            Optional<User> user = userService.getByCredentials(credentials);
            if (user.isPresent()) {
                req.getSession().setAttribute("user", user.get());
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            } else {
                /**
                 * Как вариант, перенаправлять на страницу ошибки
                 */
                req.setAttribute("loginPass", "invalid");
                req.setAttribute("data",credentials);
                req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
                return;
            }
        }

//        validate.getErrors().forEach((fieldName,fieldValue)-> System.out.println(fieldName + "   " + fieldValue));
        req.setAttribute("data",credentials);
        req.setAttribute("errors", validate.getErrors());
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }


    private void formValidation(Credentials credentials) {
        validate = new FormValidation();
        validate.validateCredentials(credentials);
    }
}
