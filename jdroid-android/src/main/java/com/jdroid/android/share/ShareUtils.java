package com.jdroid.android.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.EncodingUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class ShareUtils {
	
	public static void shareTextContent(int shareTitle, int shareSubject, int shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		shareTextContent(activity.getString(shareTitle), activity.getString(shareSubject),
			activity.getString(shareText));
	}
	
	public static void shareTextContent(String shareTitle, String shareSubject, String shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		Intent intent = createShareTextContentIntent(shareSubject, shareText);
		activity.startActivity(Intent.createChooser(intent, shareTitle));
	}
	
	public static void shareHtmlContent(String shareTitle, String shareSubject, String shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		Intent intent = createShareHtmlContentIntent(shareSubject, shareText);
		activity.startActivity(Intent.createChooser(intent, shareTitle));
	}
	
	public static Intent createShareTextContentIntent(String shareSubject, String shareText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(MimeType.TEXT);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(Intent.EXTRA_TEXT, shareText);
		return intent;
	}
	
	public static Intent createShareHtmlContentIntent(String shareSubject, String shareText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(MimeType.HTML);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareText));
		return intent;
	}
	
	public static Intent createOpenMail(String mailto, String subject) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		String uriText = "mailto:" + mailto + "?subject=" + EncodingUtils.encodeURL(subject);
		Uri uri = Uri.parse(uriText);
		intent.setData(uri);
		return intent;
	}
}