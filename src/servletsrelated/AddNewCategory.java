package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;

public class AddNewCategory extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public AddNewCategory() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//do nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		String newCategoryName = request.getParameter("newcategoryname");
		
		if (!D.checkIfCategoryAlreadyExists(newCategoryName))
		{
			D.addNewCategory(newCategoryName);
			
			response.sendRedirect("home.jsp");
		}
		else
		{
			response.sendRedirect("checks.jsp?newcategoryname=0");
		}
	}
}
