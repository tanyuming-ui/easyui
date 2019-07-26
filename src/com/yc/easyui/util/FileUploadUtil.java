package com.yc.easyui.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public class FileUploadUtil {
	public static String path = "files";
	public Map<String, String> upload(Servlet servlet, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			SmartUpload su = new SmartUpload();
			PageContext pagecontext = JspFactory.getDefaultFactory().getPageContext(servlet, request, response, null, true, 1024, true);
			su.initialize(pagecontext); // 初始化上传组件
			// 开始设置参数
			su.setCharset("utf-8");
			// 设置不允许上传的文件后缀
			//su.setDeniedFilesList("exe,bat,jsp,html");
			// 设置允许上传的文件格式
			su.setAllowedFilesList("jpg,png,gif,jpeg,doc,docx,xls,xlsx,zip,rar,mp3,mp4,txt");
			
			// 设置单个上传文件的最大大小
			su.setMaxFileSize(1024*1024*10);
			
			// 设置每次上传文件的最大总大小
			su.setTotalMaxFileSize(1024*1024*100);
			
			su.upload(); // 开始上传
			
			// 从转换后的请求对象中获取文件域表单参数
			Request req = su.getRequest();
			
			// 获取所有参数的名称
			Enumeration<String> enums = req.getParameterNames();
			String name = null;
			while (enums.hasMoreElements()) {
				name = enums.nextElement();
				map.put(name, req.getParameter(name));
			}
			
			Files files = su.getFiles();
			
			// 说明表单中有文件要上传
			if (files != null && files.getCount() > 0) {
				Collection<File> collection = files.getCollection();
				
				String fieldName = null;
				String fileName = null;
				String basePath = pagecontext.getServletContext().getRealPath("/") + path;
				String filePath = "";
				String preFieldName = "";
				for (File fl : collection) {
					if (fl != null && !fl.isMissing()) {
						fieldName = fl.getFieldName(); // 对应的这个文件选择框的name属性的属性值
						
						if ("".equals(preFieldName)) {
							preFieldName = fieldName;
						} else if (!"".equals(preFieldName) && !preFieldName.equals(fieldName)) {
							if (!"".equals(filePath)) {
								filePath = filePath.substring(0, filePath.lastIndexOf(";"));
							}
							map.put(preFieldName, filePath);
							preFieldName = fieldName;
							filePath = "";
						}
						//重命名文件名，为了避免文件重名
						fileName = new Date().getTime() + "-" + new Random().nextInt(10000) + "." +fl.getFileExt();
						
						// 将文件存到服务器 -> 注意：必须先创建一个目录，用来存放这个图片，那么在WebContent下创建一个images
						filePath += path + fileName + ";";
						fl.saveAs(basePath + fileName);
						
					}
				}
				if (!"".equals(filePath)) {
					filePath = filePath.substring(0, filePath.lastIndexOf(";"));
				}
				map.put(fieldName, filePath);
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
		System.out.println(map);
		return map;
	}
}
