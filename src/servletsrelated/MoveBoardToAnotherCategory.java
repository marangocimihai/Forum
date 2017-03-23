package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;

public class MoveBoardToAnotherCategory extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       

    public MoveBoardToAnotherCategory() 
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
		
		String boardName = request.getParameter("currentboardname").toString();
		String selectedCategory = request.getParameter("selectedcategory").trim();
		D.moveBoardToAnotherCategory(boardName, selectedCategory);
		
		response.sendRedirect("home.jsp");
	}
}
