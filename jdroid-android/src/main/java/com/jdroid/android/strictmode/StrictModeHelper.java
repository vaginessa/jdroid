package com.jdroid.android.strictmode;

import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;
import com.jdroid.android.utils.AppUtils;

public class StrictModeHelper {
	
	public static void initStrictMode() {
		if (isStrictModeEnabled()) {
			innerInitStrictMode();
			if (Build.VERSION.SDK_INT >= 16) {
				//restore strict mode after onCreate() returns.
				new Handler().postAtFrontOfQueue(new Runnable() {
					@Override
					public void run() {
						innerInitStrictMode();
					}
				});
			}
		}
	}
	
	private static void innerInitStrictMode() {
		StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder();
		threadPolicyBuilder.detectAll();
		threadPolicyBuilder.penaltyLog();
		threadPolicyBuilder.penaltyFlashScreen();
		if (isStrictModePenaltyDeath()) {
			threadPolicyBuilder.penaltyDeath();
		}
		StrictMode.setThreadPolicy(threadPolicyBuilder.build());
		
		StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
		vmPolicyBuilder.detectActivityLeaks();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			vmPolicyBuilder.detectFileUriExposure();
		}
		vmPolicyBuilder.detectLeakedRegistrationObjects();
		vmPolicyBuilder.detectLeakedSqlLiteObjects();
		vmPolicyBuilder.penaltyLog();
		if (isStrictModePenaltyDeath()) {
			vmPolicyBuilder.penaltyDeath();
		}
		StrictMode.setVmPolicy(vmPolicyBuilder.build());
	}
	
	public static Boolean isStrictModeEnabled() {
		return FirebaseTestLab.isRunningInstrumentedTests() || !AppUtils.isReleaseBuildType() && BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_ENABLED", false);
	}
	
	public static Boolean isStrictModePenaltyDeath() {
		return FirebaseTestLab.isRunningInstrumentedTests() || BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_PENALTY_DEATH", false);
	}
}
