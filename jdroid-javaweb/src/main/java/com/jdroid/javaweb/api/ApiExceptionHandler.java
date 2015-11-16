package com.jdroid.javaweb.api;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.jdroid.javaweb.exception.InvalidArgumentException;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;

import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.style.StylerUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renders a response with a RESTful Error representation based on the error format discussed in <a
 * href="http://www.stormpath.com/blog/spring-mvc-rest-exception-handling-best-practices-part-1"> Spring MVC Rest
 * Exception Handling Best Practices.</a>
 * <p/>
 * At a high-level, this implementation functions as follows:
 * 
 * <ol>
 * <li>The HTTP Response's Status Code will be set to the {@code ApiError}'s
 * {@link com.jdroid.javaweb.api.ApiError#getStatus() status} value.</li>
 * <li>The {@code ApiError} instance is presented to a configured {@link HttpMessageConverter} to allow transforming the
 * {@code ApiError} instance into an object potentially more suitable for rendering as the HTTP response body.</li>
 * <li>The {@link #setMessageConverters(List) HttpMessageConverters} are consulted (in iteration order) with this object
 * result for rendering. The first {@code HttpMessageConverter} instance that
 * {@link HttpMessageConverter#canWrite(Class, org.springframework.http.MediaType) canWrite} the object based on the
 * request's supported {@code MediaType}s will be used to render this result object as the HTTP response body.</li>
 * <li>If no {@code HttpMessageConverter}s {@code canWrite} the result object, nothing is done, and this handler returns
 * {@code null} to indicate other ExceptionResolvers potentially further in the resolution chain should handle the
 * exception instead.</li>
 * </ol>
 * 
 * <h3>Defaults</h3>
 * This implementation has the following property defaults:
 * <table>
 * <tr>
 * <th>Property</th>
 * <th>Instance</th>
 * <th>Notes</th>
 * </tr>
 * <tr>
 * <td>messageConverters</td>
 * <td>multiple instances</td>
 * <td>Default collection are those automatically enabled by Spring as <a href=
 * "http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-config-enable"
 * >defined here</a> (specifically item #5)</td>
 * </tr>
 * </table>
 * 
 * <h2>JSON Rendering</h2>
 * This implementation comes pre-configured with Spring's {@link MappingJackson2HttpMessageConverter}
 * 
 * @see HttpMessageConverter
 * @see org.springframework.http.converter.json.MappingJacksonHttpMessageConverter MappingJacksonHttpMessageConverter
 * 
 */
public class ApiExceptionHandler extends AbstractHandlerExceptionResolver {
	
	public static final String STATUS_CODE_HEADER = "status-code";
	public static final String OK_STATUS_CODE_HEADER_VALUE = "200";
	
	private static final Logger LOGGER = LoggerUtils.getLogger(ApiExceptionHandler.class);
	
	private List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
	
	public ApiExceptionHandler() {
		setOrder(0);
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.getObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
		mappingJackson2HttpMessageConverter.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
		messageConverters.add(mappingJackson2HttpMessageConverter);
	}
	
	/**
	 * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
	 * represents a specific error page if appropriate.
	 * <p/>
	 * May be overridden in subclasses, in order to apply specific exception checks. Note that this template method will
	 * be invoked <i>after</i> checking whether this resolved applies ("mappedHandlers" etc), so an implementation may
	 * simply proceed with its actual exception handling.
	 * 
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception (for
	 *            example, if multipart resolution failed)
	 * @param exception the exception that got thrown during handler execution
	 * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
	 * 
	 * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		
		ModelAndView modelAndView = null;
		ServletWebRequest webRequest = null;
		ApiError error = null;
		try {
			webRequest = new ServletWebRequest(request, response);
			if (exception instanceof BadRequestException) {
				error = handleException((BadRequestException)exception);
			} else if (exception instanceof UnexpectedException) {
				error = handleException(exception);
			} else if (exception instanceof ErrorCodeException) {
				error = handleException((ErrorCodeException)exception);
			} else if (exception instanceof InvalidArgumentException) {
				error = handleException((InvalidArgumentException)exception);
			} else if (exception instanceof InvalidAuthenticationException) {
				error = handleException((InvalidAuthenticationException)exception);
			} else if (exception instanceof TypeMismatchException) {
				error = handleException((TypeMismatchException)exception);
			} else if (exception instanceof HttpMessageNotReadableException) {
				error = handleException((HttpMessageNotReadableException)exception);
			} else if (exception instanceof MissingServletRequestParameterException) {
				error = handleException((MissingServletRequestParameterException)exception);
			} else if (exception instanceof NoSuchRequestHandlingMethodException) {
				error = handleException((NoSuchRequestHandlingMethodException)exception);
			} else if (exception instanceof HttpRequestMethodNotSupportedException) {
				error = handleException((HttpRequestMethodNotSupportedException)exception);
			} else {
				error = handleException(exception);
			}

			setHeaderStatus(webRequest, error);
			modelAndView = getModelAndView(webRequest, error);
			
		} catch (Exception invocationEx) {
			LOGGER.error("Error resolving ModelAndView for Exception [" + exception + "]", invocationEx);
			error = null;
			setHeaderStatus(webRequest, error);
			modelAndView = new ModelAndView();
		}
		return modelAndView;
	}
	
	protected void setHeaderStatus(ServletWebRequest webRequest, ApiError error) {
		if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
			int status = error != null ? error.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR.value();
			webRequest.getResponse().setStatus(status);
			LOGGER.info("Http Response Status: " + status);
			webRequest.getResponse().setHeader(STATUS_CODE_HEADER, error != null ? error.getCode() : "500");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	protected ModelAndView getModelAndView(ServletWebRequest webRequest, ApiError error)
			throws HttpMessageNotWritableException, IOException {
		
		if (error != null) {
			HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
			
			List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
			if (acceptedMediaTypes.isEmpty()) {
				acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
			}
			
			MediaType.sortByQualityValue(acceptedMediaTypes);
			
			HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
			
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter messageConverter : messageConverters) {
					if (messageConverter.canWrite(error.getClass(), acceptedMediaType)) {
						messageConverter.write(error, acceptedMediaType, outputMessage);
						// return empty model and view to short circuit the iteration and to let
						// Spring know that we've rendered the view ourselves:
						return new ModelAndView();
					}
				}
			}
			
			if (logger.isWarnEnabled()) {
				logger.warn("Could not find HttpMessageConverter that supports return type [" + error.getClass()
						+ "] and " + acceptedMediaTypes);
			}
			return new ModelAndView();
			
		} else {
			return new ModelAndView();
		}
	}
	
	protected ApiError handleException(ErrorCodeException errorCodeException) {
		LOGGER.info("Server Status code: " + errorCodeException.getErrorCode().getStatusCode());
		return new ApiError(HttpStatus.OK, errorCodeException.getErrorCode().getStatusCode());
	}
	
	protected ApiError handleException(BadRequestException badRequestException) {
		LOGGER.warn("No mapping found for HTTP request with [URI '" + badRequestException.getUri() + "', method '"
				+ badRequestException.getRequestMethod() + "', parameters "
				+ StylerUtils.style(badRequestException.getUriParameters()) + "] in DispatcherServlet with name '"
				+ badRequestException.getServletName() + "'");
		return new ApiError(HttpStatus.BAD_REQUEST, badRequestException.getErrorCode().getStatusCode(),
				badRequestException.getMessage());
	}
	
	protected ApiError handleException(TypeMismatchException typeMismatchException) {
		LOGGER.warn(typeMismatchException.getMessage());
		return new ApiError(HttpStatus.BAD_REQUEST, CommonErrorCode.BAD_REQUEST.getStatusCode(),
				typeMismatchException.getMessage());
	}
	
	protected ApiError handleException(HttpMessageNotReadableException httpMessageNotReadableException) {
		return handleBadRequest(httpMessageNotReadableException);
	}
	
	protected ApiError handleException(MissingServletRequestParameterException missingServletRequestParameterException) {
		return handleBadRequest(missingServletRequestParameterException);
	}
	
	protected ApiError handleException(NoSuchRequestHandlingMethodException noSuchRequestHandlingMethodException) {
		return handleBadRequest(noSuchRequestHandlingMethodException);
	}
	
	protected ApiError handleException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
		return handleBadRequest(httpRequestMethodNotSupportedException);
	}
	
	private ApiError handleBadRequest(Exception exception) {
		LOGGER.warn(exception.getMessage());
		return new ApiError(HttpStatus.BAD_REQUEST, CommonErrorCode.BAD_REQUEST.getStatusCode(), exception.getMessage());
	}
	
	protected ApiError handleException(InvalidArgumentException invalidArgumentException) {
		return handleBadRequest(invalidArgumentException);
	}
	
	protected ApiError handleException(InvalidAuthenticationException invalidAuthentificationException) {
		LOGGER.warn("User NOT authenticated.");
		return new ApiError(HttpStatus.UNAUTHORIZED, invalidAuthentificationException.getErrorCode().getStatusCode());
	}
	
	protected ApiError handleException(Exception exception) {
		LOGGER.error("Unexpected error", exception);
		return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, null, exception.getMessage());
	}
	
	public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		this.messageConverters = messageConverters;
	}
}
