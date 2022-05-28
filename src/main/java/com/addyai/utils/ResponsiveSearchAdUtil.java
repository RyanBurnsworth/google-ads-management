package com.addyai.utils;

import com.google.ads.googleads.v10.common.AdTextAsset;

import java.util.ArrayList;
import java.util.List;

public class ResponsiveSearchAdUtil {
    public List<AdTextAsset> createHeadlinesList(List<String> headlines) {
        List<AdTextAsset> headlineList = new ArrayList<>();

        for (String headline : headlines) {
            headlineList.add(createAdTextAsset(headline));
        }
        return headlineList;
    }

    public List<AdTextAsset> createDescriptionList(List<String> descriptions) {
        List<AdTextAsset> descriptionList = new ArrayList<>();

        for (String description : descriptions) {
            descriptionList.add(createAdTextAsset(description));
        }
        return descriptionList;
    }

    /**
     * Creates an AdTextAsset from a given string.
     *
     * @param text the text string to insert in the AdTextAsset.
     * @return AdTextAsset.
     */
    private AdTextAsset createAdTextAsset(String text) {
        return AdTextAsset.newBuilder().setText(text).build();
    }
}
