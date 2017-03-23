package servletsrelated;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;


public class ModifyThreadTitle extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public ModifyThreadTitle() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		
		String currentCategoryName = request.getParameter("currentcategoryname");
		String currentBoardName = request.getParameter("currentboardname");
		String currentThreadName = request.getParameter("currentthreadname");
		String newTitle = request.getParameter("newtitle").trim();
		
		if (newTitle.trim().length() == 0)
		{
			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
						  					"&board=" + currentBoardName + 
						  					"&access=denied" +
						  					"&action=modifythreadtitle" +
						  					"&newthreadtitle=zerolength");	
		}
		else if(D.checkIfNewThreadNameIsAlreadyTaken(newTitle))
		{
			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
					  						"&board=" + currentBoardName + 
					  						"&access=denied" +
						  					"&action=modifythreadtitle" +
					  						"&newthreadtitle=alreadytaken");	
		}
		else
		{
			categoryrelated.Thread currentThread = new categoryrelated.Thread();
		
			currentThread.name = request.getParameter("newtitle");
			currentThread.ID = D.getThreadIDByName(currentThreadName.trim());
			D.modifyThreadTitle(currentThread);
		
			response.sendRedirect("showthreads.jsp?category=" + currentCategoryName + 
				   							  	  "&board=" + currentBoardName + 
				   							  	  "&thread=" + currentThreadName);
		}
	}
}
