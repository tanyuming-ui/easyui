package com.yc.easyui.dao;

import java.util.List;

import com.yc.easyui.bean.UserInfo;

public class UserInfoDao{
	public UserInfo login(String uname, String pwd) {
		DBHelper db = new DBHelper();
		String sql = "select usid, uname, pwd, photo,files from userInfo "
				+ "where uname =? and pwd = ?";
		return db.find(UserInfo.class, sql, uname, pwd);
	}

	public List<UserInfo> findAll() {
		DBHelper db = new DBHelper();
		String sql = "select usid, uname, pwd, photo,files from userInfo order by usid";
		return db.finds(UserInfo.class, sql);
	}

	public List<UserInfo> findByPage(int pageNo, int pageSize) {
		DBHelper db = new DBHelper();
		String sql = "select * from (select a.*, rownum rn from (select usid, uname, pwd, "
				+ "photo,files from userInfo order by usid) a where rownum <= ?) where rn>?";
		return db.finds(UserInfo.class, sql, pageNo*pageSize, (pageNo-1)*pageSize);
	}

	public List<UserInfo> findByCondition(String uname, int pageNo, int pageSize) {
		DBHelper db = new DBHelper();
		String sql = "select * from (select a.*, rownum rn from ("
				+ "select usid, uname, pwd, photo,files from userInfo where uname like '%'||?||'%'"
				+ " order by usid) a where rownum <= ?) where rn>?";
		return db.finds(UserInfo.class, sql,uname, pageNo*pageSize, (pageNo-1)*pageSize);
	}

	public int getTotal() {
		DBHelper db = new DBHelper();
		String sql = "select count(usid) from userInfo";
		return db.getTotal(sql);
	}
	
	public int getTotal(String uname) {
		DBHelper db = new DBHelper();
		String sql = "select count(usid) from userInfo where uname like '%'||?||'%'";
		return db.getTotal(sql, uname);
	}
}
