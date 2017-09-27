package web.servlet.changers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import service.RelationshipService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Singleton
public class ChangeRelationshipServlet extends HttpServlet{

        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangeRelationshipServlet.class);
        private final RelationshipService relationshipService;

        @Inject
        public ChangeRelationshipServlet(RelationshipService relationshipService) {
            this.relationshipService = relationshipService;
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
            long otherUserID = Long.parseLong(req.getParameter("otherUserID"));
            String action = req.getParameter("action");
            if(switchActionByString(action, currentUserID, otherUserID)){
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            String fullName=req.getParameter("fullName");
            String page=req.getParameter("page");

            log.debug("fullName - {} page - {}", fullName, page);
            if(fullName!=null)
                req.setAttribute("fullName",fullName);
            if(page!=null)
                req.setAttribute("page",page);

            resp.sendRedirect(req.getContextPath() + req.getServletPath().replaceFirst("/changer",""));
        }


        private boolean switchActionByString(String action, long currentUserID, long otherUserID){
            switch (action){
                case "sendRequest":{
                    relationshipService.addFriendRequestFromCurrentUser(
                            currentUserID,otherUserID, Timestamp.valueOf(LocalDateTime.now()));
                    return true;
                }
                case "deleteRequestFromMe":{
                    relationshipService.deleteFriendRequests(currentUserID,otherUserID);
                    return true;
                }
                case "deleteRequestFromOther":{
                    relationshipService.deleteFriendRequests(currentUserID,otherUserID);
                    return true;
                }
                case "confirmRequest":{
                    relationshipService.confirmFriendRequestByCurrentUser(
                            currentUserID,otherUserID, Timestamp.valueOf(LocalDateTime.now()));
                    return true;
                }
                case "deleteFriend":{
                    relationshipService.deleteFriendRelationships(
                            currentUserID,otherUserID, Timestamp.valueOf(LocalDateTime.now()));
                    return true;
                }
                case "blockUser":{
                    relationshipService.blockedOtherUserByCurrentUser(
                            currentUserID,otherUserID, Timestamp.valueOf(LocalDateTime.now()));
                    return true;
                }
                case "unblockUser":{
                    relationshipService.unblockedOtherUserByCurrentUser(
                            currentUserID,otherUserID, Timestamp.valueOf(LocalDateTime.now()));
                    return true;
                }
                default:
                    return false;
            }
        }
}
