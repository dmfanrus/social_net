package web.servlet;

import com.google.inject.Singleton;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class LogoutServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]",((User)req.getSession(false).getAttribute("user")).getId());
        if(req.getSession(false).getAttribute("user")!=null) {
            log.debug("Invalidate session with user- {}", req.getSession(false).getAttribute("user").toString());
            req.getSession(false).invalidate();
        }
        final String rootPage = req.getContextPath()+"/";
        resp.sendRedirect(rootPage);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");
        super.doPost(req, resp);
    }
}
