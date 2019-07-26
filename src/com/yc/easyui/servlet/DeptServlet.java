package com.yc.easyui.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.easyui.bean.Dept;
import com.yc.easyui.dao.DeptDao;

@WebServlet("/dept")
public class DeptServlet extends BasicServlet {
	private static final long serialVersionUID = -219677111894389501L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		
		if ("findByPage".equals(op)) {
			findByPage(request, response);
		} else if ("addDept".equals(op)) {
			addDept(request, response);
		} else if ("updateDept".equals(op)) {
			updateDept(request, response);
		} else if ("deleteDept".equals(op)) {
			deleteDept(request, response);
		} else if ("findAll".equals(op)) {
			findAll(request, response);
		}
	}

	private void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DeptDao deptDao = new DeptDao();
		this.send(response, deptDao.findAll());
	}

	private void deleteDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String deptno = request.getParameter("deptno");
		
		DeptDao deptDao = new DeptDao();
		this.send(response, deptDao.deleteDept(deptno));
	}
	
	private void updateDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String deptno = request.getParameter("deptno");
		String dname = request.getParameter("dname");
		String loc = request.getParameter("loc");
		
		DeptDao deptDao = new DeptDao();
		this.send(response, deptDao.update(deptno, dname, loc));
	}

	private void addDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String deptno = request.getParameter("deptno");
		String dname = request.getParameter("dname");
		String loc = request.getParameter("loc");
		
		DeptDao deptDao = new DeptDao();
		this.send(response, deptDao.add(deptno, dname, loc));
	}

	private void findByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int pageNo = Integer.parseInt(request.getParameter("page"));
		int pageSize = Integer.parseInt(request.getParameter("rows"));
		
		DeptDao deptDao = new DeptDao();
		List<Dept> list = deptDao.findByPage(pageNo, pageSize);
		
		int total = deptDao.getTotal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		this.send(response, map);
	}
}
