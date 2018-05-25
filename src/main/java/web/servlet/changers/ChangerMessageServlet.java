package web.servlet.changers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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

@Singleton
public class ChangerMessageServlet extends HttpServlet {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangerMessageServlet.class);
    private final ConversationService conversationService;

    @Inject
    public ChangerMessageServlet(ConversationService conversationService) {
        this.conversationService = conversationService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        log.debug("POST - user[{}]", currentUserID);
        String action = req.getParameter("action");
        String conv_idS = req.getParameter("conv_id");
        String msg_idS = req.getParameter("msg_id");
        String otherUserIDS = req.getParameter("otherUserID");
        String new_msg = req.getParameter("new_msg");
        switch (action) {
            case "createConversation": {
                if (otherUserIDS != null && !otherUserIDS.isEmpty()) {
                    long otherUserID = Long.parseLong(otherUserIDS);
                    Message new_message = Message.builder()
                            .sender_id(currentUserID)
                            .ts_action(Timestamp.valueOf(LocalDateTime.now()))
                            .message(new_msg)
                            .build();
                    conversationService.createConversation(currentUserID,otherUserID,new_message);
                    return;
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                    return;
                }
            }
            case "deleteConversation": {
                if (conv_idS != null && !conv_idS.isEmpty()) {
                    long conv_id = Long.parseLong(conv_idS);

                    conversationService.deleteConversation(conv_id);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.sendRedirect(req.getContextPath() + "/messages");
                    return;
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                    return;
                }
            }
            case "deleteMessage": {
                if (conv_idS != null && !conv_idS.isEmpty() && msg_idS != null && !msg_idS.isEmpty()) {
                    long conv_id = Long.parseLong(conv_idS);
                    long msg_id = Long.parseLong(msg_idS);
                    conversationService.deleteMessage(msg_id);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.sendRedirect(req.getContextPath() + "/messages_" + conv_idS);
                    return;
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);
                    return;
                }
            }
            default: {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/WEB-INF/not_found.jsp").forward(req, resp);

            }
        }
    }

}
