package com.jdroid.android.uri;

import android.net.Uri;

import com.jdroid.android.AbstractUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class UriUtilsTest extends AbstractUnitTest {

	@Test
	public void randomParamTest() {
		verifyRandomParam("http://jdroidframework.com", "http://jdroidframework.com/?rnd=");
		verifyRandomParam("http://jdroidframework.com/", "http://jdroidframework.com/?rnd=");
		verifyRandomParam("http://jdroidframework.com/?a=1", "http://jdroidframework.com/?a=1&rnd=");
		verifyRandomParam("http://jdroidframework.com/a", "http://jdroidframework.com/a?rnd=");
		verifyRandomParam("http://jdroidframework.com/a/b", "http://jdroidframework.com/a/b?rnd=");
		verifyRandomParam("http://jdroidframework.com/a?b=1", "http://jdroidframework.com/a?b=1&rnd=");
		verifyRandomParam("http://jdroidframework.com/a/b?c=1", "http://jdroidframework.com/a/b?c=1&rnd=");
	}

	private void verifyRandomParam(String originalUrl, String expectedUrl) {
		String newUrl = UriUtils.addRandomParam(Uri.parse(originalUrl)).toString();
		assertTrue("Transformed url: " + newUrl, newUrl.startsWith(expectedUrl));
	}
}
