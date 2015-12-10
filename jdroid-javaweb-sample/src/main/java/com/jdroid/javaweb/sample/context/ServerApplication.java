package com.jdroid.javaweb.sample.context;

import com.jdroid.java.domain.Entity;
import com.jdroid.javaweb.context.Application;

public class ServerApplication extends Application<Entity> {
	
	public static ServerApplication get() {
		return (ServerApplication)Application.get();
	}
	
	@Override
	public ServerAppContext getAppContext() {
		return (ServerAppContext)super.getAppContext();
	}
	
	public void setAppContext(ServerAppContext appContext) {
		super.setAppContext(appContext);
	}
}
