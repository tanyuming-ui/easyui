package com.yc.easyui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class BasicServlet extends HttpServlet{
	private static final long serialVersionUID = 3594249794688978928L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		super.service(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void send(HttpServletResponse response, int result) throws IOException {
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
	}
	
	protected void send(HttpServletResponse response, String result) throws IOException {
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
	}
	
	protected void send(HttpServletResponse response, Object result) throws IOException {
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		//Gson gson = new GsonBuilder().serializeNulls().create();
		out.println(gson.toJson(result));
		out.flush();
	}
}
