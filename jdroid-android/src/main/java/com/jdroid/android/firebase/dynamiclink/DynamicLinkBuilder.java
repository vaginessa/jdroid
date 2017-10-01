package com.jdroid.android.firebase.dynamiclink;

import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.utils.StringUtils;

public class DynamicLinkBuilder {

	// Your Firebase project's Dynamic Links domain. You can find this value in the Dynamic Links section of the Firebase console.
	private String domain;

	// The deep link your app will open. This link must be a valid URL, and use HTTP or HTTPS scheme.
	private String linkUrl;

	// Optional: The versionCode of the minimum version of your app that can open the link.
	// If the installed app is an older version, the user is taken to the Play Store to upgrade the app.
	private Integer minVersionCode;

	// Optional: The link to open when the app isn't installed. Specify this to do something other than install your app from the Play Store when the app isn't installed,
	// such as open the mobile web version of the content, or display a promotional page for your app.
	private String fallbackLink;

	// Optional: The link to open on Android. This link can use any scheme, and, if specified, takes priority over the linkUrl parameter on Android.
	private String customAppLocation;

	// Optional: Identify the advertiser, site, publication, etc. that is sending traffic to your property, for example: google, newsletter4, billboard.
	private String utmSource;

	// Optional: The advertising or marketing medium, for example: cpc, banner, email newsletter.
	private String utmMedium;

	// Optional: The individual campaign name, slogan, promo code, etc. for a product.
	private String utmCampaign;

	// Optional: Identify paid search keywords. If you're manually tagging paid keyword campaigns, you should also use utm_term to specify the keyword.
	private String utmTerm;

	// Optional: Used to differentiate similar content, or links within the same ad. For example, if you have two call-to-action links within the same
	// email message, you can use utm_content and set different values for each so you can tell which version is more effective.
	private String utmContent;

	public String build() {
		StringBuilder builder = new StringBuilder();
		builder.append(AbstractHttpService.HTTPS_PROTOCOL);
		builder.append("://");

		if (domain == null) {
			domain = FirebaseDynamicLinksAppContext.getDynamicLinksDomain();
		}
		if (domain == null) {
			throw new UnexpectedException("Missing domain when building Firebase dynamic link");
		}
		builder.append(domain);

		if (StringUtils.isEmpty(linkUrl)) {
			throw new UnexpectedException("Missing linkUrl when building Firebase dynamic link");
		}
		builder.append("/?link=");
		builder.append(linkUrl);

		builder.append("&apn=");
		builder.append(AppUtils.getReleaseApplicationId());

		if (minVersionCode != null) {
			builder.append("&amv=");
			builder.append(minVersionCode);
		}
		if (StringUtils.isNotEmpty(fallbackLink)) {
			builder.append("&afl=");
			builder.append(fallbackLink);
		}
		if (StringUtils.isNotEmpty(customAppLocation)) {
			builder.append("&al=");
			builder.append(customAppLocation);
		}
		if (StringUtils.isNotEmpty(utmSource)) {
			builder.append("&utm_source=");
			builder.append(utmSource);
		}
		if (StringUtils.isNotEmpty(utmMedium)) {
			builder.append("&utm_medium=");
			builder.append(utmMedium);
		}
		if (StringUtils.isNotEmpty(utmCampaign)) {
			builder.append("&utm_campaign=");
			builder.append(utmCampaign);
		}
		if (StringUtils.isNotEmpty(utmTerm)) {
			builder.append("&utm_term=");
			builder.append(utmTerm);
		}
		if (StringUtils.isNotEmpty(utmContent)) {
			builder.append("&utm_content=");
			builder.append(utmContent);
		}
		return builder.toString();
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public void setMinVersionCode(Integer minVersionCode) {
		this.minVersionCode = minVersionCode;
	}

	public void setFallbackLink(String fallbackLink) {
		this.fallbackLink = fallbackLink;
	}

	public void setCustomAppLocation(String customAppLocation) {
		this.customAppLocation = customAppLocation;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}

	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}

	public void setUtmTerm(String utmTerm) {
		this.utmTerm = utmTerm;
	}

	public void setUtmContent(String utmContent) {
		this.utmContent = utmContent;
	}
}
