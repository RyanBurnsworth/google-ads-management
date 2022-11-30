package com.addyai.utils.helpers;

public class ResourceNameCreator {
    public static String getAdResourceName(long customerId, String adGroupResName, String adResName) {
        String adGroupId = getAdGroupIdFromResName(customerId, adGroupResName);
        String adId = getAdIdFromResName(customerId, adResName);

        return "customers/" + customerId + "/adGroupAds/" + adGroupId + "~" + adId;
    }

    private static String getAdGroupIdFromResName(long customerId, String adGroupResName) {
        String prefix = "customers/" + customerId + "/adGroups/";
        return adGroupResName.replace(prefix, "");
    }

    private static String getAdIdFromResName(long customerId, String adResName) {
        String prefix = "customers/" + customerId + "/ads/";
        return adResName.replace(prefix, "");
    }
}
