<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
    com.contact.Contact c = (com.contact.Contact) request.getAttribute("contact");
%>

<h2>Edit Contact</h2>

<form action="updateContact" method="post">
    <input type="hidden" name="id" value="<%=c.getId()%>">

    Name: <input type="text" name="name" value="<%=c.getName()%>"><br>
    Phone: <input type="text" name="phone" value="<%=c.getPhone()%>"><br>
    Email: <input type="text" name="email" value="<%=c.getEmail()%>"><br>

    <button type="submit">Update</button>
</form>


</body>
</html>