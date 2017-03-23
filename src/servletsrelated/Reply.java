package servletsrelated;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import categoryrelated.Message;
import databaserelated.*;

public class Reply extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       

    public Reply() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		
		Message newMessage = new Message();
		
		newMessage.author = request.getSession().getAttribute("loginnickname").toString();
		newMessage.content = request.getParameter("messagetobeposted");
		newMessage.threadID = D.getThreadIDByName(request.getSession().getAttribute("currentthreadname").toString());

		java.util.Date date = new java.util.Date();
		long t = date.getTime();
		
		newMessage.date = String.valueOf(t);
		
		D.addNewMessage(newMessage);
		
		response.sendRedirect("showmessages.jsp?" +
							  					"category=" + request.getParameter("currentcategoryname").toString() +
							  					"&board=" + request.getParameter("currentboardname").toString() +
							  					"&thread=" + request.getParameter("currentthreadname").toString() +
							  					"&closed=" + request.getParameter("currentclosedname").toString()
							 );	
	}

}
