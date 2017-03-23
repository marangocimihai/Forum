package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;


public class AddNewModerator extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

    public AddNewModerator() 
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
		int currentBoardID = D.getBoardIDByName(request.getParameter("currentboardname"));
		String newModeratorNickname = request.getParameter("newmoderator").toString().trim();
		
		D.addNewModerator(newModeratorNickname, currentBoardID);
		
		response.sendRedirect("home.jsp");
	}

}
