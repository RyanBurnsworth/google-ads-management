package com.addyai.repos.campaigns.impl;

import com.addyai.models.campaign_criterion.*;
import com.addyai.repos.campaigns.CampaignCriteriaRepository;
import com.google.ads.googleads.v11.common.*;
import com.google.ads.googleads.v11.resources.CampaignCriterion;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;

import java.util.ArrayList;
import java.util.List;

public class CampaignCriteriaRepositoryImpl implements CampaignCriteriaRepository {
    @Override
    public void getCampaignCriteria(long customerId, long campaignId) {

    }

    @Override
    public void createCampaignCriterion(long customerId, List<CampaignCriteria> campaignCriteriaList) {
        List<CampaignCriterionOperation> campaignCriterionOperationList = new ArrayList<>();

        for (CampaignCriteria campaignCriteria : campaignCriteriaList) {
            CampaignCriterion campaignCriterion = CampaignCriterion.newBuilder().build();

            if (campaignCriteria instanceof AdScheduleCriteria) {
                // create ad schedule info
                AdScheduleInfo adScheduleInfo = AdScheduleInfo.newBuilder()
                        .setDayOfWeek(((AdScheduleCriteria) campaignCriteria).getDayOfWeek())
                        .setStartHour(((AdScheduleCriteria) campaignCriteria).getStartHour())
                        .setEndHour(((AdScheduleCriteria) campaignCriteria).getEndHour())
                        .setStartMinute(((AdScheduleCriteria) campaignCriteria).getStartMinute())
                        .setEndMinute(((AdScheduleCriteria) campaignCriteria).getEndMinute())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setAdSchedule(adScheduleInfo)
                        .setCampaign(campaignCriteria.getCampaignName())
                        .setBidModifier(campaignCriteria.getBidModifier())
                        .setNegative(campaignCriteria.isNegative())
                        .build();

            } else if (campaignCriteria instanceof KeywordCriteria) {
                KeywordInfo keywordInfo = KeywordInfo.newBuilder()
                        .setMatchType(((KeywordCriteria) campaignCriteria).getKeywordMatchType())
                        .setText(((KeywordCriteria) campaignCriteria).getKeywordText())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setKeyword(keywordInfo)
                        .setNegative(campaignCriteria.isNegative())
                        .setBidModifier(campaignCriteria.getBidModifier())
                        .build();

            } else if (campaignCriteria instanceof LanguageLocationCriteria) {
                LanguageInfo languageInfo = LanguageInfo.newBuilder()
                        .setLanguageConstant(((LanguageLocationCriteria) campaignCriteria).getLanguageCode())
                        .build();

                LocationInfo locationInfo = LocationInfo.newBuilder()
                        .setGeoTargetConstant(((LanguageLocationCriteria) campaignCriteria).getGeoTargetingConstant())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setLanguage(languageInfo)
                        .setLocation(locationInfo)
                        .setNegative(campaignCriteria.isNegative())
                        .setBidModifier(campaignCriteria.getBidModifier())
                        .build();

            } else if (campaignCriteria instanceof ProximityCriteria) {
                AddressInfo addressInfo = AddressInfo.newBuilder()
                        .setStreetAddress(((ProximityCriteria) campaignCriteria).getStreetAddress())
                        .setPostalCode(((ProximityCriteria) campaignCriteria).getPostalCode())
                        .setCityName(((ProximityCriteria) campaignCriteria).getCityName())
                        .setProvinceCode(((ProximityCriteria) campaignCriteria).getProvinceCode())
                        .setProvinceName(((ProximityCriteria) campaignCriteria).getProvinceName())
                        .setCountryCode(((ProximityCriteria) campaignCriteria).getCountryCode())
                        .build();

                GeoPointInfo geoPointInfo = GeoPointInfo.newBuilder()
                        .setLatitudeInMicroDegrees(((ProximityCriteria) campaignCriteria).getMicroLatitude())
                        .setLongitudeInMicroDegrees(((ProximityCriteria) campaignCriteria).getMicroLongitude())
                        .build();

                ProximityInfo proximityInfo = ProximityInfo.newBuilder()
                        .setAddress(addressInfo)
                        .setRadius(((ProximityCriteria) campaignCriteria).getRadius())
                        .setRadiusUnits(((ProximityCriteria) campaignCriteria).getRadiusUnits())
                        .setGeoPoint(geoPointInfo)
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setProximity(proximityInfo)
                        .setNegative(campaignCriteria.isNegative())
                        .setBidModifier(campaignCriteria.getBidModifier())
                        .build();
            }

            CampaignCriterionOperation operation = CampaignCriterionOperation.newBuilder()
                    .setCreate(campaignCriterion)
                    .build();

            campaignCriterionOperationList.add(operation);
        }
    }
}
