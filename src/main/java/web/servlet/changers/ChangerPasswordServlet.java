package web.servlet.changers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Credentials;
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
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class ChangerPasswordServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangerPasswordServlet.class);

    private final SecurityService securityService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Inject
    public ChangerPasswordServlet(SecurityService securityService, UserService userService, NotificationService notificationService) {
        this.securityService = securityService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]", ((User) req.getSession(false).getAttribute("user")).getId());
        req.getRequestDispatcher("/WEB-INF/update_password.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("POST");
        User currentUser = (User) req.getSession(false).getAttribute("user");
        log.debug("Availably of user in session: {}", currentUser);

        HashMap<String, String> fields = new HashMap<>();
        fields.put("login", currentUser.getLogin());
        fields.put("newPassword", req.getParameter("newPassword"));
        fields.put("currentPassword", req.getParameter("currentPassword"));


        FormValidation validate = formValidation(fields);

        if (validate.isValid()) {
            Credentials newCredentials = Credentials.builder()
                    .login(fields.get("login"))
                    .password(securityService.encrypt(fields.get("newPassword")))
                    .build();
            log.debug("Form is Valid");
            Optional<User> userOut = userService.updatePassword(newCredentials);
            log.debug("Getting new user");
            if (userOut.isPresent()) {
                log.debug("User getted is successefully");
                notificationService.addNotification(Notification.builder()
                        .sender_id(1)
                        .recipient_id(userOut.get().getId())
                        .not_status(1)
                        .ts_action(userOut.get().getTimeCreate())
                        .build());
                log.debug("Changed user[{}]'s password is success", userOut.get().getId());
                req.getSession(false).setAttribute("user", userOut.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                req.setAttribute("updatePassword", true);
                req.getRequestDispatcher("/WEB-INF/update_password.jsp").forward(req, resp);
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
                return;
            }
        }

        log.debug("Form is not valid({})", validate.getErrors());
        req.setAttribute("errors", validate.getErrors());
        resp.setStatus(HttpServletResponse.SC_OK);
        req.getRequestDispatcher("/WEB-INF/update_password.jsp").
                forward(req, resp);

    }

    private FormValidation formValidation(HashMap<String, String> fields) {
        FormValidation validate = new FormValidation();
        Credentials credentials = Credentials.builder()
                .login(fields.get("login"))
                .password(fields.get("currentPassword"))
                .build();
        validate.validateString("newPassword", fields.get("newPassword"));
        validate.validateString("currentPassword", fields.get("currentPassword"));

        if (!userService.validateUserByCurrentCredantials(credentials)) {
            validate.setError("currentPassword", "wrongPassword");
        }

        return validate;
    }

}
