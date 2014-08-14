package com.jdroid.javaweb.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.api.ApiExceptionHandler;
import com.jdroid.javaweb.context.Application;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;

/**
 * Filter used to verify that requests come from an authenticated user when accessing to content that requires
 * authentication.
 */
public class AbstractAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractAuthenticationFilter.class);
	
	private static final String USER_TOKEN_HEADER = "x-user-token";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (requiresAuthentication(request) && !isAuthenticated(request)) {
			response.setHeader(ApiExceptionHandler.STATUS_CODE_HEADER,
				CommonErrorCode.INVALID_USER_TOKEN.getStatusCode());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			
			filterChain.doFilter(request, response);
			
			Application.get().getSecurityContext().invalidate();
			request.getSession().invalidate();
		}
	}
	
	/**
	 * Checks whether the request requires user authentication to grant access to the requested content.
	 * 
	 * @param request The {@link HttpServletRequest} to check.
	 * @return If authentication is needed.
	 */
	private Boolean requiresAuthentication(HttpServletRequest request) {
		for (String each : getAllowedPaths()) {
			if (request.getPathInfo().startsWith(each)) {
				return false;
			}
		}
		return true;
	}
	
	private Boolean isAuthenticated(HttpServletRequest request) {
		String userToken = request.getHeader(USER_TOKEN_HEADER);
		try {
			Application.get().getSecurityContext().authenticateUser(userToken);
			return true;
		} catch (InvalidAuthenticationException e) {
			LOGGER.warn("User with token " + userToken + " NOT authenticated.");
			return false;
		}
	}
	
	public List<String> getAllowedPaths() {
		return Lists.newArrayList();
	}
}