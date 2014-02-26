/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.inappbilling;


/**
 * Exception thrown when something went wrong with in-app billing. An IabException has an associated
 * InAppBillingResponseCode (an error). To get the result that caused this exception to be thrown, call
 * {@link #getInAppBillingResponseCode()}.
 */
public class IabException extends Exception {
	
	private InAppBillingResponseCode inAppBillingResponseCode;
	
	public IabException(InAppBillingResponseCode inAppBillingResponseCode) {
		this.inAppBillingResponseCode = inAppBillingResponseCode;
	}
	
	/**
	 * @return the inAppBillingResponseCode
	 */
	public InAppBillingResponseCode getInAppBillingResponseCode() {
		return inAppBillingResponseCode;
	}
	
}