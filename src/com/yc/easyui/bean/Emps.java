package com.yc.easyui.bean;

import java.io.Serializable;

public class Emps  implements Serializable{
	private static final long serialVersionUID = -9109906931949744442L;
	private int eno;
	private int deptno;
	private String ename;
	private String pwd;
	private String tel;
	private String pic;
	private String intro;
	
	private String dname;
	
	@Override
	public String toString() {
		return "emps [eno=" + eno + ", deptno=" + deptno + ", ename=" + ename + ", pwd=" + pwd + ", tel=" + tel
				+ ", pic=" + pic + ", intro=" + intro + "]";
	}

	public int getEno() {
		return eno;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public void setEno(int eno) {
		this.eno = eno;
	}

	public int getDeptno() {
		return deptno;
	}

	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deptno;
		result = prime * result + ((ename == null) ? 0 : ename.hashCode());
		result = prime * result + eno;
		result = prime * result + ((intro == null) ? 0 : intro.hashCode());
		result = prime * result + ((pic == null) ? 0 : pic.hashCode());
		result = prime * result + ((pwd == null) ? 0 : pwd.hashCode());
		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emps other = (Emps) obj;
		if (deptno != other.deptno)
			return false;
		if (ename == null) {
			if (other.ename != null)
				return false;
		} else if (!ename.equals(other.ename))
			return false;
		if (eno != other.eno)
			return false;
		if (intro == null) {
			if (other.intro != null)
				return false;
		} else if (!intro.equals(other.intro))
			return false;
		if (pic == null) {
			if (other.pic != null)
				return false;
		} else if (!pic.equals(other.pic))
			return false;
		if (pwd == null) {
			if (other.pwd != null)
				return false;
		} else if (!pwd.equals(other.pwd))
			return false;
		if (tel == null) {
			if (other.tel != null)
				return false;
		} else if (!tel.equals(other.tel))
			return false;
		return true;
	}

	public Emps(int eno, int deptno, String ename, String pwd, String tel, String pic, String intro) {
		super();
		this.eno = eno;
		this.deptno = deptno;
		this.ename = ename;
		this.pwd = pwd;
		this.tel = tel;
		this.pic = pic;
		this.intro = intro;
	}

	public Emps() {
		super();
	}
}
