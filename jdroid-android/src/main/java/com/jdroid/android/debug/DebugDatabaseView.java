package com.jdroid.android.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.jdroid.android.R;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.FileUtils;

public class DebugDatabaseView extends LinearLayout {
	
	public DebugDatabaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DebugDatabaseView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		
		LayoutInflater.from(context).inflate(R.layout.debug_database_view, this, true);
		
		findViewById(R.id.downloadDatabase).setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("resource")
			@Override
			public void onClick(View v) {
				
				File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				dir.mkdirs();
				
				File file = new File(dir, AndroidUtils.getApplicationName() + ".sqlite");
				try {
					FileUtils.copyStream(new FileInputStream(SQLiteHelper.getDatabaseFile(getContext())), file);
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					intent.setType(MimeType.OCTET_STREAM);
					getContext().startActivity(intent);
				} catch (FileNotFoundException e) {
					throw new UnexpectedException(e);
				}
				
			}
		});
	}
}
