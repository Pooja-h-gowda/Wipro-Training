<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>All Contacts</h2>

<a href="addContact.jsp">Add New Contact</a>

<table border="1">
<tr><th>ID</th><th>Name</th><th>Phone</th><th>Email</th><th>Action</th></tr>

<%
   java.util.List<com.contact.Contact> list =
       (java.util.List<com.contact.Contact>) request.getAttribute("list");

   for(com.contact.Contact c : list) {
%>
<tr>
   <td><%=c.getId()%></td>
   <td><%=c.getName()%></td>
   <td><%=c.getPhone()%></td>
   <td><%=c.getEmail()%></td>

   <td>
     <a href="editContact?id=<%=c.getId()%>">Edit</a>
     <a href="deleteContact?id=<%=c.getId()%>">Delete</a>
   </td>
</tr>
<% } %>
</table>


</body>
</html>