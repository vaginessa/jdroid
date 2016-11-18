package com.jdroid.android.google.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapUtils {

	/**
	 * Convert a {@link Drawable} to a {@link BitmapDescriptor}, for use as a marker icon.
	 */
	public static BitmapDescriptor vectorToBitmap(Context context, @DrawableRes int drawableRes, @ColorRes int colorRes) {
		Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), drawableRes, null);
		Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
				vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		DrawableCompat.setTint(vectorDrawable, context.getResources().getColor(colorRes));
		vectorDrawable.draw(canvas);
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}
}
