/*******************************************************************************
 * Copyright (c) 2020. Tiktok Inc.
 *
 * This source code is licensed under the MIT license found in the LICENSE file in the root directory of this source tree.
 ******************************************************************************/

package com.tiktok.appevents;

import com.tiktok.util.JSON;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class TTPurchaseInfo {
    private final JSONObject purchase;
    private final JSONObject purchaseOfferDetails;
    private final JSONObject skuDetails;
    private String eventId;
    private boolean isAutoTrack;
    private boolean isSubs = false;

    public static class InvalidTTPurchaseInfoException extends Exception {

        public InvalidTTPurchaseInfoException(String str) {
            super(str);
        }
    }

    /**
     * @param purchase   for google billing v3, simply pass the purchase JSONObject.
     *                   for v4, you may try using new JSONObject(purchase.getOriginalJson()
     * @param skuDetails for google billing v3, simply pass the skuDetails JSONObject.
     *                   for v4, you may try using new JSONObject(skuDetails.getOriginalJson()
     * @throws InvalidTTPurchaseInfoException if either the purchase or the skuDetails object are not valid
     *                                        or the productId does not match
     */
    public TTPurchaseInfo(JSONObject purchase, JSONObject purchaseOfferDetails, JSONObject skuDetails) throws InvalidTTPurchaseInfoException {
        if (!isValidPurchase(purchase)) {
            throw new InvalidTTPurchaseInfoException("Not a valid purchase object");
        }
        if (!isValidPurchaseOfferDetails(purchaseOfferDetails)) {
            throw new InvalidTTPurchaseInfoException("Not a valid purchaseOfferDetails object");
        }
        if (!isValidSkuDetails(skuDetails)) {
            throw new InvalidTTPurchaseInfoException("Not a valid skuDetails Object");
        }

        String pid = JSON.getString(purchase, "productId");
        String pidSKU = JSON.getString(purchaseOfferDetails, "productId");
        if (pid != null && !pid.equals(pidSKU)) {
            throw new InvalidTTPurchaseInfoException("Product Id does not match");
        }
        this.purchase = purchase;
        this.purchaseOfferDetails = purchaseOfferDetails;
        this.skuDetails = skuDetails;
    }

    public TTPurchaseInfo(JSONObject purchase, JSONObject purchaseOfferDetails, JSONObject skuDetails, String eventId) throws InvalidTTPurchaseInfoException {
        this(purchase, purchaseOfferDetails, skuDetails);
        this.eventId = eventId;
    }

    public JSONObject getPurchase() {
        return purchase;
    }
    
    public JSONObject getPurchaseOfferDetails() {
        return purchaseOfferDetails;
    }

    public JSONObject getSkuDetails() {
        return skuDetails;
    }

    /**
     * {
     * "packageName":"com.example",
     * "acknowledged":false,
     * "orderId":"transactionId.android.test.purchased",
     * "productId":"android.test.purchased", // the same productId should also exist in the skuDetails
     * "developerPayload":"",
     * "purchaseTime":0,
     * "purchaseState":0,
     * "purchaseToken":"inapp:com.example:android.test.purchased"
     * }
     */
    private boolean isValidPurchase(JSONObject purchase) {
        return !purchase.isNull("orderId")
                && !purchase.isNull("productId");
    }
    
//    {
//        "productId": "games.extradimension.cafemerge.gp.iap.00799.bundle001",
//            "type": "inapp",
//            "title": "Bundle Offer (Starbrew Cafe: Mystical Merge)",
//            "name": "Bundle Offer",
//            "description": "Bundle Offer",
//            "localizedIn": [
//        "en-US"
//  ],
//        "skuDetailsToken": "AEuhp4JBft488lCym2eHWoX9UEScJvU0XW4QFe8VSNAiPOQxKvPnQcYY0pbg7pN7gjJXdTKJ1FNRsc4=",
//            "oneTimePurchaseOfferDetails": {},
//        "oneTimePurchaseOfferDetailsList": [
//        {
//            "priceAmountMicros": 10990000,
//                "priceCurrencyCode": "CAD",
//                "formattedPrice": "$10.99",
//                "offerIdToken": "ATdH1COn85RKhRpBaBJiqM5LfAe1sgPJPT6YNAW6GZqi02eT3dTREioHkx/nWI/n1uwwJbGrAGayYI288/xSxD15Bw==",
//                "purchaseOptionId": "legacy-base",
//                "offerTags": []
//        }
    private boolean isValidPurchaseOfferDetails(JSONObject purchaseOfferDetails) {
        return !purchaseOfferDetails.isNull("productId");
    }

//        {
//            "priceAmountMicros": 10990000,
//                "priceCurrencyCode": "CAD",
//                "formattedPrice": "$10.99",
//                "offerIdToken": "ATdH1COn85RKhRpBaBJiqM5LfAe1sgPJPT6YNAW6GZqi02eT3dTREioHkx/nWI/n1uwwJbGrAGayYI288/xSxD15Bw==",
//                "purchaseOptionId": "legacy-base",
//                "offerTags": []
//        }
    private boolean isValidSkuDetails(JSONObject skuDetails) {
        return !skuDetails.isNull("formattedPrice")
                && !skuDetails.isNull("priceCurrencyCode");
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isAutoTrack() {
        return isAutoTrack;
    }

    public void setAutoTrack(boolean autoTrack) {
        isAutoTrack = autoTrack;
    }

    public boolean isSubs() {
        return isSubs;
    }

    public void setSubs(boolean subs) {
        isSubs = subs;
    }
}
