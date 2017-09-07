package web.filter;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Михаил on 05.01.2017.
 */
@Singleton
public class LoggedInFilter implements Filter {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoggedInFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if(req instanceof HttpServletRequest) {
            final HttpServletRequest request = (HttpServletRequest) req;
            final HttpServletResponse response = (HttpServletResponse) resp;
            final HttpSession session = request.getSession(false);
            final String loginPage = request.getContextPath()+"/login";
            final boolean isLoggedIn = session != null && session.getAttribute("user") != null;
            if (!isLoggedIn) {
                log.debug("There is not user in session(Request from {}) - SendRedirect() in /login", request.getRemoteAddr());
                response.sendRedirect(loginPage);
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
