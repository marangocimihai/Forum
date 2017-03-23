package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;


public class AddNewBoard extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       

    public AddNewBoard() 
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
		String newBoardName = request.getParameter("newboardtitle");
		
		if (newBoardName.trim().length() == 0)
		{
			System.out.println("zerolength");
			
			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
											"&access=denied" +
											"&action=addnewboard" +
											"&newboardtitle=zerolength");
		}
		else if (D.checkIfNewBoardNameIsAlreadyTaken(newBoardName.trim()))
		{
			System.out.println("taken");

			response.sendRedirect("checks.jsp?category=" + currentCategoryName + 
											"&access=denied" +
											"&action=addnewboard" +
					 						"&newboardtitle=alreadytaken");
		}
		else 
		{		
			System.out.println("success");

			int currentCategoryID = D.getCategoryIDByName(currentCategoryName);
		
			D.addNewBoard(newBoardName, currentCategoryID);
		
			response.sendRedirect("home.jsp");
		}
	}

}
