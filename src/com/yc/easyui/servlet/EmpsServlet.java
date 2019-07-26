package com.yc.easyui.servlet;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.easyui.bean.Emps;
import com.yc.easyui.dao.EmpsDao;
import com.yc.easyui.util.FileUploadUtil;

@WebServlet("/emp")
public class EmpsServlet extends BasicServlet {
	private static final long serialVersionUID = -219677111894389501L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		
		if ("findByPage".equals(op)) {
			findByPage(request, response);
		} else if ("addEmps".equals(op)) {
			addEmps(request, response);
		} else if ("upload".equals(op)){
			upload(request,response);
		}
	}

	private void upload(HttpServletRequest request, HttpServletResponse response)  throws IOException {
		FileUploadUtil fuu = new FileUploadUtil();
		Map<String, String> map = fuu.upload(this, request, response);
		String picPath = map.get("upload");
		System.out.println(picPath);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("filename", picPath.substring(picPath.lastIndexOf("/")));
		resultMap.put("uploaded", 1);
		resultMap.put("url", "..\\..\\..\\" + picPath);
		System.out.println(resultMap);
		this.send(response, resultMap);
		
	}

	private void addEmps(HttpServletRequest request, HttpServletResponse response) throws IOException {
		FileUploadUtil fuu = new FileUploadUtil();
		Map<String, String> map = fuu.upload(this, request, response);
		
		EmpsDao dao = new EmpsDao();
		int result = dao.addEmps(map.get("deptno"), map.get("ename")," ", map.get("tel"), map.get("pic"), map.get("intro"));
		System.out.println(result);
		this.send(response, result);
	}

	private void findByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int pageNo = Integer.parseInt(request.getParameter("page"));
		int pageSize = Integer.parseInt(request.getParameter("rows"));
		
		EmpsDao dao = new EmpsDao();
		List<Emps> list = dao.findByPage(pageNo, pageSize);
		
		int total = dao.getTotal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		this.send(response, map);
	}
}
