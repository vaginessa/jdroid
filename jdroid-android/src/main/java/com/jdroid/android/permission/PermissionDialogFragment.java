package com.jdroid.android.permission;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.jdroid.android.dialog.AlertDialogFragment;

public class PermissionDialogFragment extends AlertDialogFragment {

	public static final String PERMISSION_EXTRA = "permissionExtra";
	public static final String PERMISSION_REQUEST_CODE_EXTRA = "permissionRequestCodeExtra";

	private String permission;
	private int permissionRequestCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		permission = getArgument(PERMISSION_EXTRA);
		permissionRequestCode = getArgument(PERMISSION_REQUEST_CODE_EXTRA);
	}

	@Override
	protected void onPositiveClick() {
		ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, permissionRequestCode);
	}
}
