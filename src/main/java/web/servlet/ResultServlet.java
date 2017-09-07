package web.servlet;

import model.Book;
import model.User;
import service.impl.BookServiceImpl;
import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class ResultServlet  extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ResultServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET - user[{}]",((User)req.getSession().getAttribute("user")).getId());
        req.getRequestDispatcher("/WEB-INF/help.jsp")
                .forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST");
        req.setCharacterEncoding("UTF-8");
        String inputReqText = req.getParameter("reqText");
        Process p = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList("C:/Python35/python.exe", "C:/Mihail/Study/Diplom/Soical_SN/python/test_em.py", "\"" + inputReqText + "\""));
            p = processBuilder.start();
        } catch (Exception e){
            System.out.println(e);
        }

        BookServiceImpl bookService = new BookServiceImpl();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String file_line = "";
        String author_line = "";
        String book_line = "";
        List<Book> listNBooksAuthors = new ArrayList<>();
        for(int i=0;i<10;i++){
            try {
                file_line = in.readLine();
                author_line = in.readLine();
                book_line = in.readLine();
                listNBooksAuthors.add(bookService.findBookByListFileName(i+1,file_line,author_line,book_line));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        req.setAttribute("listBooks", listNBooksAuthors);
        req.getRequestDispatcher("/WEB-INF/results.jsp")
                .forward(req,resp);
    }
}
