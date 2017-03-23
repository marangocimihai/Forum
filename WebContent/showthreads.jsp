<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="categoryrelated.Category"%>
<%@page import="categoryrelated.Board"%>
<%@page import="categoryrelated.Thread"%>
<%@page import="categoryrelated.Message"%>
<%@page import="databaserelated.Database"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet" href="cssfiles/smth.css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<body>

	<% 
		Database D = new Database();
		int selectedBoardID = D.getBoardIDByName(request.getParameter("board"));
		
		List<categoryrelated.Thread> threadsList = new ArrayList<categoryrelated.Thread>();
		threadsList = D.getThreads(selectedBoardID);
		
		Iterator<categoryrelated.Thread> it1 = threadsList.iterator();
		
		%>
	<div
		style="border: 1px solid black; padding: 15px 0px 15px 5px; background: #D3D3D3">
		<% out.println(request.getParameter("board").toUpperCase()); %>
	</div>
	<br>

	<%
			if (request.getSession().getAttribute("islogged") != null)
			{		
	%>
	<div style="padding-bottom: 40px">
		<p style="float: left">
			Logged in as
			<%=request.getSession().getAttribute("loginnickname") %>.
		</p>

		<form action="login.jsp">
			<input type="submit" value="Logout" style="float:right; padding-right:10px" class="btn-danger">
		</form>
	</div>
	<% 
			}
			else
			{
		%>
				<div style="padding-bottom: 5px;">
					You are not logged in. Press <a href="login.jsp">here</a> to login or
					<a href="register.jsp">here</a> to register.
				</div>
	<% 
			}
		%>
	<div>
		<div style="margin-left:13.4%;">

			<a href="home.jsp?">Home</a>

			<%out.print(" > ");%>

			<%out.print(request.getParameter("category") + " > " );%>

			<a href="#" onclick="window.location.reload(true);"> <%=request.getParameter("board")%></a>
			
			
			<%
				if (request.getSession().getAttribute("islogged") != null)
				{
					%>
						<div style="float:right; padding-right:252px;" >
							<a href="addthread.jsp?category=<%=request.getParameter("category")%>
							  	  				  &board=<%=request.getParameter("board")%>" class="button">
													New thread 
							</a>
						</div>
					<%
				}
	 		%>
			
		</div>
		<% 
			
		while (it1.hasNext())
		{
			categoryrelated.Thread currentThread = new categoryrelated.Thread();
			currentThread = it1.next();
			
			if (currentThread.isSticky)
			{
				Category currentCategory = new Category();
				Board currentBoard = new Board();
			
				%>
		<center>
			<div style="padding: 18px 0px 5px 30px; background-color: #C0C0C0; width: 1400px; height: 60px; margin-bottom:5px;" class="panel panel-default">
				<p style="float: left; color: white; font-size: 70%; padding-right: 5px;">
					<span style="background: green" class="badge">STICKY</span>
				</p>

				<%
								if (currentThread.isClosed)
								{
									%>
				<p style="float: left; color: white; font-size: 70%; padding-right: 5px;">
					<span style="background: red" class="badge">CLOSED</span>
				</p>
				<% 
								}
								else
								
							 %>

				<b><a
					href="showmessages.jsp?
								category=<%=request.getParameter("category")%>
								&board=<%=request.getParameter("board")%>
								&thread=<%=currentThread.getName()%>
								&closed=<%=currentThread.isClosed %>"
					style="float: left; padding: 0px 0px 5px 10px;"> <%
									out.print(currentThread.name);  
								%> </a></b>

				<div style="float: left; padding-left: 5px;">
					by <b> <%
										out.println(currentThread.messagesList.get(0).author);
									%> </b> at <u> <%
										Long mostRecentMessageDateInMilliseconds0 = Long.parseLong(currentThread.messagesList.get(0).date);
				
										Date currentDate0 = new Date(mostRecentMessageDateInMilliseconds0);
										SimpleDateFormat sdf0 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
										String dateText0 = sdf0.format(currentDate0);
										out.println(dateText0);
							 		%> </u>
				</div>

				<div style="float: right">
					<%
									if (!currentThread.messagesList.isEmpty())
									{
										Message mostRecentMessage = new Message();
										mostRecentMessage = D.getMostRecentMessageDetails(currentThread.ID);
										out.println(mostRecentMessage.author);
									
										Long mostRecentMessageDateInMilliseconds = Long.parseLong(mostRecentMessage.date);
				
										Date currentDate = new Date(mostRecentMessageDateInMilliseconds);
										SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
										String dateText = sdf.format(currentDate);
										%>
					<br>
					<div style="padding-right: 5px">
						<% 
											out.println(dateText); 
										%>
					</div>
					<% 
									}
									else
									{
										%>
					<div style="width: 138px">
						<center>
							<p>
								<%out.println("N/A");%>
							</p>
						</center>
					</div>
					<% 
									}
								 %>
				</div>

				<div style="float: right; padding-right: 40px;">
					<%
									out.println(currentThread.messagesList.size());
								%>
					post(s)
				</div>

				<%
								boolean moderatedBoard = false;
								
								if (request.getSession().getAttribute("numberofmoderatedboards") != null)
								{
									for (int i = 0; i < Integer.parseInt(session.getAttribute("numberofmoderatedboards").toString()); i++)
									{
										if (Integer.parseInt(session.getAttribute("moderatedboard" + i).toString()) == D.getBoardIDByName(request.getParameter("board").toString()))
										{
											moderatedBoard = true;
										}
									}
								}
								
								if (((request.getSession().getAttribute("role")) != null && 
									 request.getSession().getAttribute("role").equals("Admin")) ||
									 request.getSession().getAttribute("role") != null &&
									 request.getSession().getAttribute("role").equals("Moderator") &&
									 moderatedBoard
								   )
								{
							%>
				<div>
					<a
						href="RemoveSticky?
												category=<%=request.getParameter("category")%>
												&board=<%=request.getParameter("board")%>
												&thread=<%=currentThread.getName()%>"
						class="sticky"> Remove sticky </a>
				</div>

				<div>
					<a
						href="modifythreadname.jsp?
												category=<%=request.getParameter("category")%>
												&board=<%=request.getParameter("board")%>
												&thread=<%=currentThread.getName()%>"
						class="modifytitle"> Modify title </a>
				</div>

				<%
									if (!D.checkIfThreadIsClosed(currentThread))
									{
								 %>
				<div>
					<a
						href="CloseThread?category=<%=request.getParameter("category")%>
													 &board=<%=request.getParameter("board")%>
													 &thread=<%=currentThread.getName()%>"
						class="closethread"> Close </a>
				</div>
				<%
									}
								%>
				<%
								}
							%>
			</div>
		</center>
	</div>
	<% 
			}
			else
			{
				Category currentCategory = new Category();
				Board currentBoard = new Board();
			
				%>
	<center>
		<div style="padding: 18px 0px 5px 30px; background-color: #F5F5F5; width: 1400px; height: 60px; margin-bottom:5px;" class="panel panel-default" class="shadow">
			<%
								if (currentThread.isClosed)
								{
									%>
			<p style="float: left; color: white; font-size: 70%; padding-right: 5px;">
				<span style="background: red" class="badge">CLOSED</span>
			</p>
			<% 
								}
								else		
							 %>

			<b><a
				href="showmessages.jsp?
								category=<%=request.getParameter("category")%>
								&board=<%=request.getParameter("board")%>
								&thread=<%=currentThread.getName()%>
								&closed=<%=currentThread.isClosed %>"
				style="float: left"> <%out.print(currentThread.name);%></a></b>

			<div style="float: left; padding-left: 5px;">
				by <b> <%
										out.println(currentThread.messagesList.get(0).author);
									%> </b> at <u> <%
										Long mostRecentMessageDateInMilliseconds0 = Long.parseLong(currentThread.messagesList.get(0).date);
				
										Date currentDate0 = new Date(mostRecentMessageDateInMilliseconds0);
										SimpleDateFormat sdf0 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
										String dateText0 = sdf0.format(currentDate0);
										out.println(dateText0);
							 		%> </u>
			</div>

			<div style="float: right;">
				<%
									if (!currentThread.messagesList.isEmpty())
									{
										Message mostRecentMessage = new Message();
										mostRecentMessage = D.getMostRecentMessageDetails(currentThread.ID);
										out.println(mostRecentMessage.author);
									
										Long mostRecentMessageDateInMilliseconds = Long.parseLong(mostRecentMessage.date);
				
										Date currentDate = new Date(mostRecentMessageDateInMilliseconds);
										SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
										String dateText = sdf.format(currentDate);
										%>
				<br>
				<div style="padding-right: 5px">
					<% 
											out.println(dateText); 
										%>
				</div>
				<% 
									}
									else
									{
										%>
				<div style="width: 138px">
					<center>
						<p>
							<%out.println("N/A");%>
						</p>
					</center>
				</div>
				<% 
									}
								 %>
			</div>

			<div style="float: right; padding-right: 40px;">
				<%
									out.println(currentThread.messagesList.size());
								%>
				post(s)
			</div>

			<%
								boolean moderatedBoard1 = false;
								
								if (request.getSession().getAttribute("numberofmoderatedboards") != null)
								{
									for (int i = 0; i < Integer.parseInt(session.getAttribute("numberofmoderatedboards").toString()); i++)
									{
										if (Integer.parseInt(session.getAttribute("moderatedboard" + i).toString()) == D.getBoardIDByName(request.getParameter("board").toString()))
										{
											moderatedBoard1 = true;
										}
									}
								}
								
								if ((request.getSession().getAttribute("role")) != null && 
									(request.getSession().getAttribute("islogged") != null && 
							 		 request.getSession().getAttribute("islogged").equals("yes")) &&
									 request.getSession().getAttribute("role").equals("Admin")||
									 request.getSession().getAttribute("role") != null &&
									 request.getSession().getAttribute("role").equals("Moderator") &&
									 moderatedBoard1
								   )
									
								{
							%>
			<div>
				<a
					href="AddSticky?category=<%=request.getParameter("category")%>
											&board=<%=request.getParameter("board")%>
											&thread=<%=currentThread.getName()%>"
					class="sticky"> Mark as sticky </a>
			</div>

			<div>
				<a
					href="modifythreadname.jsp?
												category=<%=request.getParameter("category")%>
												&board=<%=request.getParameter("board")%>
												&thread=<%=currentThread.getName()%>"
					class="modifytitle"> Modify title </a>
			</div>

			<%
									if (!D.checkIfThreadIsClosed(currentThread))
									{
								 %>
			<div>
				<a
					href="CloseThread?category=<%=request.getParameter("category")%>
												 &board=<%=request.getParameter("board")%>
												 &thread=<%=currentThread.getName()%>"
					class="closethread"> Close </a>
			</div>
			<%
									}
								%>
			<%
								}
							%>
		</div>
	</center>
	<%
			}
		}
	%>
	<br>
	<%
		if (request.getSession().getAttribute("islogged") != null)
		{
	%>

	<%
		}
	 %>
</body>
</html>