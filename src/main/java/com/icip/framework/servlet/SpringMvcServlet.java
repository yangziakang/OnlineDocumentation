package com.icip.framework.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 重写Spring Mvc Servlet，处理输入URL没有requestMapping处理的情况。
 */
public class SpringMvcServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(SpringMvcServlet.class);

	@Override
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("not foud handle mapping for url: " + request.getRequestURI());
		super.noHandlerFound(request, response);
	}

	@Override
	protected void onRefresh(ApplicationContext context) {
		initStrategies(context);
	}

}
