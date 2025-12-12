package com.contact;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
public class ListContactServlet extends HttpServlet {
	ContactService service = new ContactService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("list", service.getAll());
        req.getRequestDispatcher("listContacts.jsp").forward(req, resp);
    }

}
