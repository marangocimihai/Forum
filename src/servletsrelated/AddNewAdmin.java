package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;

public class AddNewAdmin extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

    public AddNewAdmin() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// do nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Database D = new Database();
		
		String newAdminNickname = request.getParameter("newadmin").trim();

		D.addNewAdmin(newAdminNickname);
		
		response.sendRedirect("admincontrolpanel.jsp");
	}
}
