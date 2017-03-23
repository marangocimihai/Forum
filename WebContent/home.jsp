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
<link type="text/css" rel="stylesheet" href="cssfiles/smth.css" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div class="page-header" style="background: #d3d3d3;">
		<center>
			<h1>FORUM</h1>
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
			<form action="login.jsp">
				<input type="submit" value="Logout" style="float:right; padding-right:10px" class="btn-danger">
			</form>
		</div>
	</div>
	<% 
		}
		else
		{
			%>
	<div style="padding-bottom: 5px">
		You are not logged in. Press <a href="login.jsp">here</a> to login or
		<a href="register.jsp">here</a> to register.
	</div>
	<% 
		}

		Database D = new Database();
		List<Category> categories = new ArrayList<Category>();
		
		categories = D.getCategories();
		
		Iterator<Category> it1 = categories.iterator();
		
		while (it1.hasNext())
		{
			Category currentCategory = new Category();
			currentCategory = it1.next();
			
			%>
	<center>
		<div class="categoryclass">

			<div style="float: left">
				<% out.println(currentCategory.name.toUpperCase()); %>
			</div>

			<%
						if ((request.getSession().getAttribute("role")) != null && 
						    (request.getSession().getAttribute("role").equals("Admin"))
						    ) 
						   
						{
							request.getSession().setAttribute("categorytobedeleted", currentCategory.ID);
					%>
			<div style="float: right; padding-right: 15px;">
				<a href="addboard.jsp?category=<%=currentCategory.getName() %>">
					Add new board </a>
			</div>

			<%
								if (!D.checkIfCategoryHasBoards(currentCategory.ID))
								{
							%>
			<a href="DeleteCategory?category=<%=currentCategory.name%>"
				class="deletecategory"> Delete </a>
			<%
								}
						}	
					%>
		</div>
	</center>
	<% 
					
			Iterator<Board> it2 = currentCategory.boardList.iterator();
			
			while (it2.hasNext())
			{
				Board currentBoard = new Board();
				currentBoard = it2.next();
				
				%>
	<center>
		<div class="boardclass">
			<div>
				<a
					href="showthreads.jsp?category=<%=currentCategory.getName()%>
									&board=<%=currentBoard.getName()%>">
					<%out.print(currentBoard.name);%> </a> <br>
				<div>
					<b>Moderators</b>:
					<%
						 			List<Moderator> currentBoardModerators = new ArrayList<Moderator>();
						 			
						 			currentBoardModerators = D.getModeratorsByBoardID(D.getBoardIDByName(currentBoard.name));
						 			Iterator<Moderator> it5 = currentBoardModerators.iterator();
						 			
						 			int moderatorsCount = 0;
						 			
						 			while (it5.hasNext())
						 			{
						 				if (moderatorsCount != currentBoardModerators.size()-1)
						 				
						 				{
						 					%>
					<%=it5.next().nickname + ","%>
					<% 
						 				}
						 				else
						 				{
						 					%>
					<%=it5.next().nickname%>
					<% 
						 				}
						 				
						 				moderatorsCount++;
						 			}
						 		 %>

				</div>
			</div>
			<%
						  	if ((request.getSession().getAttribute("role")) != null &&
						 		 request.getSession().getAttribute("role").equals("Admin")
						 	    )
						 	{
						%>
			<div style="width: 270px; float: right">
				<div class="addnewmoderatorclass">
					<a href="removemoderator.jsp?board=<%=currentBoard.getName() %>">
						Remove moderator </a>
				</div>

				<div class="addnewmoderatorclass">
					<a href="addmoderator.jsp?board=<%=currentBoard.getName() %>">
						Add moderator </a>
				</div>
			</div>
			<%
							}

							if ((request.getSession().getAttribute("role")) != null &&
								 request.getSession().getAttribute("role").equals("Admin"))
							{
						%>
			<a href="moveboard.jsp?category=<%=currentCategory.getName() %>
								  &board=<%=currentBoard.getName()%>"
								  class="movetoanothercategory">
								  Move
			</a>
			<%
							}
							
							currentBoard.threadsList = D.getThreads(D.getBoardIDByName(currentBoard.name));
							
							if ((request.getSession().getAttribute("role")) != null &&
								 request.getSession().getAttribute("role").equals("Admin") &&
								 currentBoard.threadsList.size() == 0
								)
							{
						%>
			<a href="DeleteBoard?board=<%=currentBoard.getName()%>"
				class="deleteboard"> Delete </a>
			<%
							}
						%>

			<div align="center">
				<%out.print(currentBoard.threadsList.size() + " thread(s)");%>
			</div>

		</div>
	</center>
	<%				
			}
		%>
	<br>
	<% 
		}	
	 %>
</body>
</html>