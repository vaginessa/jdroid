package com.jdroid.javaweb.filter;

import com.jdroid.java.domain.Entity;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.context.AbstractSecurityContext;
import com.jdroid.javaweb.context.Application;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web Filter to add information to Log4J for logging
 * 
 */
public class Log4jFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(Log4jFilter.class);
	
	private static final String USER_ID = "userId";
	private static final String SESSION_ID = "sessionId";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		AbstractSecurityContext<?> securityContext = Application.get().getSecurityContext();
		Entity user = securityContext != null ? securityContext.getUser() : null;
		if (user != null) {
			// Add the user id to the mapped diagnostic context. May be shown using %X{userId} in the layout pattern.
			MDC.put(USER_ID, user.getId());
		}
		
		// Add the session id to the mapped diagnostic context. May be shown using %X{sessionId} in the layout pattern.
		MDC.put(SESSION_ID, request.getSession().getId());
		
		String queryString = request.getQueryString();
		LOGGER.info(request.getMethod() + " " + request.getRequestURI()
				+ (queryString != null ? "?" + request.getQueryString() : ""));
		
		try {
			// Continue processing the rest of the filter chain.
			filterChain.doFilter(request, response);
		} finally {
			// Remove the added elements - only if added.
			MDC.remove(USER_ID);
			MDC.remove(SESSION_ID);
		}
	}
}
