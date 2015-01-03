package com.jdroid.android.share;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.jdroid.android.R;
import com.jdroid.android.utils.ScreenUtils;

public class ShareView extends FrameLayout {
	
	public ShareView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ShareView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ShareView(Context context) {
		super(context);
	}
	
	public void init(Context context, final SharingItem sharingCommand) {
		LayoutInflater.from(context).inflate(R.layout.share_view, this, true);
		((ImageView)findViewById(R.id.shareAppIcon)).setImageDrawable(sharingCommand.getAppIcon());
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sharingCommand.share();
			}
		});
	}
	
	public static void initShareSection(final Activity activity, List<SharingItem> sharingItems,
			MoreSharingItem moreSharingItem) {
		ViewGroup shareSection = (ViewGroup)activity.findViewById(R.id.shareSection);
		ViewGroup shareItemsContainer = (ViewGroup)activity.findViewById(R.id.shareItemsContainer);
		
		int itemWidth = ScreenUtils.convertDimenToPixel(R.dimen.shareItemWidth);
		int itemsToDisplay = (ScreenUtils.getScreenWidthPx() / itemWidth) - 1;
		for (SharingItem each : sharingItems) {
			if (each.isEnabled() && (itemsToDisplay > 0)) {
				ShareView shareView = new ShareView(activity);
				shareView.init(activity, each);
				shareItemsContainer.addView(shareView);
				
				shareSection.setVisibility(View.VISIBLE);
				
				itemsToDisplay--;
			}
		}
		
		if (shareSection.getVisibility() == View.VISIBLE) {
			ShareView shareView = new ShareView(activity);
			shareView.init(activity, moreSharingItem);
			shareItemsContainer.addView(shareView);
		}
	}
}
