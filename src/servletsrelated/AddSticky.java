package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;

public class AddSticky extends HttpServlet 
{

	private static final long serialVersionUID = 1L;

    public AddSticky() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		
		String currentCategoryName = request.getParameter("category").toString();
		String currentBoardName = request.getParameter("board").toString();
		String currentThreadName = request.getParameter("thread").toString();
		
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
			D.setStickyThread(currentThreadName, true);
			
			response.sendRedirect("showthreads.jsp?category=" + currentCategoryName + 
												  "&board=" + currentBoardName
								 );
		} 
		else
		{
			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
										     "&board=" + currentBoardName + 
										     "&action=addsticky" +
											 "&access=denied");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//do nothing
	}
}
