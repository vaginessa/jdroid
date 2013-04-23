package com.jdroid.android.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.jdroid.android.common.AndroidTestRunner;
import com.jdroid.java.utils.StringUtils;

@RunWith(AndroidTestRunner.class)
public class StringUtilsTest {
	
	@Test
	public void wordWrapToTwoLinesTests() {
		// Word wrap with two words of the same size
		assertEquals("Two words of the same size word wrap fail!", "Aerolineas\nArgentinas",
			StringUtils.wordWrapToTwoLines("Aerolineas Argentinas", 10));
		// Word wrap with the first word longer than the second
		assertEquals("First word longer than the second word wrap fail!", "Southern\nWinds",
			StringUtils.wordWrapToTwoLines("Southern Winds", 10));
		// Word wrap with the first word shorter than the second
		assertEquals("First word shorter than the second word wrap fail!", "Virgin\nAirlines",
			StringUtils.wordWrapToTwoLines("Virgin Airlines", 10));
		// Without Word wrap, text length less than the minimum
		assertEquals("Text length less than the minimum word wrap fail!", "Swiss Air",
			StringUtils.wordWrapToTwoLines("Swiss Air", 10));
		// Without Word wrap, text length less than minimun and one word
		assertEquals("One word and text length less than minimun word wrap fail!", "Tam",
			StringUtils.wordWrapToTwoLines("Tam", 10));
		// Without Word wrap, text length greater than minimun and one word
		assertEquals("One word and text length greater than minimun word wrap fail!", "LargeNameAirline",
			StringUtils.wordWrapToTwoLines("LargeNameAirline", 10));
		// Word wrap with more than two words
		assertEquals("More than two words word wrap fail!", "Large Name\nAirline",
			StringUtils.wordWrapToTwoLines("Large Name Airline", 10));
	}
	
}
