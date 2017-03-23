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
<%@page import="java.text.*"%>
<%@page import="java.sql.Date"%>
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
	<%
		Database D = new Database();
		List<Message> currentMessagesList = new ArrayList<Message>();
		currentMessagesList = D.getMessagesByThread(D.getThreadIDByName(request.getParameter("thread")));
		
		Iterator<Message> it1 = currentMessagesList.iterator();
		
		%>
	<div
		style="border: 1px solid black; padding: 15px 0px 15px 5px; background: #D3D3D3;">
		<% out.println(request.getParameter("thread").toUpperCase()); %>
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
	<div align="left">

		<a href="home.jsp?"> Home </a>

		<%out.print(" > ");%>

		<%out.print(request.getParameter("category") + " > " );%>

		<a
			href="showthreads.jsp?category=<%=request.getParameter("category")%>
										&board=<%=request.getParameter("board") %>">
			<%=request.getParameter("board")%> </a>

		<%out.print(" > ");%>

		<a href="#" onclick="window.location.reload(true);"> <%=request.getParameter("thread")%>
		</a>

	</div>
	<% 
	
		if (request.getParameter("closed").equals("true"))
		{
			%>
	<center>
		<p style="color: white">
			<span style="background: red"> CLOSED THREAD </span>
		</p>
	</center>
	<% 
		}
		
		while (it1.hasNext())
		{
			Message currentMessage = new Message();
			currentMessage = it1.next();
			
			%>
	<center>
		<div style="width: 1168px; height: 150px;">
			<div style="border: 1px solid black; float: left; width: 104px; background: #D3D3D3;" class="badge">
				<div style="float: center">
					<% out.println(currentMessage.author); %>
				</div>
			</div>

			<div style="border: 1px solid black; padding: 5px 0px 5px 20px; float: right; width: 1000px; height: 150px; background-color: #F5F5F5;" class="panel-body">
				<div class="date">
					<% 
							Long currentDateInMilliseconds = Long.parseLong(currentMessage.date);
				
							Date currentDate = new Date(currentDateInMilliseconds);
							SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
							String dateText = sdf.format(currentDate);

							out.println(dateText); 
						%>
				</div>

				<div style="float: left; padding-top: 35px">
					<% out.println(currentMessage.content); %>
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
						
						if ((request.getSession().getAttribute("role") != null && 
							(request.getSession().getAttribute("islogged") != null && 
							 request.getSession().getAttribute("islogged").equals("yes")) &&
							 request.getSession().getAttribute("role").equals("Admin")) ||
							 request.getSession().getAttribute("role") != null && 
							(request.getSession().getAttribute("role").equals("Moderator") &&
							 moderatedBoard)
						   )
						{
							session.setAttribute("currentthread", "isModerated");
							
							if (!(currentMessage.ID == currentMessagesList.get(0).ID))
							{
					 %>
				<div class="deletemessage">
					<a
						href="DeleteMessage?category=<%=request.getParameter("category")%>
														   &board=<%=request.getParameter("board")%>
														   &thread=<%=request.getParameter("thread")%>
														   &author=<%= currentMessage.author%>
														   &content=<%=currentMessage.content%>
														   &date=<%=currentMessage.date%>
														   &moderator=<%=moderatedBoard%>
														   &closed=<%=request.getParameter("closed")%>">
						Delete </a>
				</div>
				<%
							}
						    %>

				<div class="editmessage">
					<a
						href="editmessage.jsp?
											category=<%=request.getParameter("category")%>
											&board=<%=request.getParameter("board")%>
											&thread=<%=request.getParameter("thread")%>
											&author=<%= currentMessage.author%>
											&content=<%=currentMessage.content%>
											&date=<%=currentMessage.date%>
											&moderator=<%=moderatedBoard%>
											&closed=<%=request.getParameter("closed")%>">
						Edit </a>
				</div>
				<%
						}
						
						if (currentMessage.editorID != 0  && !currentMessage.editDate.equals(null))
					  	{
					  %>
				<br>
				<br>
				<br>
				<br>

				<div style="float: left; width: 341px; padding: 50px 0px 0px 0px;">
					<div style="float: left">
						Last edited by <b> <%
					  						out.print(D.getUserNicknameByID(currentMessage.editorID));
					  					%> </b> at <u> <%
					  						Long editDateInMilliseconds = Long.parseLong(currentMessage.editDate);
				
											Date currentDate2 = new Date(editDateInMilliseconds);
											SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
											String dateText2 = sdf2.format(currentDate2);

											out.println(dateText2); 
					  					%> </u>
					</div>
				</div>
				<%
					  	}
					  %>
			</div>
		</div>
	</center>
	<br>
	<br>
	<% 	
		}
		
	boolean moderatedBoard2 = false;
								
	if (request.getSession().getAttribute("numberofmoderatedboards") != null)
	{
		for (int i = 0; i < Integer.parseInt(session.getAttribute("numberofmoderatedboards").toString()); i++)
		{
			if (Integer.parseInt(session.getAttribute("moderatedboard" + i).toString()) == D.getBoardIDByName(request.getParameter("board").toString()))
			{
				moderatedBoard2 = true;
			}
		}
	}
		
	if (request.getSession().getAttribute("islogged") != null)
	{
		if (request.getParameter("closed").equals("false") ||
		   (request.getSession().getAttribute("role") != null &&
		   (request.getSession().getAttribute("role").equals("Admin") ||
		   (request.getSession().getAttribute("role").equals("Moderator") &&
		    moderatedBoard2)))
		    )	  
		{
	 		%>
	<center>
		<div style="width: 387px">
			<form action="Reply" method="post">
				<textarea rows="4" cols="59" onclick="this.focus();this.select()"
					name="messagetobeposted">Type your message here...</textarea>

				<br> <input type="hidden"
					value="<%=request.getParameter("category")%>"
					name="currentcategoryname"> <input type="hidden"
					value="<%=request.getParameter("board")%>" name="currentboardname">
				<input type="hidden" value="<%=request.getParameter("thread")%>"
					name="currentthreadname"> <input type="hidden"
					value="<%=request.getParameter("closed")%>"
					name="currentclosedname"> <input type="submit"
					value="Reply">
			</form>
		</div>
	</center>
	<%
		}
		else
		{
			%>
	<center>
		<p>This thread is closed. You are not allowed to post a message
			here.</p>
	</center>
	<% 
		}
		
		session.setAttribute("currentthreadname", request.getParameter("thread"));
	}
	else
	{
		%>
	<center>
		<p>You are not allowed to post a new message since you are not
			logged in.</p>
	</center>
	<% 
	}
	 %>

</body>
</html>