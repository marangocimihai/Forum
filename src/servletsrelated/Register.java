package servletsrelated;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databaserelated.Database;
import userrelated.User;;

public class Register extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
  
    public Register() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		Database D = new Database();
		
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");
		String retypedPassword = request.getParameter("retypedpassword");
		String email = request.getParameter("email");
		
		User user = new User();
		
		user.nickname = nickname;
		user.password = password;
		user.retypedPassword = retypedPassword;
		user.email = email;
		
		if (D.checkRegisterFormDetails(user) > 0)
		{
			response.sendRedirect("wrongregisterdetails.jsp");
		}
		else
		{
			D.getRegisterFormDetails(user);
			
			HttpSession session = request.getSession();
			session.setAttribute("islogged", "yes");
			session.setAttribute("loginnickname", user.nickname);
			session.setAttribute("role", "User");
			
			response.sendRedirect("home.jsp");
		}
	}
}
