package web.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Conversation;
import model.Message;
import model.User;
import service.ConversationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Singleton
public class MessageNewServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessageNewServlet.class);
    private final ConversationService conversationService;

    @Inject
    public MessageNewServlet(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("GET - user[{}]", currentUserID);

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("POST - user[{}]", currentUserID);

        String action = req.getParameter("action");
        String message = req.getParameter("message");
        String otherUserIDS = req.getParameter("otherUserID");
        if (otherUserIDS != null && !otherUserIDS.isEmpty()) {
            long otherUserID = Long.parseLong(otherUserIDS);

            if (action.equals("get")) {
                Optional<Long> existConv = conversationService.getConversationID(currentUserID, otherUserID);

                if (existConv.isPresent()) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.sendRedirect(req.getContextPath() + "/messages_" + existConv.get());
                    return;
                } else {
                    req.setAttribute("convList", new ArrayList<Conversation>());
                    req.setAttribute("msgList", new ArrayList<Message>());
                    req.setAttribute("otherUserID", otherUserID);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    req.getRequestDispatcher("/WEB-INF/messages.jsp")
                            .forward(req, resp);
                    return;
                }
            } else if (action.equals("write")) {
                Optional<Long> conv_id = conversationService.createConversation(currentUserID, otherUserID,
                        Message.builder()
                                .sender_id(currentUserID)
                                .message(message)
                                .ts_action(Timestamp.valueOf(LocalDateTime.now()))
                                .build());

                if (conv_id.isPresent()) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.sendRedirect(req.getContextPath() + "/messages_" + conv_id.get());
                    return;
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    req.getRequestDispatcher("/WEB-INF/error.jsp")
                            .forward(req, resp);
                    return;
                }
            }
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        req.getRequestDispatcher("/WEB-INF/not_found.jsp")
                .forward(req, resp);
    }
}
