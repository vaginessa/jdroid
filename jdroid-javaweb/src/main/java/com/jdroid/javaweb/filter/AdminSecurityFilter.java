package com.jdroid.javaweb.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.context.Application;

/**
 * 
 * @author Maxi Rosson
 */
public class AdminSecurityFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AdminSecurityFilter.class);
	
	private static final String TOKEN_PARAMETER = "token";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getParameter(TOKEN_PARAMETER);
		if (getAdminToken().equals(token)) {
			filterChain.doFilter(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOGGER.warn("Invalid or missing admin security token");
		}
	}
	
	protected String getAdminToken() {
		return Application.get().getAppContext().getAdminToken();
	}
}
