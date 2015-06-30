package com.jdroid.java.http.mock;

public abstract class XmlMockHttpService extends AbstractMockHttpService {
	
	private final static String MOCKS_BASE_PATH = "mocks/xml/";
	private final static String MOCKS_EXTENSION = ".xml";
	
	public XmlMockHttpService(Object... urlSegments) {
		super(urlSegments);
	}
	
	/**
	 * @see AbstractMockHttpService#getMocksBasePath()
	 */
	@Override
	protected String getMocksBasePath() {
		return MOCKS_BASE_PATH;
	}
	
	/**
	 * @see AbstractMockHttpService#getMocksExtension()
	 */
	@Override
	protected String getMocksExtension() {
		return MOCKS_EXTENSION;
	}
	
}
