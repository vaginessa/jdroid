package com.jdroid.javaweb.api;

import java.util.Map;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.context.Application;

public abstract class AbstractController {
	
	public String marshallSimple(Object object) {
		return marshall(object, MarshallerMode.SIMPLE);
	}
	
	public String marshall(Object object) {
		return marshall(object, MarshallerMode.COMPLETE);
	}
	
	public String marshall(Object object, MarshallerMode mode) {
		return marshall(object, mode, null);
	}
	
	public String marshall(Object object, Map<String, String> extras) {
		return marshall(object, MarshallerMode.COMPLETE, extras);
	}
	
	public String marshall(Object object, MarshallerMode mode, Map<String, String> extras) {
		return object != null ? MarshallerProvider.get().marshall(object, mode, extras).toString() : StringUtils.EMPTY;
	}
	
	public Long getUserId() {
		return Application.get().getSecurityContext().isAuthenticated() ? Application.get().getSecurityContext().getUser().getId()
				: null;
	}
}
