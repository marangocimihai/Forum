<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="databaserelated.Database"%>
<%@ page import="categoryrelated.Category"%>
<%@ page import="categoryrelated.Board"%>
<%@ page import="userrelated.User"%>
<%@ page import="userrelated.Moderator"%>
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

	<div class="page-header" style="background: #d3d3d3;">
		<center>
			<h1>ADD A NEW MODERATOR</h1>
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
		<p>
			Select the nickname of the user to be the new moderator of '<%=request.getParameter("board") %>'
			board
		</p>

		<form action="AddNewModerator" method="post">
			<select style="width: 172px" name="newmoderator">
				<%
						Database D3 = new Database();
						
						List<User> normalUsers = new ArrayList<User>();
						List<Moderator> currentBoardModerators = new ArrayList<Moderator>();
																						
						String currentBoardName = request.getParameter("board");
						
						normalUsers = D3.getNormalUsersAndModerators(true);
						currentBoardModerators = D3.getModeratorsByBoardID(D3.getBoardIDByName(currentBoardName));
											
						Iterator<User> it2 = normalUsers.iterator();
											
						while (it2.hasNext())
						{
							boolean isModeratorAlready = false;
							
							User currentUser = new User();
							currentUser = it2.next();
							
							Iterator<Moderator> it3 = currentBoardModerators.iterator();
													
							while (it3.hasNext())
							{
								Moderator currentModerator = new Moderator();
								currentModerator = it3.next();
								
								if (currentUser.nickname.equals(currentModerator.nickname))
								{								
									isModeratorAlready = true;
								}
							}
							
							if (!isModeratorAlready)
							{
								%>
				<option value="<%out.println(currentUser.nickname);%>">
					<% out.println(currentUser.nickname); %>
				</option>
				<%
							}
						}
					%>
			</select> <br> <input type="hidden"
				value="<%=request.getParameter("board")%>" name="currentboardname">
			<input type="submit" value="Add new moderator"
				style="margin-top: 12px">
		</form>


	</center>
	<%
		} 
		else
		{
			%>
	<center>
		<h1>ACCESS DENIED</h1>
	</center>
	<%
		}
	%>
</body>
</html>