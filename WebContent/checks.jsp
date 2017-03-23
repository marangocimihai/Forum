<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		if(request.getParameter("access") != null && request.getParameter("access").equals("denied"))
		{
			%>
	<center>
		ACCESS DENIED ! <br>
		<br>
	</center>
	<%
	 		
			if (request.getParameter("action") != null)
			{
				if (request.getParameter("action").equals("login"))
				{
					%>
	<center>Wrong username / password.</center>
	<%
	 				
	 				response.setHeader("Refresh", "5;url=login.jsp?");
				}
				else if (request.getParameter("action").equals("deletemessage"))
				{
					%>
	<center>You are not allowed to delete a message.</center>
	<%
	 			
	 				response.setHeader("Refresh", "5:url=showmessages.jsp?category=" + request.getParameter("category") + 
					   							      			 	 	 "&board="   + request.getParameter("board")    + 
					   							   					 	 "&thread="  + request.getParameter("thread")   +
					   							   					 	 "&closed="  + request.getParameter("closed")
						              ); 
				}
				else if(request.getParameter("action").equals("deletecategory"))
				{
					if (request.getParameter("hasboards") != null)
	 			 	{
	 			 		%>
	<br>
	<center>You are not allowed to delete a category which
		contains boards.</center>
	<% 
	 			 	}
	 			 		
	 			 	response.setHeader("Refresh", "5;url=home.jsp?");
				}
				else if(request.getParameter("action").equals("deleteboard"))
				{
					if (request.getParameter("hasthreads") != null)
	 			 	{
	 			 		%>
	<br>
	<center>You are not allowed to delete a board which contains
		threads.</center>
	<% 
	 			 	}
	 			 		
	 			 	response.setHeader("Refresh", "5;url=home.jsp?");
				}
				else if(request.getParameter("action").equals("closethread"))
				{
					response.setHeader("Refresh", "5;url=showthreads.jsp?category=" + request.getParameter("category") +
	 																   "&board=" + request.getParameter("board"));	
				}
				else if(request.getParameter("action").equals("addsticky"))
				{
					response.setHeader("Refresh", "5;url=showthreads.jsp?category=" + request.getParameter("category") +
	 																   "&board=" + request.getParameter("board"));	
				}
				else if(request.getParameter("action").equals("removesticky"))
				{
					response.setHeader("Refresh", "5;url=showthreads.jsp?category=" + request.getParameter("category") +
	 																   "&board=" + request.getParameter("board"));	
				}
				else if (request.getParameter("action").equals("modifythreadtitle") || request.getParameter("action").equals("newthread"))
				{
					if (request.getParameter("newthreadtitle") != null)
					{
						if (request.getParameter("newthreadtitle").equals("zerolength"))
						{
							%>
	<center>The title is required.</center>
	<% 
						}
						else if (request.getParameter("newthreadtitle").equals("alreadytaken"))
						{
							%>
	<center>This title is already used by another thread. Please
		choose another one.</center>
	<% 
						}
						
						if (request.getParameter("action").equals("modifythreadtitle"))
						{
							response.setHeader("Refresh", "5;url=modifythreadname.jsp?category=" + request.getParameter("category") + 
						  						 		  			 			    "&board=" + request.getParameter("board"));	
						}
						else if (request.getParameter("action").equals("newthread"))
						{
							response.setHeader("Refresh", "5;url=addthread.jsp?category=" + request.getParameter("category") + 
						  						 		  			 		 "&board=" + request.getParameter("board"));	 
						}
					}
				}
				else if (request.getParameter("action").equals("addnewboard"))
				{
					if (request.getParameter("newboardtitle") != null)
					{
						if (request.getParameter("newboardtitle").equals("zerolength"))
						{
							%>
	<center>The title is required.</center>
	<% 
						}
						else if (request.getParameter("newboardtitle").equals("alreadytaken"))
						{
							%>
	<center>This title is already used by another board. Please
		choose another one.</center>
	<% 
						}
						
						response.setHeader("Refresh", "5;url=addboard.jsp?category=" + request.getParameter("category"));
					}
				}
			}
			
			%>
	<br>
	<br>
	<center>In 5 seconds you will be redirected back to the
		previous page.</center>
	<% 
		}
		%>
</body>
</html>