package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Conversation;
import model.Message;
import model.User;
import service.ConversationService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Singleton
public class MessageServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessageServlet.class);
    private final ConversationService conversationService;
    private static final String SERVLET_PATTERN =
            "/message_([0-9])+";

    @Inject
    public MessageServlet(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("GET - user[{}]", currentUserID);
        String servletPath = req.getServletPath();
        if (servletPath.equals("/message")) {
            Optional<List<Conversation>> conversations = conversationService.getListConversation(currentUserID);
            if (conversations.isPresent()) {
                log.debug("List of conversations: {}", conversations.get());
                req.setAttribute("convList", conversations.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                req.getRequestDispatcher("/WEB-INF/message.jsp")
                        .forward(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                        .forward(req, resp);
            }
        } else if (servletPath.matches(SERVLET_PATTERN)) {
            long conv_id = Long.parseLong(servletPath.replaceFirst("/message_", ""));
            Optional<List<Message>> messages = conversationService.getListMessages(conv_id, currentUserID);
            Optional<List<Conversation>> conversations = conversationService.getListConversation(currentUserID);
            if (conversations.isPresent() && messages.isPresent()) {
                req.setAttribute("convList", conversations.get());
                req.setAttribute("msgList", messages.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                req.getRequestDispatcher("/WEB-INF/message.jsp")
                        .forward(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                        .forward(req, resp);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("POST - user[{}]", currentUserID);

        String message = req.getParameter("message");
        String servletPath = req.getServletPath();
        if (servletPath.matches(SERVLET_PATTERN)) {
            long conv_id = Long.parseLong(servletPath.replaceFirst("/message_", ""));
            conversationService.addMessage(Message.builder()
                    .sender_id(currentUserID)
                    .message(message)
                    .ts_action(Timestamp.valueOf(LocalDateTime.now()))
                    .conv_id(conv_id)
                    .build());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.sendRedirect(req.getContextPath() + servletPath);
        }
    }
}
