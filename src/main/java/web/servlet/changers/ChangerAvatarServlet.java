package web.servlet.changers;

import com.google.inject.Singleton;
import model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

//@MultipartConfig
@Singleton
public class ChangerAvatarServlet extends HttpServlet {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangerAvatarServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long currentUserID = ((User) req.getSession(false).getAttribute("user")).getId();
        if (ServletFileUpload.isMultipartContent(req)) {
            FileItemFactory itemFactory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(itemFactory);
            try {
                List<FileItem> multiparts = upload.parseRequest(req);

                for (FileItem item : multiparts) {
                    String contentType = item.getContentType();
                    if (!contentType.equals("image/jpeg")) {
                        continue;
                    }
                    File uploadDir = new File("\\resources\\img");
                    File file = new File(uploadDir + "\\avatar_" + currentUserID + ".jpeg");
                    item.write(file);
                    log.debug("File saved successfully");
                }
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (FileUploadException ex) {
                log.error("Upload fail", ex);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("Can't save file", ex);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect(req.getContextPath() + "/profile/changer");
    }
}
