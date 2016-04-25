/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.google.inappbilling;

import java.util.List;
import java.util.Map;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

/**
 * Represents a block of information about in-app items. An Inventory is returned by such methods as
 * {@link InAppBillingClient#queryInventory}.
 */
public class Inventory {
	
	private Map<String, Product> productsMap = Maps.newLinkedHashMap();
	
	public List<Product> getProducts() {
		return Lists.newArrayList(productsMap.values());
	}
	
	public void addProduct(Product product) {
		productsMap.put(product.getId(), product);
	}
	
	public List<Product> getProductToConsume() {
		List<Product> productsToConsume = Lists.newArrayList();
		for (Product product : getProducts()) {
			if (product.isWaitingToConsume()) {
				productsToConsume.add(product);
			}
		}
		return productsToConsume;
	}
	
	public Product getProduct(String productId) {
		return productsMap.get(productId);
	}
	
}
