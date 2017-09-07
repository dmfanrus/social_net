package web.servlet;

import com.google.inject.Singleton;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class FriendsServlet extends HttpServlet{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FriendsServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]",((User)req.getSession().getAttribute("user")).getId());
        req.getRequestDispatcher("/WEB-INF/friends.jsp")
                .forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");

        super.doPost(req, resp);
    }
}
