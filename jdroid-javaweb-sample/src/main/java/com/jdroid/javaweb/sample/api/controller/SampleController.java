package com.jdroid.javaweb.sample.api.controller;

import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.api.AbstractController;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sample")
public class SampleController extends AbstractController {

	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractHttpService.class);
	
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = MimeType.JSON_UTF8)
	@ResponseBody
	public String get(@RequestParam String param1, @RequestHeader String header1, @RequestHeader(value="User-Agent") String userAgent) {
		LOGGER.info("GET. param1 = " + param1 + ". header1 = " + header1 + ". userAgent = " + userAgent);
		return marshall(new SampleResponse("response1"));
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public void post(@RequestParam String param1, @RequestHeader String header1, @RequestHeader(value="User-Agent") String userAgent) {
		LOGGER.info("POST. param1 = " + param1 + ". header1 = " + header1 + ". userAgent = " + userAgent);
	}

	@RequestMapping(value = "/put", method = RequestMethod.PUT)
	public void put(@RequestParam String param1, @RequestHeader String header1, @RequestHeader(value="User-Agent") String userAgent) {
		LOGGER.info("PUT. param1 = " + param1 + ". header1 = " + header1 + ". userAgent = " + userAgent);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam String param1, @RequestHeader String header1, @RequestHeader(value="User-Agent") String userAgent) {
		LOGGER.info("DELETE. param1 = " + param1 + ". header1 = " + header1 + ". userAgent = " + userAgent);
	}

	@RequestMapping(value = "/patch", method = RequestMethod.PATCH)
	public void patch(@RequestParam String param1, @RequestHeader String header1, @RequestHeader(value="User-Agent") String userAgent) {
		LOGGER.info("PATCH. param1 = " + param1 + ". header1 = " + header1 + ". userAgent = " + userAgent);
	}
}