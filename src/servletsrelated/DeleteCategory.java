package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import databaserelated.Database;;


public class DeleteCategory extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public DeleteCategory() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		if (request.getSession().getAttribute("role").equals("Admin"))
		{ 
			if (JOptionPane.showConfirmDialog(null,
					 "Are you sure you want to delete this category ?",
					 "Warning",
					 JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				Database D = new Database();
				
				boolean hasBoards = D.checkIfCategoryHasBoards(D.getCategoryIDByName(request.getParameter("category")));
				
				if (!hasBoards)
				{			
					D.deleteCategory(D.getCategoryIDByName(request.getParameter("category").toString()));
					
					response.sendRedirect("home.jsp");
				}
				else
				{
					response.sendRedirect("checks.jsp?access=denied&action=deletecategory&hasboards=true");
				}
			}
			else
			{
				response.sendRedirect("home.jsp");
			}
		} 
		else
		{
			response.sendRedirect("checks.jsp?access=denied&action=deletecategory");
		}
	}
}
