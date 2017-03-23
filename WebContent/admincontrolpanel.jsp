<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="databaserelated.Database"%>
<%@ page import="categoryrelated.Category"%>
<%@ page import="categoryrelated.Board"%>
<%@ page import="userrelated.User"%>
<%@page import="java.util.Iterator"%>
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
	<%
		if (request.getSession().getAttribute("role").equals("Admin"))
		{ 
	%>

	<center>
		<h1 style="background: #d3d3d3">
			<u>CONTROL PANEL</u>
		</h1>
	</center>

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

	<div style="float: left">
		<div style="border: 1px solid black; float: left; margin-left: 20px"
			class="controlpanelclass">
			<form action="AddNewCategory" method="post">
				<center>
					<p>Add a new Category</p>
					<input type="text" name="newcategoryname" size="22"> <br>
					<input type="submit" value="Add new category"
						style="margin-top: 10px; padding-bottom: 5px; width: 167px; height: 25px">
				</center>
			</form>
		</div>
	</div>

	<div style="float: left">
		<div
			style="border: 1px solid black; float: left; margin-left: 20px; height: 94px"
			class="controlpanelclass">
			<form action="AddNewAdmin" method="post">
				<center>
					<p>Add a new admin</p>

					<select style="width: 175px; height: 22px" name="newadmin"
						size="22">
						<%
								Database D3 = new Database();
								List<User> normalUsersAndModerators = new ArrayList<User>();
											
								normalUsersAndModerators = D3.getNormalUsersAndModerators(true);
											
								Iterator<User> it2 = normalUsersAndModerators.iterator();
											
								while (it2.hasNext())
								{
									User currentUser = new User();
									currentUser = it2.next();
												
									%>
						<option value="<%out.println(currentUser.nickname);%>">
							<% out.println(currentUser.nickname); %>
						</option>
						<% 
								}
							%>
					</select> <br> <input type="submit" value="Add new admin"
						style="margin-top: 10px; width: 175px">

				</center>
			</form>
		</div>
	</div>
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