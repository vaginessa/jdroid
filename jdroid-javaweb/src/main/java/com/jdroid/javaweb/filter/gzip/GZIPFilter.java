package com.jdroid.javaweb.filter.gzip;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jdroid.java.http.HttpService;

public class GZIPFilter implements Filter {
	
	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 *      javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if (request instanceof HttpServletRequest) {
			String acceptEncoding = ((HttpServletRequest)request).getHeader(HttpService.ACCEPT_ENCODING_HEADER);
			if ((acceptEncoding != null) && (acceptEncoding.contains(HttpService.GZIP_ENCODING))) {
				GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper((HttpServletResponse)response);
				chain.doFilter(request, wrappedResponse);
				wrappedResponse.close();
				return;
			}
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filterConfig) {
		// Do nothing
	}
	
	@Override
	public void destroy() {
		// Do nothing
	}
}
