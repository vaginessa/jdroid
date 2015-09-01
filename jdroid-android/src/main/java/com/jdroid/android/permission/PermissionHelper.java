package com.jdroid.android.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.jdroid.android.R;
import com.jdroid.android.dialog.AlertDialogFragment;

public class PermissionHelper {

	public static Boolean checkPermission(FragmentActivity fragmentActivity, @StringRes int titleResId, @StringRes int messageResId, String permission, int permissionRequestCode) {
		return checkPermission(fragmentActivity, fragmentActivity.getString(titleResId), fragmentActivity.getString(messageResId), permission, permissionRequestCode);
	}
	public static Boolean checkPermission(FragmentActivity fragmentActivity, String title, String message, String permission, int permissionRequestCode) {
		if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
				PermissionDialogFragment fragment = new PermissionDialogFragment();
				fragment.addParameter(PermissionDialogFragment.PERMISSION_EXTRA, permission);
				fragment.addParameter(PermissionDialogFragment.PERMISSION_REQUEST_CODE_EXTRA, permissionRequestCode);

				String screenViewName = PermissionDialogFragment.class.getSimpleName() + "-" + permission;

				AlertDialogFragment.show(fragmentActivity, fragment, null, title, message, fragmentActivity.getString(R.string.cancel),
						null, fragmentActivity.getString(R.string.ok), true, screenViewName, null);
			} else {
				ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, permissionRequestCode);
			}
			return false;
		} else {
			return true;
		}
	}

	public static Boolean checkPermission(FragmentActivity fragmentActivity, String permission, int permissionRequestCode) {
		if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, permissionRequestCode);
			return false;
		} else {
			return true;
		}
	}

	public static Boolean shouldShowRequestPermissionRationale(FragmentActivity fragmentActivity, String permission) {
		return ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission);
	}

	public static void requestPermission(FragmentActivity fragmentActivity, String permission, int permissionRequestCode) {
		ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, permissionRequestCode);
	}

	public static Boolean verifyPermission(Context context, String permission) {
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}
}
