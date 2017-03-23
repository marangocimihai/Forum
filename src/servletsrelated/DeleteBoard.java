package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import databaserelated.Database;


public class DeleteBoard extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       

    public DeleteBoard() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		
		if (request.getSession().getAttribute("role").equals("Admin"))
		{ 
			if (JOptionPane.showConfirmDialog(null,
					 						 "Are you sure you want to delete this board ?",
					 						 "Warning",
					 						 JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				Database D = new Database();
				
				String boardName = request.getParameter("board");
				
				boolean hasThreads = D.checkIfBoardHasThreads(D.getBoardIDByName(boardName));
				
				if (!hasThreads)
				{			
					D.deleteBoard(D.getBoardIDByName(boardName));
					
					response.sendRedirect("home.jsp");
				}
				else
				{
					response.sendRedirect("checks.jsp?access=denied&action=deleteboard&hasthreads=true");
				}
			}
			else
			{
				response.sendRedirect("home.jsp");
			}
		} 
		else
		{
			response.sendRedirect("checks.jsp?access=denied&action=deleteboard");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//do nothing
	}
}
