package com.jdroid.android.fabric;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

public class FabricAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FabricAppModule.class.getName();

	public static FabricAppModule get() {
		return (FabricAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}
	
	private List<Kit> fabricKits = Lists.newArrayList();
	private Boolean initialized = false;

	@Override
	public void onCreate() {
		super.onCreate();
		if (!fabricKits.isEmpty()) {
			Fabric.with(AbstractApplication.get(), fabricKits.toArray(new Kit[0]));
		}
		initialized = true;
	}
	
	public void addFabricKit(Kit fabricKit) {
		if (initialized) {
			throw new UnexpectedException("The fabric kits should be added before the FabricAppModule.onCreate execution");
		} else {
			this.fabricKits.add(fabricKit);
		}
	}
}