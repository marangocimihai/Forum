<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="userrelated.User"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		if (User.wrongRegisterDetails == 1)
		{
			%>
	<center>The username already exists. Please choose another
		one.</center>
	<% 
		}
		else if (User.wrongRegisterDetails == 2)
		{
			%>
	<center>The two passwords do not match. Please check them.</center>
	<%
		}
		else if (User.wrongRegisterDetails == 3)
		{
			%>
	<center>Specified e-mail does not respect the given format.</center>
	<%
		}
		else if (User.wrongRegisterDetails == 4)
		{
			%>
	<center>The username is required.</center>
	<%
		}
		else if (User.wrongRegisterDetails == 5)
		{
			%>
	<center>The password is required.</center>
	<%
		}
	 %>

	<center>In 5 seconds you will be redirected back to the
		register page.</center>

	<% response.setHeader("Refresh", "5;url=register.jsp"); %>

</body>
</html>