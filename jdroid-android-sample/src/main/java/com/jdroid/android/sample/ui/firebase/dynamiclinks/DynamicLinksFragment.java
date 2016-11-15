package com.jdroid.android.sample.ui.firebase.dynamiclinks;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jdroid.android.firebase.dynamiclink.DynamicLinkBuilder;
import com.jdroid.android.firebase.dynamiclink.ShortDynamicLinkService;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.NumberUtils;
import com.jdroid.java.utils.StringUtils;

public class DynamicLinksFragment extends AbstractFragment {


	private TextView linkUrlTextView;
	private TextView minVersionCodeTextView;
	private TextView fallbackLinkTextView;
	private TextView customAppLocationTextView;
	private TextView utmSourceTextView;
	private TextView utmMediumTextView;
	private TextView utmCampaignTextView;
	private TextView utmTermTextView;
	private TextView utmContentTextView;
	private CheckBox unguessableCheckBox;

	private TextView shortLinkTextView;
	private TextView longLinkTextView;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.dynamic_links_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


		linkUrlTextView = findView(R.id.linkUrl);
		linkUrlTextView.setText("http://www.despegar.com/");

		minVersionCodeTextView = findView(R.id.minVersionCode);
		fallbackLinkTextView = findView(R.id.fallbackLink);
		customAppLocationTextView = findView(R.id.customAppLocation);
		utmSourceTextView = findView(R.id.utmSource);
		utmMediumTextView = findView(R.id.utmMedium);
		utmCampaignTextView = findView(R.id.utmCampaign);
		utmTermTextView = findView(R.id.utmTerm);
		utmContentTextView = findView(R.id.utmContent);
		unguessableCheckBox = findView(R.id.unguessable);

		shortLinkTextView = findView(R.id.shortLink);
		longLinkTextView = findView(R.id.longLink);


		findView(R.id.buildLink).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						final DynamicLinkBuilder builder = new DynamicLinkBuilder();
						builder.setLinkUrl(linkUrlTextView.getText().toString());
						builder.setMinVersionCode(NumberUtils.getLong(minVersionCodeTextView.getText().toString()));
						builder.setFallbackLink(fallbackLinkTextView.getText().toString());
						builder.setCustomAppLocation(customAppLocationTextView.getText().toString());
						builder.setUtmSource(utmSourceTextView.getText().toString());
						builder.setUtmMedium(utmMediumTextView.getText().toString());
						builder.setUtmCampaign(utmCampaignTextView.getText().toString());
						builder.setUtmTerm(utmTermTextView.getText().toString());
						builder.setUtmContent(utmContentTextView.getText().toString());

						executeOnUIThread(new Runnable() {
							@Override
							public void run() {
								longLinkTextView.setText(builder.build());
							}
						});
					}
				});
			}
		});

		findView(R.id.shortenLink).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String longLink = longLinkTextView.getText().toString();
				if (StringUtils.isNotEmpty(longLink)) {
					ExecutorUtils.execute(new Runnable() {
						@Override
						public void run() {
							final String shortLink = new ShortDynamicLinkService().getShortDynamicLink(longLink, unguessableCheckBox.isChecked());
							executeOnUIThread(new Runnable() {
								@Override
								public void run() {
									shortLinkTextView.setText(shortLink);
								}
							});
						}
					});
				}
			}
		});
	}
}
