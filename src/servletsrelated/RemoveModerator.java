package servletsrelated;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databaserelated.Database;

public class RemoveModerator extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

    public RemoveModerator() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if (request.getSession().getAttribute("role").equals("Admin"))
		{
			Database D = new Database();
					
			String nickname = request.getParameter("removemoderator").toString().trim();
			String boardName = request.getParameter("currentboardname");
			int currentBoardID = D.getBoardIDByName(boardName);
			
			D.removeModerator(nickname, currentBoardID);
			
			response.sendRedirect("home.jsp");
		}
		else
		{
			PrintWriter out = response.getWriter();
			out.println("<center><h1>" + "ACCESS DENIED" + "</center></h1>");
		}
	}

}
