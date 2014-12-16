package com.jdroid.android.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.utils.AndroidUtils;

public class ButtonBarView extends FrameLayout {
	
	private View negativeButton;
	private TextView negativeText;
	private View positiveButton;
	private TextView positiveText;
	
	public ButtonBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public ButtonBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ButtonBarView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		
		LayoutInflater.from(context).inflate(R.layout.button_bar_view, this, true);
		
		negativeButton = findViewById(R.id.negativeButton);
		negativeText = (TextView)findViewById(R.id.negativeButtonText);
		setNegativeOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AbstractApplication.get().getCurrentActivity().finish();
			}
		});
		
		positiveButton = findViewById(R.id.positiveButton);
		positiveText = (TextView)findViewById(R.id.positiveButtonText);
	}
	
	public void setNegativeTextId(int negativeTextId) {
		negativeText.setText(negativeTextId);
	}
	
	public void setNegativeDrawableId(int negativeDrawableId) {
		negativeText.setCompoundDrawablesWithIntrinsicBounds(negativeDrawableId, 0, 0, 0);
	}
	
	public void setPositiveTextId(int positiveTextId) {
		positiveText.setText(positiveTextId);
	}
	
	public void setPositiveDrawableId(Integer positiveDrawableId) {
		positiveText.setCompoundDrawablesWithIntrinsicBounds(positiveDrawableId, 0, 0, 0);
	}
	
	public void setNegativeOnClickListener(OnClickListener negativeOnClickListener) {
		negativeButton.setOnClickListener(negativeOnClickListener);
	}
	
	public void setPositiveOnClickListener(OnClickListener positiveOnClickListener) {
		positiveButton.setOnClickListener(positiveOnClickListener);
	}
	
	public View getNegativeButton() {
		return negativeButton;
	}
	
	public TextView getNegativeText() {
		return negativeText;
	}
	
	public View getPositiveButton() {
		return positiveButton;
	}
	
	public TextView getPositiveText() {
		return positiveText;
	}
	
	/**
	 * Put this {@link ButtonBarView} on the {@link ActionBar}. To make it work properly on KitKat you also need to add
	 * a theme on your activity with the following line: <item name="android:homeAsUpIndicator">@null</item>
	 * 
	 * @param activity The {@link Activity}
	 */
	public void addToActionBar(AbstractFragmentActivity activity) {
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(!AndroidUtils.isPreKitkat());
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(this);
	}
}
