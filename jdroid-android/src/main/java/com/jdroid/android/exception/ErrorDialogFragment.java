package com.jdroid.android.exception;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;

/**
 * 
 * @author Maxi Rosson
 */
public class ErrorDialogFragment extends AbstractDialogFragment {
	
	private static final String TITLE_EXTRA = "titleExtra";
	private static final String MESSAGE_EXTRA = "messageExtra";
	private static final String SHOULD_GO_BACK_EXTRA = "shouldGoBackExtra";
	
	private String title;
	private String message;
	private Boolean shouldGoBack;
	
	public static void show(FragmentActivity activity, String title, String message, Boolean shouldGoBack) {
		
		Fragment currentErrorDialogFragment = activity.getSupportFragmentManager().findFragmentByTag(
			ErrorDialogFragment.class.getSimpleName());
		
		if (currentErrorDialogFragment == null) {
			ErrorDialogFragment fragment = new ErrorDialogFragment();
			
			Bundle bundle = new Bundle();
			bundle.putSerializable(TITLE_EXTRA, title);
			bundle.putSerializable(MESSAGE_EXTRA, message);
			bundle.putSerializable(SHOULD_GO_BACK_EXTRA, shouldGoBack);
			fragment.setArguments(bundle);
			
			fragment.show(activity.getSupportFragmentManager(), ErrorDialogFragment.class.getSimpleName());
		}
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		title = getArgument(TITLE_EXTRA);
		message = getArgument(MESSAGE_EXTRA);
		shouldGoBack = getArgument(SHOULD_GO_BACK_EXTRA);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(title);
		dialog.setCancelable(true);
		dialog.setMessage(message);
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goBackIfrequired();
			}
		});
		return dialog.create();
	}
	
	/**
	 * @see android.support.v4.app.DialogFragment#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		goBackIfrequired();
	}
	
	private void goBackIfrequired() {
		if (shouldGoBack) {
			getActivity().finish();
		}
	}
}
