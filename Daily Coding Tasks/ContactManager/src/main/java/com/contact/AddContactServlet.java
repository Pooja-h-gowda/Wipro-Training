package com.contact;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AddContactServlet extends HttpServlet {

		    ContactService service = new ContactService();

		    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		            throws ServletException, IOException {

		        String name = req.getParameter("name");
		        String phone = req.getParameter("phone");
		        String email = req.getParameter("email");

		        boolean added = service.add(name, phone, email);

		        if(added) {
		            resp.sendRedirect("listContacts");
		        } else {
		            req.setAttribute("msg", "Fill all fields!");
		            req.getRequestDispatcher("addContact.jsp").forward(req, resp);
		        }
		    }

}
