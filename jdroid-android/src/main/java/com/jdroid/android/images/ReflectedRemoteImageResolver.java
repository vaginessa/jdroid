package com.jdroid.android.images;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.net.Uri;

/**
 * 
 * @author Maxi Rosson
 */
public class ReflectedRemoteImageResolver extends RemoteImageResolver {
	
	public static final String PREFFIX = "::reflected::";
	
	/**
	 * Gap between the image and its reflection.
	 */
	private float reflectionGap = 5;
	
	/** The image reflection ratio. */
	private float imageReflectionRatio = 0.35f;
	
	private static final ReflectedRemoteImageResolver INSTANCE = new ReflectedRemoteImageResolver();
	
	public static ReflectedRemoteImageResolver get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.images.RemoteImageResolver#canResolve(android.net.Uri)
	 */
	@Override
	public Boolean canResolve(Uri uri) {
		String uriString = uri.toString();
		return uriString.startsWith(PREFFIX) ? super.canResolve(getOriginalUri(uri)) : false;
	}
	
	private Uri getOriginalUri(Uri uri) {
		String uriString = uri.toString();
		return Uri.parse(uriString.replace(PREFFIX, ""));
	}
	
	public static Uri getReflectedUri(Uri uri) {
		String uriString = uri.toString();
		return Uri.parse(PREFFIX + uriString);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageResolver#resolve(android.net.Uri, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Bitmap resolve(Uri uri, Integer maxWidth, Integer maxHeight) {
		
		Bitmap originalBitmap = super.resolve(getOriginalUri(uri), maxWidth, maxHeight);
		Bitmap bitmapWithReflection = null;
		if (originalBitmap != null) {
			
			int originalWidth = originalBitmap.getWidth();
			int originalHeight = originalBitmap.getHeight();
			
			// Creates the reflection image
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);
			int reflectionHeight = (int)(originalHeight * imageReflectionRatio);
			Bitmap reflectionImage = Bitmap.createBitmap(originalBitmap, 0, reflectionHeight, originalWidth,
				originalHeight - reflectionHeight, matrix, false);
			
			// Creates the image with the reflection
			bitmapWithReflection = Bitmap.createBitmap(originalWidth, originalHeight + reflectionHeight,
				Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmapWithReflection);
			canvas.drawBitmap(originalBitmap, 0, 0, null);
			canvas.drawBitmap(reflectionImage, 0, originalHeight + reflectionGap, null);
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0, originalBitmap.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			paint.setShader(shader);
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			canvas.drawRect(0, originalHeight, originalWidth, bitmapWithReflection.getHeight() + reflectionGap, paint);
		}
		
		return bitmapWithReflection;
	}
	
	public float getReflectionGap() {
		return reflectionGap;
	}
	
	public float getImageReflectionRatio() {
		return imageReflectionRatio;
	}
	
}
