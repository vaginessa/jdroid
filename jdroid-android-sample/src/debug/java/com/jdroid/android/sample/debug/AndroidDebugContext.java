package com.jdroid.android.sample.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.sample.api.ApiServer;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;

import java.util.List;
import java.util.Map;

public class AndroidDebugContext extends DebugContext {

	public AndroidDebugContext() {
		addCustomDebugInfoProperty(new Pair<String, Object>("Sample Key", "Sample Value"));
	}

	@Override
	public Map<Class<? extends Server>, List<? extends Server>> getServersMap() {
		Map<Class<? extends Server>, List<? extends Server>> serversMap = Maps.newHashMap();
		serversMap.put(ApiServer.class, Lists.newArrayList(ApiServer.values()));
		return serversMap;
	}

	@Override
	public List<String> getUrlsToTest() {
		List<String> urls = Lists.newArrayList();
		urls.add("http://jdroidframework.com");
		urls.add("http://jdroidframework.com/");
		urls.add("http://jdroidframework.com/uri");
		urls.add("http://jdroidframework.com/uri/singletop?a=1");
		urls.add("http://jdroidframework.com/uri/singletop?a=2");
		urls.add("http://jdroidframework.com/uri/singletop?a=3");
		urls.add("http://jdroidframework.com/uri/noflags?a=1");
		urls.add("http://jdroidframework.com/uri/noflags?a=2");
		urls.add("http://jdroidframework.com/uri/noflags?a=3");
		urls.add("http://jdroidframework.com/uri/noflags");
		urls.add("http://jdroidframework.com/uri/invalid");
		return urls;
	}
}
