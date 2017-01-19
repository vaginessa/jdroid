package com.jdroid.android.firebase.dynamiclink;

import android.support.annotation.WorkerThread;

import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.java.http.api.AbstractApiService;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;

import java.util.Map;

public class ShortDynamicLinkService extends AbstractApiService {

	static {
		MarshallerProvider.get().addMarshaller(DynamicLink.class, new DynamicLinkMarshaller());
	}

	@Override
	protected Server getServer() {
		return new DefaultServer("firebasedynamiclinks.googleapis.com/v1");
	}

	@Override
	protected AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return null;
	}

	@Override
	protected Boolean isHttpMockEnabled() {
		return false;
	}

	@WorkerThread
	public String getShortDynamicLink(String longDynamicLink) {
		return getShortDynamicLink(longDynamicLink, false);
	}

	@WorkerThread
	public String getShortDynamicLink(String longDynamicLink, Boolean unguessable) {
		BodyEnclosingHttpService service = newPostService("shortLinks");
		service.setSsl(true);
		service.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON);
		service.addQueryParameter("key", FirebaseAppModule.get().getFirebaseAppContext().getWebApiKey());

		DynamicLink dynamicLink = new DynamicLink();
		dynamicLink.longDynamicLink = longDynamicLink;
		dynamicLink.suffix.put("option",  unguessable ? "UNGUESSABLE" : "SHORT");

		marshall(service, dynamicLink);

		return service.execute(new DynamicLinkParser());
	}

	private class DynamicLink {
		String longDynamicLink;
		Map<String, String> suffix = Maps.newHashMap();
	}

	private static class DynamicLinkMarshaller implements Marshaller<DynamicLink, JsonMap> {

		@Override
		public JsonMap marshall(DynamicLink dynamicLink, MarshallerMode mode, Map<String, String> extras) {
			JsonMap jsonMap = new JsonMap(mode, extras);
			jsonMap.put("longDynamicLink", dynamicLink.longDynamicLink);
			jsonMap.put("suffix", dynamicLink.suffix);
			return jsonMap;
		}
	}
}
