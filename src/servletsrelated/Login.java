package servletsrelated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databaserelated.Database;
import categoryrelated.Board;


public class Login extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Login() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//get the typed nickname and password
		String nickname = request.getParameter("loginnickname");
		String password = request.getParameter("loginpassword");
		
		//check if they match with some duo in the database
		Database D = new Database();
		//D.checkLoginFormDetails(nickname, password);
		
		//keep evidence of how many boards the current logged in user is able to moderate over
		int moderatedBoardCount = 0;
		
		//if yes, create a session and redirect to homepage
		if (D.checkLoginFormDetails(nickname, password))
		{
			HttpSession session = request.getSession();
			session.setAttribute("islogged", "yes");
			session.setAttribute("loginnickname", nickname);
			session.setAttribute("role", D.getUserRole(nickname));
						
			if(D.getUserRole(nickname).equals("Moderator"))
			{
				List<Board> moderatedBoards = new ArrayList<Board>();
				moderatedBoards = D.getModeratedBoardsByNicknameID(D.getUserIDByNickname(nickname));
				
				Iterator<Board> it = moderatedBoards.iterator();
				
				while (it.hasNext())
				{
					Board currentBoard = new Board();
					currentBoard = it.next();

					session.setAttribute("moderatedboard" + moderatedBoardCount, currentBoard.ID);
					
					moderatedBoardCount++;
				}
			}
			
			session.setAttribute("numberofmoderatedboards", moderatedBoardCount);
			
			response.sendRedirect("home.jsp");
		}
		//if not, redirect to log in
		else
		{
			response.sendRedirect("checks.jsp?access=denied&action=login");
		}
	}
}
