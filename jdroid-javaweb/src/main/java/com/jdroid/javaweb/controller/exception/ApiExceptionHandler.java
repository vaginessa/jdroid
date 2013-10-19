package com.jdroid.javaweb.controller.exception;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.core.style.StylerUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;

/**
 * Renders a response with a RESTful Error representation based on the error format discussed in <a
 * href="http://www.stormpath.com/blog/spring-mvc-rest-exception-handling-best-practices-part-1"> Spring MVC Rest
 * Exception Handling Best Practices.</a>
 * <p/>
 * At a high-level, this implementation functions as follows:
 * 
 * <ol>
 * <li>Upon encountering an Exception, the configured {@link RestErrorResolver} is consulted to resolve the exception
 * into a {@link RestError} instance.</li>
 * <li>The HTTP Response's Status Code will be set to the {@code RestError}'s
 * {@link com.jdroid.javaweb.controller.exception.RestError#getStatus() status} value.</li>
 * <li>The {@code RestError} instance is presented to a configured {@link RestErrorConverter} to allow transforming the
 * {@code RestError} instance into an object potentially more suitable for rendering as the HTTP response body.</li>
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
 * <td>errorResolver</td>
 * <td>{@link DefaultRestErrorResolver DefaultRestErrorResolver}</td>
 * <td>Converts Exceptions to {@link RestError} instances. Should be suitable for most needs.</td>
 * </tr>
 * <tr>
 * <td>errorConverter</td>
 * <td>{@link MapRestErrorConverter}</td>
 * <td>Converts {@link RestError} instances to {@code java.util.Map} instances to be used as the response body. Maps can
 * then be trivially rendered as JSON by a (configured) {@link HttpMessageConverter HttpMessageConverter}. If you want
 * the raw {@code RestError} instance to be presented to the {@code HttpMessageConverter} instead, set this property to
 * {@code null}.</td>
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
 * @see DefaultRestErrorResolver
 * @see MapRestErrorConverter
 * @see HttpMessageConverter
 * @see org.springframework.http.converter.json.MappingJacksonHttpMessageConverter MappingJacksonHttpMessageConverter
 * 
 * @author Les Hazlewood
 */
public class ApiExceptionHandler extends AbstractHandlerExceptionResolver {
	
	public static final String STATUS_CODE_HEADER = "status-code";
	public static final String OK_STATUS_CODE_HEADER_VALUE = "200";
	
	private static final Logger LOGGER = LoggerUtils.getLogger(ApiExceptionHandler.class);
	
	private List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
	private RestErrorResolver errorResolver;
	private RestErrorConverter<?> errorConverter;
	
	public ApiExceptionHandler() {
		errorResolver = new DefaultRestErrorResolver();
		errorConverter = new MapRestErrorConverter();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
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
	 * @param ex the exception that got thrown during handler execution
	 * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
	 * 
	 * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		ModelAndView modelAndView = null;
		ServletWebRequest webRequest = null;
		RestError error = null;
		try {
			
			logException(ex);
			
			webRequest = new ServletWebRequest(request, response);
			
			error = errorResolver.resolveError(webRequest, handler, ex);
			if (error == null) {
				return new ModelAndView();
			}
			
			modelAndView = getModelAndView(webRequest, error);
			
		} catch (Exception invocationEx) {
			LOGGER.error("Error resolving ModelAndView for Exception [" + ex + "]", invocationEx);
			error = null;
			modelAndView = new ModelAndView();
		} finally {
			setHeaderStatus(webRequest, error);
		}
		return modelAndView;
	}
	
	protected void setHeaderStatus(ServletWebRequest webRequest, RestError error) {
		if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
			webRequest.getResponse().setStatus(
				error != null ? error.getStatus().value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
			webRequest.getResponse().setHeader(STATUS_CODE_HEADER, error != null ? error.getCode().toString() : "500");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ModelAndView getModelAndView(ServletWebRequest webRequest, RestError error)
			throws HttpMessageNotWritableException, IOException {
		
		Object body = errorConverter.convert(error);
		
		HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
		
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}
		
		MediaType.sortByQualityValue(acceptedMediaTypes);
		
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
		
		Class<?> bodyType = body.getClass();
		
		for (MediaType acceptedMediaType : acceptedMediaTypes) {
			for (HttpMessageConverter messageConverter : messageConverters) {
				if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
					messageConverter.write(body, acceptedMediaType, outputMessage);
					// return empty model and view to short circuit the iteration and to let
					// Spring know that we've rendered the view ourselves:
					return new ModelAndView();
				}
			}
		}
		
		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type [" + bodyType + "] and "
					+ acceptedMediaTypes);
		}
		return null;
	}
	
	protected void logException(Exception exception) {
		
		if (exception instanceof BusinessException) {
			handleException((BusinessException)exception);
		} else if (exception instanceof BadRequestException) {
			handleException((BadRequestException)exception);
		} else if (exception instanceof InvalidAuthenticationException) {
			handleException((InvalidAuthenticationException)exception);
		} else {
			handleException(exception);
		}
	}
	
	protected void handleException(BusinessException businessException) {
		LOGGER.info("Server Status code: " + businessException.getErrorCode().getStatusCode());
	}
	
	protected void handleException(BadRequestException badRequestException) {
		LOGGER.warn("No mapping found for HTTP request with [URI '" + badRequestException.getUri() + "', method '"
				+ badRequestException.getRequestMethod() + "', parameters "
				+ StylerUtils.style(badRequestException.getUriParameters()) + "] in DispatcherServlet with name '"
				+ badRequestException.getServletName() + "'");
	}
	
	protected void handleException(InvalidAuthenticationException invalidAuthentificationException) {
		LOGGER.warn("User NOT authenticated.");
	}
	
	protected void handleException(Exception exception) {
		LOGGER.error("Unexpected error", exception);
	}
	
	public void setErrorResolver(RestErrorResolver errorResolver) {
		this.errorResolver = errorResolver;
	}
	
	public void setErrorConverter(RestErrorConverter<?> errorConverter) {
		this.errorConverter = errorConverter;
	}
	
	public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		this.messageConverters = messageConverters;
	}
}
