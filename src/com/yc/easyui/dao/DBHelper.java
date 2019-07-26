package com.yc.easyui.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBHelper {
	/**
	 * 获取连接的方法
	 * @return
	 */
	private Connection getConnection() {
		Connection con = null;
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/orcl");
			con = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 关闭资源的方法
	 * @param rs
	 * @param pstmt
	 * @param con
	 */
	private void closeAll(ResultSet rs, PreparedStatement pstmt, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void setValues(PreparedStatement pstmt, List<Object> params) {
		if (pstmt == null || params == null || params.isEmpty()) {
			return;
		}

		for (int i = 0, len = params.size(); i < len; ++ i) {
			try {
				pstmt.setObject(i + 1, params.get(i));
			} catch (SQLException e) {
				System.out.println("第 " + (i + 1) + " 个占位符注值失败...");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给预编译执行语句中的占位符赋值
	 * @param pstmt 要赋值的预编译块
	 * @param params 值列表
	 */
	private void setValues(PreparedStatement pstmt, Object ... params) {
		if (pstmt == null || params == null || params.length <= 0) {
			return;
		}

		for (int i = 0, len = params.length; i < len; ++ i) {
			try {
				pstmt.setObject(i + 1, params[i]);
			} catch (SQLException e) {
				System.out.println("第 " + (i + 1) + " 个占位符注值失败...");
				e.printStackTrace();
			}
		}
	}



	/**
	 * 执行更新操作，包括update、delete和insert
	 * @param sql 要执行的更新语句
	 * @param params 要执行的更新语句中占位符?对应的值
	 * @return 返回语句执行后，所影响的数据行数
	 */
	public int update(String sql, Object ... params) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = -1;
		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译执行语句块
			this.setValues(pstmt, params); // 给预编译执行语句块中的占位符赋值
			result = pstmt.executeUpdate(); // 执行语句获取结果
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(null, pstmt, con);
		}
		return result;
	}

	/**
	 * 查询数据
	 * @param sql 要执行的查询语句
	 * @param params 要执行查询语句中占位符的值
	 * @return 每一行数据存到一个map中，以列名为键，对应列的值为值。将所有行记录存到list集合中。
	 */
	public List<Map<String, String>> find(String sql, Object ... params) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();
			Map<String, String> map = null;

			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}

			while(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				map = new HashMap<String, String>();
				// 列的名称已经写死，无法查其他的表? -> 能不能动态的获取结果集中每个列的列名?
				// 如果能取到，我们可以将这些列名存到一个数组中，然后遍历数组取值即可
				for (String col : columnNames) {
					map.put(col, rs.getString(col));
				}

				list.add(map); // 如果这一行的列数据读取结束，将这行数据存到list集合中
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return list;
	}

	public List<Map<String, String>> find(String sql, List<Object> params) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();
			Map<String, String> map = null;

			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}

			while(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				map = new HashMap<String, String>();
				// 列的名称已经写死，无法查其他的表? -> 能不能动态的获取结果集中每个列的列名?
				// 如果能取到，我们可以将这些列名存到一个数组中，然后遍历数组取值即可
				for (String col : columnNames) {
					map.put(col, rs.getString(col));
				}

				list.add(map); // 如果这一行的列数据读取结束，将这行数据存到list集合中
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return list;
	}

	/**
	 * 查询数据
	 * @param sql 要执行的查询语句
	 * @param params 要执行查询语句中占位符的值
	 * @return 每一行数据存到一个map中，以列名为键，对应列的值为值。将所有行记录存到list集合中。
	 */
	public Map<String, String> findSingle(String sql, Object ... params) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String> map = null;

		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();


			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}

			if(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				map = new HashMap<String, String>();
				// 列的名称已经写死，无法查其他的表? -> 能不能动态的获取结果集中每个列的列名?
				// 如果能取到，我们可以将这些列名存到一个数组中，然后遍历数组取值即可
				for (String col : columnNames) {
					map.put(col, rs.getString(col));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return map;
	}

	public Map<String, String> findSingle(String sql, List<Object> params) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String> map = null;

		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();


			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}

			if(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				map = new HashMap<String, String>();
				// 列的名称已经写死，无法查其他的表? -> 能不能动态的获取结果集中每个列的列名?
				// 如果能取到，我们可以将这些列名存到一个数组中，然后遍历数组取值即可
				for (String col : columnNames) {
					map.put(col, rs.getString(col));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return map;
	}

	/**
	 * 返回一个整形数的方法，如获取总记录数
	 * @param sql
	 * @param params
	 * @return
	 */
	public int getTotal(String sql, Object ... params) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;

		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return result;
	}

	/**
	 * 基于对象的查询
	 * @param cl 数据对应的类信息
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> finds(Class<?> c, String sql, Object ... params) {
		List<T> list = new ArrayList<T>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		T t = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();

			Method[] methods = c.getDeclaredMethods(); // 获取指定的类中的所有方法
			List<Method> setters = new ArrayList<Method>(); // 获取类中的所有setter方法
			Map<String, String> typeNames = new HashMap<String, String>();
			for(Method method : methods) {
				if (method.getName().startsWith("set")) {
					setters.add(method);

					// 获取这个setter方法的第一个参数的类型
					// 获取这个类型对应的类的类型，我用的是SimpleName，说明不需要获取这个类的包路径，主要类名
					typeNames.put(method.getName(), method.getParameterTypes()[0].getSimpleName()); 
				}
			}

			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}

			String mname = null;
			String tname = null;
			while(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				// 根据类先实例化一个对象那个
				t = (T) c.newInstance(); // t = new UserInfo();

				// 循环这一行数据中的所有列，取出每一列的数据
				for (String colName : columnNames) { // usid  uname pwd
					mname = "set" + colName; // setuisd setuname setpwd
					// 循环所有的方法，找到这一列对应的注入是setter方法
					for (Method md : setters) {
						if (mname.equalsIgnoreCase(md.getName())) { // setUsid setUname setPwd
							tname = typeNames.get(md.getName());
							if ("int".equals(tname) || "Integer".equals(tname)) {
								md.invoke(t, rs.getInt(colName)); // t.setUsid(1001) t.setUname("yc")
							} else if ("float".equals(tname) || "Float".equals(tname)) {
								md.invoke(t, rs.getFloat(colName)); 
							} else if ("double".equals(tname) || "Double".equals(tname)) {
								md.invoke(t, rs.getDouble(colName)); 
							} else {
								md.invoke(t, rs.getString(colName)); // t.setUsid(1001) t.setUname("yc")
							}
							break;
						}
					}
				}
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return list;
	}

	/**
	 * 基于对象的查询
	 * @param cl 数据对应的类信息
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> finds(Class<?> c, String sql, List<Object> params) {
		List<T> list = new ArrayList<T>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		T t = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();

			Method[] methods = c.getDeclaredMethods(); // 获取指定的类中的所有方法
			List<Method> setters = new ArrayList<Method>(); // 获取类中的所有setter方法
			Map<String, String> typeNames = new HashMap<String, String>();
			for(Method method : methods) {
				if (method.getName().startsWith("set")) {
					setters.add(method);

					// 获取这个setter方法的第一个参数的类型
					// 获取这个类型对应的类的类型，我用的是SimpleName，说明不需要获取这个类的包路径，主要类名
					typeNames.put(method.getName(), method.getParameterTypes()[0].getSimpleName()); 
				}
			}

			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}

			String mname = null;
			String tname = null;
			while(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				// 根据类先实例化一个对象那个
				t = (T) c.newInstance(); // t = new UserInfo();

				// 循环这一行数据中的所有列，取出每一列的数据
				for (String colName : columnNames) { // usid  uname pwd
					mname = "set" + colName; // setuisd setuname setpwd
					// 循环所有的方法，找到这一列对应的注入是setter方法
					for (Method md : setters) {
						if (mname.equalsIgnoreCase(md.getName())) { // setUsid setUname setPwd
							tname = typeNames.get(md.getName());
							if ("int".equals(tname) || "Integer".equals(tname)) {
								md.invoke(t, rs.getInt(colName)); // t.setUsid(1001) t.setUname("yc")
							} else if ("float".equals(tname) || "Float".equals(tname)) {
								md.invoke(t, rs.getFloat(colName)); 
							} else if ("double".equals(tname) || "Double".equals(tname)) {
								md.invoke(t, rs.getDouble(colName)); 
							} else {
								md.invoke(t, rs.getString(colName)); // t.setUsid(1001) t.setUname("yc")
							}
							break;
						}
					}
				}
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T find(Class<?> c, String sql, Object ... params) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		T t = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();
			
			Method[] methods = c.getDeclaredMethods(); // 获取指定的类中的所有方法
			List<Method> setters = new ArrayList<Method>(); // 获取类中的所有setter方法
			Map<String, String> typeNames = new HashMap<String, String>();
			for(Method method : methods) {
				if (method.getName().startsWith("set")) {
					setters.add(method);
					
					// 获取这个setter方法的第一个参数的类型
					// 获取这个类型对应的类的类型，我用的是SimpleName，说明不需要获取这个类的包路径，主要类名
					typeNames.put(method.getName(), method.getParameterTypes()[0].getSimpleName()); 
				}
			}
			
			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}
			
			String mname = null;
			String tname = null;
			if(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				// 根据类先实例化一个对象那个
				t = (T) c.newInstance(); // t = new UserInfo();
				
				// 循环这一行数据中的所有列，取出每一列的数据
				for (String colName : columnNames) { // usid  uname pwd
					mname = "set" + colName; // setuisd setuname setpwd
					// 循环所有的方法，找到这一列对应的注入是setter方法
					for (Method md : setters) {
						if (mname.equalsIgnoreCase(md.getName())) { // setUsid setUname setPwd
							tname = typeNames.get(md.getName());
							if ("int".equals(tname) || "Integer".equals(tname)) {
								md.invoke(t, rs.getInt(colName)); // t.setUsid(1001) t.setUname("yc")
							} else if ("float".equals(tname) || "Float".equals(tname)) {
								md.invoke(t, rs.getFloat(colName)); 
							} else if ("double".equals(tname) || "Double".equals(tname)) {
								md.invoke(t, rs.getDouble(colName)); 
							} else {
								md.invoke(t, rs.getString(colName)); // t.setUsid(1001) t.setUname("yc")
							}
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return t;
	}
	
	/**
	 * 基于对象的查询
	 * @param cl 数据对应的类信息
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T find(Class<?> c, String sql, List<Object> params) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		T t = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setValues(pstmt, params);
			rs = pstmt.executeQuery();
			
			Method[] methods = c.getDeclaredMethods(); // 获取指定的类中的所有方法
			List<Method> setters = new ArrayList<Method>(); // 获取类中的所有setter方法
			Map<String, String> typeNames = new HashMap<String, String>();
			for(Method method : methods) {
				if (method.getName().startsWith("set")) {
					setters.add(method);
					
					// 获取这个setter方法的第一个参数的类型
					// 获取这个类型对应的类的类型，我用的是SimpleName，说明不需要获取这个类的包路径，主要类名
					typeNames.put(method.getName(), method.getParameterTypes()[0].getSimpleName()); 
				}
			}
			
			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集在元数据信息
			int columnCount = rsmd.getColumnCount(); // 获取结果集中列数量
			String[] columnNames = new String[columnCount]; // 声明一个数组，用来存放列的名称
			for (int i = 0; i < columnCount; ++ i) {
				columnNames[i] = rsmd.getColumnName(i + 1).toLowerCase();
			}
			
			String mname = null;
			String tname = null;
			if(rs.next()) { // 每循环一次就是一行数据，那么要将这一行数据存到map中
				// 根据类先实例化一个对象那个
				t = (T) c.newInstance(); // t = new UserInfo();
				
				// 循环这一行数据中的所有列，取出每一列的数据
				for (String colName : columnNames) { // usid  uname pwd
					mname = "set" + colName; // setuisd setuname setpwd
					// 循环所有的方法，找到这一列对应的注入是setter方法
					for (Method md : setters) {
						if (mname.equalsIgnoreCase(md.getName())) { // setUsid setUname setPwd
							tname = typeNames.get(md.getName());
							if ("int".equals(tname) || "Integer".equals(tname)) {
								md.invoke(t, rs.getInt(colName)); // t.setUsid(1001) t.setUname("yc")
							} else if ("float".equals(tname) || "Float".equals(tname)) {
								md.invoke(t, rs.getFloat(colName)); 
							} else if ("double".equals(tname) || "Double".equals(tname)) {
								md.invoke(t, rs.getDouble(colName)); 
							} else {
								md.invoke(t, rs.getString(colName)); // t.setUsid(1001) t.setUname("yc")
							}
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			this.closeAll(rs, pstmt, con);
		}
		return t;
	}
}
