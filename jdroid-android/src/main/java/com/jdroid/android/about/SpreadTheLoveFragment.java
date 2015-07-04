package com.jdroid.android.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.plus.PlusOneButton;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.facebook.FacebookHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.share.GooglePlusSharingItem;
import com.jdroid.android.share.HangoutsSharingItem;
import com.jdroid.android.share.MoreSharingItem;
import com.jdroid.android.share.ShareUtils;
import com.jdroid.android.share.ShareView;
import com.jdroid.android.share.SharingItem;
import com.jdroid.android.share.SmsSharingItem;
import com.jdroid.android.share.TelegramSharingItem;
import com.jdroid.android.share.TwitterSharingItem;
import com.jdroid.android.share.WhatsAppSharingItem;
import com.jdroid.android.social.googleplus.GooglePlusHelperFragment;
import com.jdroid.android.social.googleplus.GooglePlusOneButtonHelper;
import com.jdroid.android.social.twitter.TwitterConnector;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

public abstract class SpreadTheLoveFragment extends AbstractFragment {

	private static final int REQUEST_INVITE = IdGenerator.getIntId();

	private GooglePlusOneButtonHelper googlePlusOneButtonHelper;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.spread_the_love_fragment;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Boolean followUsVisible = false;

		View facebook = findView(R.id.facebook);
		if (getFacebookPageId() != null) {
			facebook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FacebookHelper.openPage(getFacebookPageId());
				}
			});
			followUsVisible = true;
		} else {
			facebook.setVisibility(View.GONE);
		}

		View googlePlus = findView(R.id.googlePlus);
		if (getGooglePlusCommunityId() != null) {
			googlePlus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					GooglePlusHelperFragment.openCommunity(getGooglePlusCommunityId());
				}
			});
			followUsVisible = true;
		} else {
			googlePlus.setVisibility(View.GONE);
		}

		View twitter = findView(R.id.twitter);
		if (getTwitterAccount() != null) {
			twitter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TwitterConnector.openProfile(getTwitterAccount());
				}
			});
			followUsVisible = true;
		} else {
			twitter.setVisibility(View.GONE);
		}

		findView(R.id.followUs).setVisibility(followUsVisible ? View.VISIBLE : View.GONE);

		List<SharingItem> sharingItems = Lists.newArrayList();
		sharingItems.add(new TwitterSharingItem() {
			
			@Override
			public String getShareKey() {
				return SpreadTheLoveFragment.this.getShareKey();
			}
			
			@Override
			public String getShareText() {
				return SpreadTheLoveFragment.this.getTwitterShareText();
			}
		});
		sharingItems.add(new GooglePlusSharingItem() {
			
			@Override
			public String getShareKey() {
				return SpreadTheLoveFragment.this.getShareKey();
			}
			
			@Override
			public String getShareText() {
				return SpreadTheLoveFragment.this.getGooglePlusShareText();
			}
		});
		sharingItems.add(new WhatsAppSharingItem() {
			
			@Override
			public String getShareKey() {
				return SpreadTheLoveFragment.this.getShareKey();
			}
			
			@Override
			public String getShareText() {
				return SpreadTheLoveFragment.this.getWhatsAppShareText();
			}
		});
		sharingItems.add(new TelegramSharingItem() {
			
			@Override
			public String getShareKey() {
				return SpreadTheLoveFragment.this.getShareKey();
			}
			
			@Override
			public String getShareText() {
				return SpreadTheLoveFragment.this.getTelegramShareText();
			}
		});
		sharingItems.add(new HangoutsSharingItem() {
			
			@Override
			public String getShareKey() {
				return SpreadTheLoveFragment.this.getShareKey();
			}
			
			@Override
			public String getShareText() {
				return SpreadTheLoveFragment.this.getHangoutsShareText();
			}
		});
		
		sharingItems.add(new SmsSharingItem() {
			
			@Override
			public String getShareKey() {
				return SpreadTheLoveFragment.this.getShareKey();
			}
			
			@Override
			public String getShareText() {
				return SpreadTheLoveFragment.this.getSmsShareText();
			}
		});
		
		Boolean initialized = ShareView.initShareSection(getActivity(), sharingItems, new MoreSharingItem() {
			
			@Override
			public void share() {
				ShareUtils.shareTextContent(SpreadTheLoveFragment.this.getShareKey(), getString(R.string.share),
						getShareTitle(), SpreadTheLoveFragment.this.getDefaultShareText());
			}
		});
		
		if (!initialized) {
			findView(R.id.shareSectionTitle).setVisibility(View.GONE);
		}

		View appInvite = findView(R.id.appInvite);
		if (displayAppInviteButton()) {
			appInvite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AppInviteInvitation.IntentBuilder intentBuilder = new AppInviteInvitation.IntentBuilder(getAppInviteTitle());
					intentBuilder.setMessage(getAppInviteMessage());
					intentBuilder.setDeepLink(Uri.parse(getAppInviteDeeplink()));
					Intent intent = intentBuilder.build();
					startActivityForResult(intent, REQUEST_INVITE);
				}
			});
		} else {
			appInvite.setVisibility(View.GONE);
		}
		
		if (displayGooglePlusOneButton()) {
			PlusOneButton plusOneButton = findView(R.id.plusOneButton);
			googlePlusOneButtonHelper = new GooglePlusOneButtonHelper(this, plusOneButton);
			if (!GooglePlayUtils.isGooglePlayServicesAvailable(getActivity())) {
				findView(R.id.plusOneSection).setVisibility(View.GONE);
			}
		} else {
			findView(R.id.plusOneSection).setVisibility(View.GONE);
		}
	}
	
	protected String getShareTitle() {
		return getString(R.string.appName);
	}
	
	protected String getFacebookPageId() {
		return AbstractApplication.get().getAppContext().getFacebookPageId();
	}
	
	protected String getGooglePlusCommunityId() {
		return AbstractApplication.get().getAppContext().getGooglePlusCommunityId();
	}
	
	protected String getTwitterAccount() {
		return AbstractApplication.get().getAppContext().getTwitterAccount();
	}

	protected Boolean displayAppInviteButton() {
		return false;
	}

	protected String getAppInviteTitle() {
		return getString(R.string.appInviteTitle, getString(R.string.appName));
	}
	protected String getAppInviteMessage() {
		return getString(R.string.appInviteTitle);
	}
	protected String getAppInviteDeeplink() {
		return AbstractApplication.get().getAppContext().getWebsite();
	}

	protected Boolean displayGooglePlusOneButton() {
		return true;
	}
	
	public String getShareKey() {
		return GooglePlayUtils.getGooglePlayLink();
	}
	
	protected abstract String getDefaultShareText();
	
	protected String getTwitterShareText() {
		return getDefaultShareText();
	}
	
	protected String getGooglePlusShareText() {
		return getDefaultShareText();
	}
	
	protected String getWhatsAppShareText() {
		return getDefaultShareText();
	}
	
	protected String getTelegramShareText() {
		return getDefaultShareText();
	}
	
	protected String getHangoutsShareText() {
		return getDefaultShareText();
	}
	
	protected String getSmsShareText() {
		return getDefaultShareText();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		if (googlePlusOneButtonHelper != null) {
			googlePlusOneButtonHelper.onResume();
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (googlePlusOneButtonHelper != null) {
			googlePlusOneButtonHelper.onActivityResult(requestCode, resultCode, data);
		}
	}
	
}
