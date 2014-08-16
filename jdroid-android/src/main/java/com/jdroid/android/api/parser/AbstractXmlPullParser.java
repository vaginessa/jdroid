package com.jdroid.android.api.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.NumberUtils;

public abstract class AbstractXmlPullParser implements Parser {
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.lang.String)
	 */
	@Override
	public Object parse(String input) {
		return parse(new ByteArrayInputStream(input.getBytes()));
	}
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.io.InputStream)
	 */
	@Override
	public Object parse(InputStream inputStream) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readFeed(parser);
		} catch (XmlPullParserException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		} finally {
			FileUtils.safeClose(inputStream);
		}
	}
	
	protected abstract Object readFeed(XmlPullParser parser) throws XmlPullParserException, IOException;
	
	protected String readStringAttribute(XmlPullParser parser, String attributeName) {
		return parser.getAttributeValue(null, attributeName);
	}
	
	protected Long readLongValue(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
		return NumberUtils.getLong(readStringValue(parser, name));
	}
	
	protected String readStringValue(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, null, name);
		String value = "";
		if (parser.next() == XmlPullParser.TEXT) {
			value = parser.getText();
			parser.nextTag();
		}
		parser.require(XmlPullParser.END_TAG, null, name);
		return value;
	}
	
	protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
	
}
