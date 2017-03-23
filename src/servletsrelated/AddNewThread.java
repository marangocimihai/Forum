package servletsrelated;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import categoryrelated.Message;

import databaserelated.Database;


public class AddNewThread extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public AddNewThread() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		
		String currentCategoryName = request.getParameter("currentcategoryname").toString();
		String currentBoardName = request.getParameter("currentboardname").toString();
		String newThreadTitle = request.getParameter("newthreadtitle");
		
		if (newThreadTitle.trim().length() == 0)
		{
			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
  											"&board=" + currentBoardName + 
  											"&access=denied" +
  											"&action=newthread" +
  											"&newthreadtitle=zerolength");	
		}	
		else if (D.checkIfNewThreadNameIsAlreadyTaken(newThreadTitle))
		{
			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
											"&board=" + currentBoardName + 
											"&access=denied" +
											"&action=newthread" +
											"&newthreadtitle=alreadytaken");	
		}
		else
		{			
			categoryrelated.Thread newThread = new categoryrelated.Thread();
			
			newThread.name = request.getParameter("newthreadtitle");
			newThread.boardID = D.getBoardIDByName(currentBoardName);
			newThread.isSticky = false;
			newThread.isClosed = false;
			newThread.numberOfMessages = 1;
			
			D.addNewThread(newThread);
			
			Message newMessage = new Message();
			newMessage.author = request.getSession().getAttribute("loginnickname").toString();
			newMessage.content = request.getParameter("newthreadmessagecontent");
			newMessage.threadID = D.getThreadIDByName(newThread.name);
			
			java.util.Date date = new java.util.Date();
			long t = date.getTime();
			newMessage.date = String.valueOf(t);
			
			D.addNewMessage(newMessage);
			
			response.sendRedirect("showthreads.jsp?category=" + currentCategoryName + 
												 "&board=" + currentBoardName
								 );
		}
	}
}
