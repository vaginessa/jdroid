package com.jdroid.javaweb.filter;

import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.api.ApiExceptionHandler;
import com.jdroid.javaweb.context.Application;
import com.jdroid.javaweb.exception.CommonErrorCode;

import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class APIVersionFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(APIVersionFilter.class);
	
	private static final String API_VERSION_HEADER = "api-version";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Long minApiVersion = Long.parseLong(Application.get().getAppContext().getMinApiVersion().replace(".", ""));
		String header = request.getHeader(API_VERSION_HEADER);
		Long clientApiVersion = header != null ? Long.parseLong(header.replace(".", "")) : null;
		if (clientApiVersion == null) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOGGER.warn("Missing [" + API_VERSION_HEADER + "] header");
		} else if (clientApiVersion >= minApiVersion) {
			response.setHeader(ApiExceptionHandler.STATUS_CODE_HEADER, ApiExceptionHandler.OK_STATUS_CODE_HEADER_VALUE);
			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(request, response);
		} else {
			response.setHeader(ApiExceptionHandler.STATUS_CODE_HEADER,
				CommonErrorCode.INVALID_API_VERSION.getStatusCode());
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOGGER.warn("Invalid  [" + API_VERSION_HEADER + "] header");
		}
	}
}