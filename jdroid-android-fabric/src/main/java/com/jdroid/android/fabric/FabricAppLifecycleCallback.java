package com.jdroid.android.fabric;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

public class FabricAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FabricAppLifecycleCallback.class);
	
	private static List<Kit> fabricKits = Lists.newArrayList();
	private static Boolean initialized = false;
	
	@Override
	public void onCreate(Context context) {
		if (!fabricKits.isEmpty()) {
			Fabric.with(AbstractApplication.get(), fabricKits.toArray(new Kit[0]));
		}
		initialized = true;
	}
	
	public static void addFabricKit(Kit fabricKit) {
		if (initialized) {
			throw new UnexpectedException("The fabric kits should be added before the FabricAppModule.onCreate execution");
		} else {
			fabricKits.add(fabricKit);
		}
	}
	
	@NonNull
	@Override
	public Integer getInitOrder() {
		return -1;
	}
}
