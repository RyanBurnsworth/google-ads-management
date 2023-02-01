package com.addyai.utils.helpers;

public class ResourceNameHelper {
    public static String getAdResourceName(long customerId, String adGroupResName, String adResName) {
        String adGroupId = getAdGroupIdFromResName(adGroupResName);
        String adId = getAdIdFromResName(customerId, adResName);

        return "customers/" + customerId + "/adGroupAds/" + adGroupId + "~" + adId;
    }

    public static String getCampaignIdFromResName(String campaignResName) {
        return campaignResName.substring(31);
    }

    public static String getAdGroupIdFromResName(String adGroupResName) {
        return adGroupResName.substring(31);
    }

    public static String getAdIdFromResName(long customerId, String adResName) {
        String prefix = "customers/" + customerId + "/ads/";
        return adResName.replace(prefix, "");
    }
}
