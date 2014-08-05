package com.jdroid.javaweb.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.UrlPathHelper;
import com.jdroid.javaweb.api.BadRequestException;

public class ApiDispatcherServlet extends DispatcherServlet {
	
	/**
	 * @see org.springframework.web.servlet.DispatcherServlet#noHandlerFound(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestUri = new UrlPathHelper().getRequestUri(request);
		throw new BadRequestException("No mapping found for HTTP request", requestUri, request.getParameterMap(),
				request.getMethod(), getServletName());
	}
}
