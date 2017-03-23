package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import categoryrelated.Message;

import databaserelated.Database;


public class EditMessage extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public EditMessage() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		
		Message currentEditedMessage = new Message();
		
		String currentMessageCategory = request.getParameter("currentcategoryname").toString();
		String currentMessageBoard = request.getParameter("currentboardname").toString();
		String currentMessageThread = request.getParameter("currentthreadname").toString();
		String currentMessageClosed = request.getParameter("currentmessageclosed").toString();
		
		String currentEditedMessageAuthor = request.getParameter("currentmessageauthor").toString();
		String currentEditedMessageContent = request.getParameter("currentmessagecontent").toString();
		String currentEditedMessageDate = request.getParameter("currentmessagedate").toString();
		String currentMessageNewContent = request.getParameter("editedmessagecontent").toString();
		
		//get current edited message ID
		currentEditedMessage.author = currentEditedMessageAuthor;
		currentEditedMessage.content = currentEditedMessageContent;
		currentEditedMessage.date = currentEditedMessageDate;
		
		currentEditedMessage.ID = D.getMessageID(currentEditedMessage);
		
		//get current logged in user ID
		int editorNicknameID = D.getUserIDByNickname(request.getSession().getAttribute("loginnickname").toString());
		
		java.util.Date date = new java.util.Date();
		long t = date.getTime();
		String editDate = String.valueOf(t);
		
		D.editMessage(currentEditedMessage, editorNicknameID, currentMessageNewContent, editDate);
		
		response.sendRedirect("showmessages.jsp?category=" + currentMessageCategory + 
					  						  "&board=" + currentMessageBoard + 
					  						  "&thread=" + currentMessageThread +
					  						  "&closed=" + currentMessageClosed
							 );
	}
}
