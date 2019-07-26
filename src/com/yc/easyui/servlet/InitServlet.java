package com.yc.easyui.servlet;

import java.io.File;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.yc.easyui.util.FileUploadUtil;

/**
 * 完成一些初始化工作，比如:创建必要的目录
 * @author navy
 */
@WebServlet(value="", initParams={@WebInitParam(name="uploadPath", value="files")}, loadOnStartup=1)
public class InitServlet extends HttpServlet{
	private static final long serialVersionUID = 7939869754491510077L;
	private String filePath = "files";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		String temp = config.getInitParameter("uploadPath");
		
		if (temp != null) {
			filePath = temp;
		}
		
		// 判断这个目录是否已经存在，如果不存在则创建
		String path = config.getServletContext().getRealPath("/"); // Tomcat/webapps/
		File fl = new File(path, "../" + filePath);
		if (!fl.exists()) {
			fl.mkdirs(); // 可能会涉及到多级目录的创建，所有用带s的这个
		}
		FileUploadUtil.path = "../" + filePath + "/";
	}
}
