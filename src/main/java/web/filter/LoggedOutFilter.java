package web.filter;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Михаил on 17.08.2017.
 */
@Singleton
public class LoggedOutFilter implements Filter{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoggedOutFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            final HttpServletRequest request = (HttpServletRequest) servletRequest;
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            final HttpSession session = request.getSession(false);
            final String profilePage = request.getContextPath()+"/profile";
            final boolean isLoggedIn = session != null && session.getAttribute("user") != null;
            if (isLoggedIn) {
                log.debug("There is user in session(Request from {}) - SendRedirect() in /profile", request.getRemoteAddr());
                response.sendRedirect(profilePage);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
