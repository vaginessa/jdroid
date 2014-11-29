package com.googleplaypublisher;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Commit;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Insert;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Listings.Update;
import com.google.api.services.androidpublisher.model.Apk;
import com.google.api.services.androidpublisher.model.ApksListResponse;
import com.google.api.services.androidpublisher.model.AppEdit;
import com.google.api.services.androidpublisher.model.Listing;
import com.google.play.developerapi.samples.AndroidPublisherHelper;

public class GooglePlayPublisher {
	
	private static final Log log = LogFactory.getLog(GooglePlayPublisher.class);
	
	/**
	 * Lists all the apks for a given app.
	 * 
	 * @param appName Specify the name of your application. If the application name is {@code null} or blank, the
	 *            application will log a warning. Suggested format is "MyCompany-Application/1.0".
	 * @param packageName Specify the package name of the app.
	 * @param serviceAccountEmail Installed application: Leave this string empty and copy or edit
	 *            resources/client_secrets.json. Service accounts: Enter the service account email and add your key.p12
	 *            file to the resources directory.
	 */
	public static void listApks(String appName, String packageName, String serviceAccountEmail) {
		try {
			
			if (Strings.isNullOrEmpty(packageName)) {
				log.error("packageName cannot be null or empty!");
				return;
			}
			
			// Create the API service.
			AndroidPublisher service = AndroidPublisherHelper.init(appName, serviceAccountEmail);
			Edits edits = service.edits();
			
			// Create a new edit to make changes.
			Insert editRequest = edits.insert(packageName, null);
			AppEdit appEdit = editRequest.execute();
			
			// Get a list of apks.
			ApksListResponse apksResponse = edits.apks().list(packageName, appEdit.getId()).execute();
			
			// Print the apk info.
			for (Apk apk : apksResponse.getApks()) {
				log.info(String.format("Version: %d - Binary sha1: %s", apk.getVersionCode(), apk.getBinary().getSha1()));
			}
		} catch (IOException | GeneralSecurityException ex) {
			log.error("Exception was thrown while updating listing", ex);
		}
	}
	
	public static void updateListings(String appName, String packageName, String serviceAccountEmail,
			List<LocaleListing> localeListings) {
		try {
			
			if (Strings.isNullOrEmpty(packageName)) {
				log.error("packageName cannot be null or empty!");
				return;
			}
			
			// Create the API service.
			AndroidPublisher service = AndroidPublisherHelper.init(appName, serviceAccountEmail);
			Edits edits = service.edits();
			
			// Create an edit to update listing for application.
			Insert editRequest = edits.insert(packageName, null);
			AppEdit edit = editRequest.execute();
			String editId = edit.getId();
			log.info(String.format("Created edit with id: %s", editId));
			
			// Update listing for each locale of the application.
			for (LocaleListing each : localeListings) {
				Listing listing = new Listing();
				listing.setTitle(each.getTitle());
				listing.setFullDescription(each.getFullDescription());
				listing.setShortDescription(each.getShortDescription());
				
				Update updateUSListingsRequest = edits.listings().update(packageName, editId,
					each.getLocale().toString(), listing);
				Listing updatedListing = updateUSListingsRequest.execute();
				log.info(String.format("Created new " + each.getLocale().toString() + " app listing with title: %s",
					updatedListing.getTitle()));
			}
			
			// Commit changes for edit.
			Commit commitRequest = edits.commit(packageName, editId);
			AppEdit appEdit = commitRequest.execute();
			log.info(String.format("App edit with id %s has been comitted", appEdit.getId()));
			
		} catch (IOException | GeneralSecurityException ex) {
			log.error("Exception was thrown while updating listing", ex);
		}
	}
}
