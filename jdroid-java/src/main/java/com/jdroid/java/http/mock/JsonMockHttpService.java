package com.jdroid.java.http.mock;

public abstract class JsonMockHttpService extends AbstractMockHttpService {
	
	private final static String MOCKS_BASE_PATH = "mocks/json/";
	private final static String MOCKS_EXTENSION = ".json";
	
	public JsonMockHttpService(Object... urlSegments) {
		super(urlSegments);
	}
	
	@Override
	protected String getMocksBasePath() {
		return MOCKS_BASE_PATH;
	}
	
	@Override
	protected String getMocksExtension() {
		return MOCKS_EXTENSION;
	}
	
}
