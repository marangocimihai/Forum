package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import categoryrelated.Message;

import databaserelated.Database;


public class DeleteMessage extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public DeleteMessage() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String currentCategoryName = request.getParameter("category").toString().trim();
		String currentBoardName = request.getParameter("board").toString().trim();
		String currentThreadName = request.getParameter("thread").toString().trim();
		String currentMessageAuthor = request.getParameter("author").toString().trim();
		String currentMessageContent = request.getParameter("content").toString().trim();
		String currentMessageDate = request.getParameter("date").toString().trim();
		String currentMessageClosed = request.getParameter("closed").toString().trim();
		
		if (request.getSession().getAttribute("role").equals("Admin") || 
		   (request.getSession().getAttribute("role").equals("Moderator") && 
		    request.getParameter("moderator").equals("true"))
		   )   
		{ 
			if (JOptionPane.showConfirmDialog(null,
					 "Are you sure you want to delete this message ?",
					 "Warning",
					 JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				Database D = new Database();
				
				Message currentMessage = new Message();
				
				currentMessage.author = currentMessageAuthor;
				currentMessage.content = currentMessageContent;
				currentMessage.date  = currentMessageDate;
				
				Message messageToDelete = new Message();
				messageToDelete.ID = D.getMessageID(currentMessage);
				D.deleteMessage(messageToDelete);
			}
			
			response.sendRedirect("showmessages.jsp?category=" + currentCategoryName + 
					   			 				  "&board=" + currentBoardName + 
					   			 				  "&thread=" + currentThreadName +
					   			 				  "&closed=" + currentMessageClosed
								 );
		} 
		else
		{		 
			response.sendRedirect("checks.jsp?access=denied" +
											 "&action=deletemessage" +
											 "&category=" +currentCategoryName +
											 "&board=" + currentBoardName + 
											 "&thread=" + currentThreadName + 
											 "&closed=" + currentMessageClosed);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//do nothing
	}
}
