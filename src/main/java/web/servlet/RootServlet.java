package web.servlet;

import com.google.inject.Singleton;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class RootServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RootServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET");
        req.getRequestDispatcher("/WEB-INF/root.jsp")
                .forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");

    }
}
