<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div class="page-header" style="background: #d3d3d3;">
		<center>
			<h1>
				ADD A NEW BOARD TO
				<%=request.getParameter("category").toUpperCase() %>
				CATEGORY
			</h1>
		</center>
	</div>

	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-9">
			<ul class="nav navbar-nav">
				<li><a href="home.jsp">Home</a>
				</li>
				<li><a href="login.jsp">Login</a>
				</li>
				<li><a href="register.jsp">Register</a>
				</li>
				<%
						if (request.getSession().getAttribute("role") != null &&
							request.getSession().getAttribute("role").equals("Admin"))
						{
							%>
				<li><a href="admincontrolpanel.jsp">Control Panel</a>
				</li>
				<%
						}
					 %>
			</ul>
		</div>
	</div>
	</nav>

	<%
		if (request.getSession().getAttribute("islogged") != null)
		{		
			%>
	<div>
		<p style="float: left">
			Logged in as
			<%=request.getSession().getAttribute("loginnickname") %>.
		</p>

		<div style="padding-bottom: 5px;" align="right">
			<a href="login.jsp"> Log out </a>
		</div>
	</div>
	<br>
	<% 
		}
	
		if (request.getSession().getAttribute("role").equals("Admin"))
		{ 
	%>
	<center>
		<form action="AddNewBoard" method="post">
			Complete the following form: <br> <br> Title: <input
				type="text" name="newboardtitle"> <br>
			<br> <input type="submit" value="Confirm" style="width: 190px">
			<input type="hidden" value="<%=request.getParameter("category")%>"
				name="currentcategoryname">
		</form>
	</center>
	<%
		} 
		else
		{
			%>
	<center>
		<h1>
			<% out.println("ACCESS DENIED"); %>
		</h1>
	</center>
	<%
		}
	%>
</body>
</html>