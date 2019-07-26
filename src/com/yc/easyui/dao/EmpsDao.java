package com.yc.easyui.dao;

import java.util.List;

import com.yc.easyui.bean.Emps;

public class EmpsDao {
	public List<Emps> findByPage(int pageNo, int pageSize) {
		DBHelper db = new DBHelper();
		String sql = "select * from (select a.*, rownum rn from ("
				+ "select * from emps order by eno) a where rownum<=?)  where rn >?";
		return db.finds(Emps.class, sql, pageNo * pageSize, (pageNo-1)*pageSize);
	}
	
	public int addEmps(String deptno, String ename, String pwd, String tel, String pic, String intro) {
		DBHelper db = new DBHelper();
		String sql = "insert into emps values(seq_emp_eno.nextval, ?, ?, ?, ?, ?, ?)";
		return db.update(sql, deptno, ename,pwd, tel, pic, intro);
	}
	
	public int getTotal() {
		DBHelper db = new DBHelper();
		String sql = "select count(eno) from emps";
		return db.getTotal(sql);
	}
	
}
