package com.contact;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class UpdateContactServlet extends HttpServlet {
	ContactService service = new ContactService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        service.update(id,
                req.getParameter("name"),
                req.getParameter("phone"),
                req.getParameter("email")
        );

        resp.sendRedirect("listContacts");
    }

}
