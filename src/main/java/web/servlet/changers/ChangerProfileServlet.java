package web.servlet.changers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Credentials;
import model.Gender;
import model.Notification;
import model.User;
import service.NotificationService;
import service.SecurityService;
import service.UserService;
import web.servlet.utils.FormValidation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class ChangerProfileServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangerProfileServlet.class);

    private final SecurityService securityService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Inject
    public ChangerProfileServlet(SecurityService securityService, UserService userService, NotificationService notificationService) {
        this.securityService = securityService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]", ((User) req.getSession(false).getAttribute("user")).getId());
        req.getRequestDispatcher("/WEB-INF/update_profile.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("POST");
        log.debug("Parameters: FirstName-[{}] LastName-[{}] Login-[{}] Email-[{}] Birthday-[{}] Gender-[{}]",
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("username"),
                req.getParameter("email"),
                req.getParameter("dateOfBirth"),
                req.getParameter("gender"));
        User currentUser = (User) req.getSession(false).getAttribute("user");
        log.debug("Availably of user in session: {}", currentUser);


        HashMap<String, String> fields = new HashMap<>();
        fields.put("firstName", req.getParameter("firstName"));
        fields.put("lastName", req.getParameter("lastName"));
        fields.put("login", req.getParameter("username"));
        fields.put("currentPassword", req.getParameter("currentPassword"));
        fields.put("email", req.getParameter("email"));
        fields.put("dateOfBirth", req.getParameter("dateOfBirth"));
        fields.put("gender", req.getParameter("gender"));

        FormValidation validate = formValidation(fields);

        if (validate.isValid()) {
            User userIn = User.builder()
                    .firstName(fields.get("firstName"))
                    .lastName(fields.get("lastName"))
                    .login(fields.get("login"))
                    .email(fields.get("email"))
                    .dateOfBirth(LocalDate.parse(fields.get("dateOfBirth")))
                    .gender(fields.get("gender").equals("M") ? Gender.MALE : Gender.FEMALE)
                    .build();
            Optional<User> userOut = userService.updateProfile(userIn);
            log.debug(userOut.toString());
            if (userOut.isPresent()) {
                notificationService.addNotification(Notification.builder()
                        .sender_id(userOut.get().getId())
                        .recipient_id(userOut.get().getId())
                        .not_status(1)
                        .ts_action(Timestamp.valueOf(LocalDateTime.now()))
                        .build());
                log.debug("Changed user[{}] is success", userOut.get().getId());
                req.getSession(false).setAttribute("user", userOut.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                req.getRequestDispatcher("/WEB-INF/update_profile.jsp").forward(req, resp);
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
                return;
            }
        }

        log.debug("Form is not valid({})", validate.getErrors());
        req.setAttribute("errors", validate.getErrors());
        log.debug(fields.toString());
        log.debug(validate.getErrors().toString());
        resp.setStatus(HttpServletResponse.SC_OK);
        req.getRequestDispatcher("/WEB-INF/update_profile.jsp").forward(req, resp);
    }

    private FormValidation formValidation(HashMap<String, String> fields) {
        FormValidation validate = new FormValidation();
        Credentials credentials = Credentials.builder()
                .login(fields.get("login"))
                .password(fields.get("currentPassword"))
                .build();
        validate.validateCredentials(credentials);
        validate.validateFirstName(fields.get(("firstName")));
        validate.validateLastName(fields.get(("lastName")));
        validate.validateEmail(fields.get("email"));
        validate.validateDate(fields.get("dateOfBirth"));
        validate.validateString("gender", fields.get("gender"));

        if (!userService.validateUserByCurrentCredantials(credentials)) {
            validate.setError("password", "wrongPassword");
        }


        return validate;
    }
}
