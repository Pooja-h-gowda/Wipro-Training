<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>Add Contact</h2>

<form action="addContact" method="post">
    Name: <input type="text" name="name"><br>
    Phone: <input type="text" name="phone"><br>
    Email: <input type="text" name="email"><br>
    <button type="submit">Save</button>
</form>

<%
   if(request.getAttribute("msg") != null) {
%>
<p style="color:red;"><%=request.getAttribute("msg")%></p>
<% } %>


</body>
</html>