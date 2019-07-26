package com.yc.easyui.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 编码集过滤器
 * @author navy
 */
@WebFilter(urlPatterns={"/back/manager/*"})
public class CheckLoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		HttpSession session = req.getSession();
		if (session.getAttribute("loginUser") == null) { // 说明没有登录
			resp.setContentType("text/html;charset=utf-8");
			String path = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/back/index.html";
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('请先登录...');location.href='"+path+"';</script>");
			out.flush();
			out.close();
		} else { // 则交给下一个过滤器
			chain.doFilter(req, resp);
		}
	}

	/**
	 * 初始化方法，用来读取配置在这个filter中的初始化参数或全局初始化参数
	 */
	public void init(FilterConfig config) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
