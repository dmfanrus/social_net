package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Credentials;
import model.Gender;
import model.User;
import service.SecurityService;
import service.UserService;
import web.servlet.utils.FormValidation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class RegistrationServlet extends HttpServlet {

    private final UserService userService;
    private final SecurityService securityService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationServlet.class);

    @Inject
    public RegistrationServlet(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET");
        req.setAttribute("data", User.builder().build());
        req.getRequestDispatcher("/WEB-INF/registration.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("POST");
        log.debug("Parameters: FullName-[{}] Email-[{}] Birthday-[{}] Gender-[{}] Login-[{}]",
                req.getParameter("fullName"),
                req.getParameter("email"),
                req.getParameter("dateOfBirth"),
                req.getParameter("gender"),
                req.getParameter("username"));
        log.debug("Availably of user in session: {}",req.getSession(false).getAttribute("user"));

        HashMap<String, String> fields = new HashMap<>();
        fields.put("fullName", req.getParameter("fullName"));
        fields.put("email", req.getParameter("email"));
        fields.put("login", req.getParameter("username"));
        fields.put("password", req.getParameter("password"));
        fields.put("dateOfBirth", req.getParameter("dateOfBirth"));
        fields.put("gender", req.getParameter("gender"));

        FormValidation validate =  formValidation(fields);

        if (validate.isValid()) {
            log.debug("Form fields is valid");
            User userIn = User.builder().fullName(fields.get("fullName"))
                    .email(fields.get("email"))
                    .login(fields.get("login"))
                    .hashPassword(securityService.encrypt(fields.get("password")))
                    .dateOfBirth(LocalDate.parse(fields.get("dateOfBirth")))
                    .gender(fields.get("gender").equals("M") ? Gender.MALE : Gender.FEMALE)
                    .build();

            Optional<User> user = userService.createUser(userIn);
            if (user.isPresent()) {
                log.debug("Create user[{}] is success", user.get().getId());
                req.getSession(true).setAttribute("user", user.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            } else {
                /*
                 * Как вариант, перенаправлять на страницу ошибки
                 */
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
                return;
            }
        }

        log.debug("Form is not valid({})",validate.getErrors());
        req.setAttribute("fields", fields);
        req.setAttribute("errors", validate.getErrors());
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        req.getRequestDispatcher("/WEB-INF/registration.jsp").forward(req, resp);
    }

    private FormValidation formValidation(HashMap<String, String> fields) {
        FormValidation validate = new FormValidation();
        validate.validateFullName(fields.get("fullName"));
        validate.validateEmail(fields.get("email"));
        validate.validateCredentials(Credentials.builder().login(fields.get("login")).password(fields.get("password")).build());
        validate.validateDate(fields.get("dateOfBirth"));
        validate.validateString("gender", fields.get("gender"));

        if (userService.checkExistByLogin(fields.get("login")))
            validate.setError("login", "loginNotUsed");
        return validate;
    }

}
