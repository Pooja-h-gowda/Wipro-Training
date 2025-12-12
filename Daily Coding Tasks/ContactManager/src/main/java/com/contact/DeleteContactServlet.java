package com.contact;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
public class DeleteContactServlet extends HttpServlet {
	ContactService service = new ContactService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        service.delete(id);
        resp.sendRedirect("listContacts");
    }

}
