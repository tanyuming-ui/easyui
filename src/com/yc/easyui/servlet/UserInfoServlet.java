package com.yc.easyui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yc.easyui.bean.UserInfo;
import com.yc.easyui.dao.UserInfoDao;

@WebServlet("/user")
public class UserInfoServlet extends BasicServlet {
	private static final long serialVersionUID = -219677111894389501L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		
		if ("login".equals(op)) {
			login(request, response);
		} 
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uname = request.getParameter("uname");
		String pwd = request.getParameter("pwd");
		
		UserInfoDao userInfoDao = new UserInfoDao();
		UserInfo uf = userInfoDao.login(uname, pwd);
		int result = 0;
		if (uf != null) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", uf);
			result = 1;
		}
		this.send(response, result);
	}
}
