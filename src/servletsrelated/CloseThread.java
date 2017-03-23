package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import databaserelated.Database;


public class CloseThread extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

    public CloseThread() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String currentCategoryName = request.getParameter("category").toString();
		String currentBoardName = request.getParameter("board").toString();
		String currentThreadName = request.getParameter("thread").toString();
		
		if (JOptionPane.showConfirmDialog(null,
										 "Are you sure you want to close this thread ?",
										 "Warning",
										  JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{
			Database D = new Database();
			
			boolean moderatedBoard = false;
										
			if (request.getSession().getAttribute("numberofmoderatedboards") != null)
			{
				for (int i = 0; i < Integer.parseInt(request.getSession().getAttribute("numberofmoderatedboards").toString()); i++)
				{
					if (Integer.parseInt(request.getSession().getAttribute("moderatedboard" + i).toString()) == D.getBoardIDByName(request.getParameter("board").toString()))
					{
						moderatedBoard = true;
					}
				}
			}
			
			if (request.getSession().getAttribute("role").equals("Admin") ||
				request.getSession().getAttribute("role").equals("Moderator") && 
				moderatedBoard
			)
			{ 
				categoryrelated.Thread currentThread = new categoryrelated.Thread();
				
				currentThread.name = currentThreadName;
				currentThread.ID = D.getThreadIDByName(currentThread.name);
				
				D.closeThread(currentThread);
				
				response.sendRedirect("showthreads.jsp?category=" + currentCategoryName + 
						  							  "&board=" + currentBoardName
									  );
			} 
			else
			{
				response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
					     						 "&board=" + currentBoardName + 
					     						 "&action=closethread" +
						 						 "&access=denied");
			}	
		}
		else
		{
			response.sendRedirect("showthreads.jsp?category=" + currentCategoryName + 
					  							 "&board=" + currentBoardName
								  );		
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//do nothing
	}
}
