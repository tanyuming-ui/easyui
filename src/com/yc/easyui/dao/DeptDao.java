package com.yc.easyui.dao;

import java.util.List;

import com.yc.easyui.bean.Dept;

public class DeptDao {
	public List<Dept> findByPage(int pageNo, int pageSize) {
		DBHelper db = new DBHelper();
		String sql = "select * from (select a.*, rownum rn from ("
				+ "select * from dept order by deptno) a where rownum<=?)  where rn >?";
		return db.finds(Dept.class, sql, pageNo * pageSize, (pageNo-1)*pageSize);
	}
	
	public int getTotal() {
		DBHelper db = new DBHelper();
		String sql = "select count(deptno) from dept";
		return db.getTotal(sql);
	}
	
	public int add(String deptno, String dname, String loc) {
		DBHelper db = new DBHelper();
		String sql = "insert into dept values(?,?,?)";
		return db.update(sql, deptno, dname, loc);
	}

	public int update(String deptno, String dname, String loc) {
		DBHelper db = new DBHelper();
		String sql = "update dept set dname=?,loc=? where deptno=?";
		return db.update(sql, dname, loc, deptno);
	}
	
	public int deleteDept(String deptno) {
		DBHelper db = new DBHelper();
		String sql = "delete from dept where deptno=?";
		return db.update(sql, deptno);
	}

	public List<Dept> findAll() {
		DBHelper db = new DBHelper();
		String sql ="select deptno, dname from dept";
		return db.finds(Dept.class, sql);
	}
}
