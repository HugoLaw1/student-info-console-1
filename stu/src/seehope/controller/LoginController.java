package seehope.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import seehope.model.UserModel;
import seehope.security.SecurityContext;
import seehope.service.IUserService;
import seehope.service.impl.UserService;

public class LoginController {

	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		IUserService userService = new UserService();
		UserModel user = userService.getUserModel(username, password);
		if (user != null) {
			SecurityContext.setCurrentUser(user);
			response.sendRedirect(request.getContextPath() + "/portal/default/index.jsp");
		} else {
			response.sendRedirect(request.getContextPath() + "/common/login.jsp");
		}
	}
	
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		new SecurityContext().getSecurityProvider().logout(request);
		HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
            
        }
		response.sendRedirect(request.getContextPath() + "/common/login.jsp");
	}

}
