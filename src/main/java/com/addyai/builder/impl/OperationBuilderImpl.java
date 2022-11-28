/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.builder.impl;

import com.addyai.builder.OperationBuilder;
import com.addyai.enums.OperationType;
import com.addyai.models.AdGroupDetails;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.KeywordDetails;
import com.addyai.models.assets.AssetDetails;
import com.addyai.models.assets.SitelinkDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v12.common.*;
import com.google.ads.googleads.v12.enums.*;
import com.google.ads.googleads.v12.resources.*;
import com.google.ads.googleads.v12.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.addyai.utils.misc.Constants.MICRO_FACTOR;

public class OperationBuilderImpl implements OperationBuilder {
    @Override
    public List<CampaignOperation> buildCampaignOperationList(List<CampaignDetails> campaignDetailsList,
                                                              OperationType operationType) {
        List<CampaignOperation> campaignOperationsList = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // if OperationType is REMOVE, create a remove operation for campaign
            if (operationType == OperationType.REMOVE) {
                CampaignOperation operation = CampaignOperation.newBuilder()
                        .setRemove(campaignDetails.getCampaignResourceName())
                        .build();
                campaignOperationsList.add(operation);
                continue;
            }

            Campaign campaign = buildCampaignFromDetails(campaignDetails, operationType);

            CampaignOperation.Builder campaignOperationBuilder = CampaignOperation.newBuilder();

            if (operationType == OperationType.CREATE) {
                campaignOperationBuilder
                        .setCreate(campaign);
            } else if (operationType == OperationType.UPDATE) {
                campaignOperationBuilder
                        .setUpdate(campaign)
                        .setUpdateMask(FieldMasks.allSetFieldsOf(campaign));
            }
            campaignOperationsList.add(campaignOperationBuilder.build());
        }
        return campaignOperationsList;
    }

    /**
     * Build [CampaignBudgetOperation] to be used for creating or updating campaign budgets
     *
     * @param budgetDetailsList [BudgetDetails] to create budgets from
     * @param operationType     the OperationType to be performed
     * @return [CampaignBudgetOperation]
     */
    @Override
    public List<CampaignBudgetOperation> buildCampaignBudgetOperationList(List<BudgetDetails> budgetDetailsList,
                                                                          OperationType operationType) {
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        // create and store a budget operation for each budget detail given
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            CampaignBudget.Builder budgetBuilder = CampaignBudget.newBuilder();
            CampaignBudgetOperation.Builder budgetOperationBuilder = CampaignBudgetOperation.newBuilder();

            // set the general budget fields
            budgetBuilder
                    .setAmountMicros(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR)
                    .setStatusValue(budgetDetails.getStatus())
                    .setDeliveryMethodValue(budgetDetails.getDeliveryMethod());

            // set the explicitly shared flag when creating budgets
            // set the budget resource name when updating budgets
            if (operationType.equals(OperationType.CREATE)) {
                budgetBuilder.setName(budgetDetails.getName());
                budgetBuilder.setExplicitlyShared(budgetDetails.isShared());
                budgetOperationBuilder.setCreate(budgetBuilder.build());
            } else if (operationType.equals(OperationType.UPDATE)) {
                budgetBuilder.setResourceName(budgetDetails.getResourceName());

                CampaignBudget budget = budgetBuilder.build();

                budgetOperationBuilder.setUpdate(budget);
                budgetOperationBuilder.setUpdateMask(FieldMasks.allSetFieldsOf(budget));
            } else if (operationType.equals(OperationType.REMOVE)) {
                budgetOperationBuilder.setRemove(budgetDetails.getResourceName());
            }

            // add the budget operation to the list of operations
            campaignBudgetOperations.add(budgetOperationBuilder.build());
        }

        return campaignBudgetOperations;
    }

    /**
     * Build [CampaignCriterionOperation] to be used for creating or updating campaign criterion.
     *
     * @param criterionMapping a mapping of campaign resource name to a list of CriterionDetails
     * @param operationType    the OperationType to be performed
     * @return [CampaignCriterionOperations]
     */
    @Override
    public List<CampaignCriterionOperation> buildCampaignCriterionOperationList(Map<String,
            List<CriterionDetails>> criterionMapping, OperationType operationType) {
        List<CampaignCriterionOperation> campaignCriterionOperationList = new ArrayList<>();

        criterionMapping.forEach((campaignResourceName, criterionDetailsList) -> {
            for (CriterionDetails criterionDetails : criterionDetailsList) {
                CampaignCriterion.Builder campaignCriterionBuilder = CampaignCriterion.newBuilder();

                if (criterionDetails instanceof AdScheduleDetails) {
                    // create an ad schedule object info for criterion object
                    AdScheduleInfo adScheduleInfo = buildAdScheduleInfo((AdScheduleDetails) criterionDetails);

                    // if the bid modifier is set, apply to campaign criterion
                    if (criterionDetails.getBidModifier() > 0.0f)
                        campaignCriterionBuilder.setBidModifier(criterionDetails.getBidModifier());

                    campaignCriterionBuilder
                            .setAdSchedule(adScheduleInfo);

                } else if (criterionDetails instanceof NegativeKeywordDetails) {
                    // create a keyword info object for campaign criterion
                    KeywordInfo keywordInfo = KeywordInfo.newBuilder()
                            .setMatchTypeValue(((NegativeKeywordDetails) criterionDetails).getKeywordMatchType())
                            .setText(((NegativeKeywordDetails) criterionDetails).getKeywordText())
                            .build();

                    campaignCriterionBuilder
                            .setKeyword(keywordInfo)
                            .setNegative(criterionDetails.isNegative()); // always true for keywordInfo

                } else if (criterionDetails instanceof LanguageDetails) {
                    // create a language info object for campaign criterion
                    LanguageDetails languageDetails = ((LanguageDetails) criterionDetails);

                    LanguageInfo languageInfo = LanguageInfo.newBuilder()
                            .setLanguageConstant(languageDetails.getLanguageCode())
                            .build();

                    // update if this is a negative language target
                    if (languageDetails.isNegative())
                        campaignCriterionBuilder.setNegative(true);

                    campaignCriterionBuilder
                            .setLanguage(languageInfo);

                } else if (criterionDetails instanceof DeviceDetails) {
                    // create device info object for campaign criterion
                    DeviceInfo deviceInfo = buildDeviceInfo((DeviceDetails) criterionDetails);

                    campaignCriterionBuilder
                            .setDevice(deviceInfo)
                            .setBidModifier(criterionDetails.getBidModifier()); // always set for DeviceInfo

                } else if (criterionDetails instanceof LocationDetails) {
                    // create location info object for campaign criterion
                    LocationDetails locationDetails = ((LocationDetails) criterionDetails);

                    LocationInfo locationInfo = LocationInfo.newBuilder()
                            .setGeoTargetConstant(locationDetails.getGeoTargetingConstant())
                            .build();

                    // update if a negative target
                    if (locationDetails.isNegative())
                        campaignCriterionBuilder.setNegative(true);

                    // if needed, update the bid modifier
                    if (locationDetails.getBidModifier() > 0.0)
                        campaignCriterionBuilder.setBidModifier(locationDetails.getBidModifier());

                    campaignCriterionBuilder
                            .setLocation(locationInfo);

                } else if (criterionDetails instanceof ProximityDetails) {
                    // create proximity info for campaign criterion
                    ProximityDetails proximityDetails = (ProximityDetails) criterionDetails;

                    // create a proximity info builder
                    ProximityInfo.Builder proximityInfoBuilder = buildProximityInfoBuilder(proximityDetails);

                    // update if this proximity target is using a bid modifier
                    if (proximityDetails.getBidModifier() != 0.0f)
                        campaignCriterionBuilder.setBidModifier(proximityDetails.getBidModifier());

                    // create the campaign criterion object from the details
                    campaignCriterionBuilder
                            .setProximity(proximityInfoBuilder.build());
                }

                if (operationType.equals(OperationType.CREATE))
                    campaignCriterionBuilder.setCampaign(campaignResourceName);
                else if (operationType.equals(OperationType.UPDATE))
                    campaignCriterionBuilder.setResourceName(criterionDetails.getCriterionResourceName());

                // build campaign criterion object
                CampaignCriterion campaignCriterion = campaignCriterionBuilder
                        .setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED)
                        .build();

                // build a create or update campaignCriterionOperation
                CampaignCriterionOperation.Builder campaignCriterionOperationBuilder = CampaignCriterionOperation.newBuilder();

                // set create or update flag for campaign criterion operation
                if (operationType.equals(OperationType.CREATE))
                    campaignCriterionOperationBuilder
                            .setCreate(campaignCriterion);
                else if (operationType.equals(OperationType.UPDATE))
                    campaignCriterionOperationBuilder
                            .setUpdate(campaignCriterion)
                            .setUpdateMask(FieldMasks.allSetFieldsOf(campaignCriterion));
                else if (operationType.equals(OperationType.REMOVE))
                    campaignCriterionOperationBuilder.setRemove(campaignCriterion.getResourceName());

                // add CampaignCriterionOperation to list
                campaignCriterionOperationList.add(campaignCriterionOperationBuilder.build());
            }
        });

        return campaignCriterionOperationList;
    }

    /**
     * Build [AdGroupOperation] to be used to create, update or remove AdGroups
     *
     * @param adGroupDetailsList [AdGroupDetails] used to populate the fields for each AdGroupOperation
     * @param operationType      the type of operation to be performed: CREATE, UPDATE or REMOVE
     * @return [AdGroupOperation] the AdGroupOperations to be performed on the client's account
     */
    @Override
    public List<AdGroupOperation> buildAdGroupOperationList(List<AdGroupDetails> adGroupDetailsList, OperationType operationType) {
        List<AdGroupOperation> adGroupOperationList = new ArrayList<>();

        for (AdGroupDetails adGroupDetails : adGroupDetailsList) {
            AdGroupOperation.Builder adGroupOperationBuilder = AdGroupOperation.newBuilder();

            AdGroup adGroup = buildAdGroupFromDetails(adGroupDetails, operationType);
            if (operationType.equals(OperationType.CREATE)) {
                adGroupOperationBuilder.setCreate(adGroup);
            } else if (operationType.equals(OperationType.UPDATE)) {
                adGroupOperationBuilder.setUpdate(adGroup);
                adGroupOperationBuilder.setUpdateMask(FieldMasks.allSetFieldsOf(adGroup));
            } else if (operationType.equals(OperationType.REMOVE)) {
                adGroupOperationBuilder.setRemove(adGroupDetails.getAdGroupResourceName());
            }

            adGroupOperationList.add(adGroupOperationBuilder.build());
        }
        return adGroupOperationList;
    }

    @Override
    public List<AdGroupCriterionOperation> buildAdGroupCriterionOperationList(List<KeywordDetails> keywordDetailsList,
                                                                              OperationType operationType) {
        List<AdGroupCriterionOperation> keywordOperationList = new ArrayList<>();

        for (KeywordDetails keywordDetails : keywordDetailsList) {
            AdGroupCriterionOperation.Builder keywordOperationBuilder = AdGroupCriterionOperation.newBuilder();

            AdGroupCriterion adGroupCriterion = buildAdGroupCriterionFromDetails(keywordDetails, operationType);
            if (operationType.equals(OperationType.CREATE)) {
                keywordOperationBuilder.setCreate(adGroupCriterion);
            } else if (operationType.equals(OperationType.UPDATE)) {
                keywordOperationBuilder.setUpdate(adGroupCriterion);
                keywordOperationBuilder.setUpdateMask(FieldMasks.allSetFieldsOf(adGroupCriterion));
            } else if (operationType.equals(OperationType.REMOVE)) {
                keywordOperationBuilder.setRemove(keywordDetails.getKeywordResourceName());
            }

            keywordOperationList.add(keywordOperationBuilder.build());
        }
        return keywordOperationList;
    }

    /**
     * Build a campaign object using a CampaignDetails object
     *
     * @param campaignDetails details to be parsed into a [Campaign]
     * @param operationType   the campaign operation type to be built (CREATE or UPDATE)
     * @return Campaign created from the CampaignDetails provided
     */
    private Campaign buildCampaignFromDetails(CampaignDetails campaignDetails, OperationType operationType) {
        // create a Manual cpc object with or without enhanced CPC
        ManualCpc manualCpc = buildManualCpc(campaignDetails.isEnhancedCpcEnabled());

        // create a GeoTargetTypeSetting using the negative and positive targets
        Campaign.GeoTargetTypeSetting geoTargetTypeSetting = buildGeoTargetTypeSettings(
                campaignDetails.getPositiveGeoTargetType(),
                campaignDetails.getNegativeGeoTargetType());

        // extract network settings into its own object
        Campaign.NetworkSettings networkSettings = buildNetworkSettings(
                campaignDetails.isTargetingContentNetwork(),
                campaignDetails.isTargetingGoogleSearchNetwork(),
                campaignDetails.isTargetingSearchNetwork()
        );

        // create the campaign status object
        CampaignStatusEnum.CampaignStatus status =
                CampaignStatusEnum.CampaignStatus.valueOf(campaignDetails.getStatus());

        //create the advertising channel type object
        AdvertisingChannelTypeEnum.AdvertisingChannelType advertisingChannelType =
                AdvertisingChannelTypeEnum.AdvertisingChannelType.valueOf(campaignDetails.getAdvertisingChannelType());

        Campaign.Builder campaignBuilder = Campaign.newBuilder();

        // set the general campaign values
        campaignBuilder.setStatus(status)
                .setId(campaignDetails.getCampaignId())
                .setStartDate(campaignDetails.getStartDate())
                .setEndDate(campaignDetails.getEndDate())
                .setName(campaignDetails.getCampaignName())
                .setGeoTargetTypeSetting(geoTargetTypeSetting)
                .setCampaignBudget(campaignDetails.getBudgetResourceName())
                .setManualCpc(manualCpc)
                .setNetworkSettings(networkSettings);

        // set advertising channel if creating campaign
        // set resource name is updating a campaign
        if (operationType.equals(OperationType.CREATE))
            campaignBuilder.setAdvertisingChannelType(advertisingChannelType);
        else if (operationType.equals(OperationType.UPDATE))
            campaignBuilder.setResourceName(campaignDetails.getCampaignResourceName());

        return campaignBuilder.build();
    }

    @Override
    public List<AssetOperation> buildAssetOperationList(List<AssetDetails> assetDetailsList, OperationType operationType) {
        List<AssetOperation> assetOperationList = new ArrayList<>();
        AssetOperation.Builder assetOperationBuilder = AssetOperation.newBuilder();

        for (AssetDetails assetDetails : assetDetailsList) {
            Asset.Builder assetBuilder = Asset.newBuilder();

            if (assetDetails.getAssetType() == AssetTypeEnum.AssetType.SITELINK_VALUE) {
                SitelinkDetails sitelinkDetails = (SitelinkDetails) assetDetails;
                SitelinkAsset.Builder sitelinkAssetBuilder = SitelinkAsset.newBuilder();
                sitelinkAssetBuilder.setDescription1(sitelinkDetails.getDescription1());
                sitelinkAssetBuilder.setDescription2(sitelinkDetails.getDescription2());
                sitelinkAssetBuilder.setLinkText(sitelinkDetails.getLinkText());
                sitelinkAssetBuilder.setStartDate(sitelinkDetails.getStartDate());
                sitelinkAssetBuilder.setEndDate(sitelinkDetails.getEndDate());

                assetBuilder.setSitelinkAsset(sitelinkAssetBuilder.build());
            }

            // add the final urls to the asset object
            assetBuilder.addAllFinalUrls(assetDetails.getFinalUrlList());

            // add the final mobile urls to the asset object
            assetBuilder.addAllFinalMobileUrls(assetDetails.getFinalMobileUrlList());

            // add a final suffix
            assetBuilder.setFinalUrlSuffix(assetDetails.getFinalUrlSuffix());

            Asset asset;
            if (operationType.equals(OperationType.CREATE)) {
                asset = assetBuilder.build();
                assetOperationBuilder.setCreate(asset);
            } else if (operationType.equals(OperationType.UPDATE)) {
                assetBuilder.setResourceName(assetDetails.getAssetName());
                asset = assetBuilder.build();

                assetOperationBuilder.setUpdate(asset);
                assetOperationBuilder.setUpdateMask(FieldMasks.allSetFieldsOf(asset));
            }
            assetOperationList.add(assetOperationBuilder.build());
        }
        return assetOperationList;
    }

    /**
     * Build a ManualCpc object for use in creating/updating campaigns
     *
     * @param isEnhancedCpcEnabled is the campaign supporting enhanced CPC
     * @return a completed [ManualCpc]
     */
    private ManualCpc buildManualCpc(boolean isEnhancedCpcEnabled) {
        return ManualCpc.newBuilder()
                .setEnhancedCpcEnabled(isEnhancedCpcEnabled)
                .build();
    }

    /**
     * Build a GeoTargetTypeSettings object for use in creating/updating campaigns
     *
     * @param positiveGeoTargetType the positive GeoTargetType code
     * @param negativeGeoTargetType the negative GeoTargetType code
     * @return completed [Campaign.GeoTargetTypeSetting]
     */
    private Campaign.GeoTargetTypeSetting buildGeoTargetTypeSettings(int positiveGeoTargetType,
                                                                     int negativeGeoTargetType) {
        return Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType
                        .forNumber(negativeGeoTargetType))
                .setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType
                        .forNumber(positiveGeoTargetType))
                .build();
    }

    /**
     * Build a Networking Settings object for use in creating/updating campaigns
     *
     * @param isTargetingContentNetwork      is this campaign targeting the content network
     * @param isTargetingGoogleSearchNetwork is this campaign targeting the Google search network
     * @param isTargetingSearchNetwork       is this campaign targeting search networking (includes partners)
     * @return completed [Campaign.NetworkSettings]
     */
    private Campaign.NetworkSettings buildNetworkSettings(
            boolean isTargetingContentNetwork,
            boolean isTargetingGoogleSearchNetwork,
            boolean isTargetingSearchNetwork
    ) {
        return Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(isTargetingContentNetwork)
                .setTargetGoogleSearch(isTargetingGoogleSearchNetwork)
                .setTargetSearchNetwork(isTargetingSearchNetwork)
                .build();
    }

    /**
     * Build an AdScheduleInfo object from details
     *
     * @param adScheduleDetails the AdScheduleDetails to build AdScheduleInfo from
     * @return AdScheduleInfo object
     */
    private AdScheduleInfo buildAdScheduleInfo(AdScheduleDetails adScheduleDetails) {
        // create ad schedule info
        return AdScheduleInfo.newBuilder()
                .setDayOfWeekValue((adScheduleDetails.getDayOfWeek()))
                .setStartHour(adScheduleDetails.getStartHour())
                .setEndHour(adScheduleDetails.getEndHour())
                .setStartMinuteValue(adScheduleDetails.getStartMinute())
                .setEndMinuteValue(adScheduleDetails.getEndMinute())
                .build();
    }

    /**
     * Build a DeviceInfo object from details
     *
     * @param deviceDetails the DeviceDetails to build DeviceInfo from
     * @return DeviceInfo object
     */
    private DeviceInfo buildDeviceInfo(DeviceDetails deviceDetails) {
        return DeviceInfo.newBuilder()
                .setTypeValue(deviceDetails.getDeviceType())
                .build();
    }

    /**
     * Build a ProximityInfo.Builder object from details
     * Can be built using either AddressInfo or GeoPointInfo
     *
     * @param proximityDetails the ProximityDetails to create ProximityInfo.Builder from
     * @return ProximityInfo.Builder object
     */
    private ProximityInfo.Builder buildProximityInfoBuilder(ProximityDetails proximityDetails) {
        ProximityInfo.Builder proximityInfoBuilder = ProximityInfo.newBuilder();

        // if the address details are not empty, create Address info object and set it in the builder
        if (!proximityDetails.getStreetAddress().isEmpty() && !proximityDetails.getPostalCode().isEmpty() &&
                !proximityDetails.getCityName().isEmpty() && !proximityDetails.getCountryCode().isEmpty()) {

            // Instantiate an AddressInfo Builder
            AddressInfo.Builder addressInfoBuilder = AddressInfo.newBuilder();

            // add the address information to the address info object
            addressInfoBuilder
                    .setStreetAddress(proximityDetails.getStreetAddress())
                    .setPostalCode(proximityDetails.getPostalCode())
                    .setCityName(proximityDetails.getCityName())
                    .setCountryCode(proximityDetails.getCountryCode());

            // if providence name and providence code are set, add them to the addressInfo object
            if (!proximityDetails.getProvinceName().isEmpty() &&
                    !proximityDetails.getProvinceCode().isEmpty()) {
                addressInfoBuilder
                        .setProvinceCode(proximityDetails.getProvinceCode())
                        .setProvinceName(proximityDetails.getProvinceName());
            }

            proximityInfoBuilder.setAddress(addressInfoBuilder.build());
        }

        // if the latitude and longitude are set and the address is not set, create and set the geopoints info object
        if (proximityDetails.getLatitude() != 0 &&
                proximityDetails.getLongitude() != 0 &&
                !proximityInfoBuilder.hasAddress()) {

            // convert longitude and latitude to micro degrees
            int longitude = Math.round(proximityDetails.getLongitude() * MICRO_FACTOR);
            int latitude = Math.round(proximityDetails.getLatitude() * MICRO_FACTOR);

            // set the longitude and latitude geo points in the builder
            GeoPointInfo geoPointInfo = GeoPointInfo.newBuilder()
                    .setLatitudeInMicroDegrees(latitude)
                    .setLongitudeInMicroDegrees(longitude)
                    .build();

            proximityInfoBuilder.setGeoPoint(geoPointInfo);
        }

        // if the radius value is set, assign radius and measurement units for the radius to the builder
        if (proximityDetails.getRadius() > 0) {
            proximityInfoBuilder
                    .setRadius(proximityDetails.getRadius())
                    .setRadiusUnitsValue(proximityDetails.getRadiusUnits());
        }
        return proximityInfoBuilder;
    }

    private AdGroup buildAdGroupFromDetails(AdGroupDetails adGroupDetails, OperationType operationType) {
        AdGroup.Builder adGroupBuilder = AdGroup.newBuilder();

        if (operationType.equals(OperationType.REMOVE)) {
            adGroupBuilder.setResourceName(adGroupDetails.getAdGroupResourceName());
            return adGroupBuilder.build();
        } else if (operationType.equals(OperationType.CREATE)) {
            adGroupBuilder.setCampaign(adGroupDetails.getCampaignResourceName());
            adGroupBuilder.setTypeValue(adGroupDetails.getType());
        } else if (operationType.equals(OperationType.UPDATE))
            adGroupBuilder.setResourceName(adGroupDetails.getAdGroupResourceName());

        if (adGroupDetails.getCpcBid() != 0.0)
            adGroupBuilder.setCpcBidMicros((long) (adGroupDetails.getCpcBid() * MICRO_FACTOR));
        if (!adGroupDetails.getAdGroupName().isEmpty())
            adGroupBuilder.setName(adGroupDetails.getAdGroupName());
        if (adGroupDetails.getStatus() != -1)
            adGroupBuilder.setStatusValue(adGroupDetails.getStatus());

        return adGroupBuilder.build();
    }

    private AdGroupCriterion buildAdGroupCriterionFromDetails(KeywordDetails keywordDetails, OperationType operationType) {
        AdGroupCriterion.Builder keywordBuilder = AdGroupCriterion.newBuilder();

        if (operationType.equals(OperationType.REMOVE)) {
            keywordBuilder.setResourceName(keywordDetails.getKeywordResourceName());
            return keywordBuilder.build();
        } else if (operationType.equals(OperationType.CREATE)) {
            keywordBuilder.setAdGroup(keywordDetails.getAdGroupResourceName());
            keywordBuilder.setKeyword(KeywordInfo.newBuilder()
                    .setText(keywordDetails.getKeywordText())
                    .setMatchTypeValue(keywordDetails.getKeywordMatchType()));
            keywordBuilder.setStatusValue(keywordDetails.getStatus());
            keywordBuilder.setCpcBidMicros((long) (keywordDetails.getCpcBid() * MICRO_FACTOR));
        } else if (operationType.equals(OperationType.UPDATE)) {
            // keyword can only update the cpc bid and/or status
            keywordBuilder.setResourceName(keywordDetails.getKeywordResourceName());

            if (keywordDetails.getCpcBid() > 0.0) {
                keywordBuilder.setCpcBidMicros((long) (keywordDetails.getCpcBid() * MICRO_FACTOR));
            }

            if (keywordDetails.getStatus() != -1) {
                keywordBuilder.setStatusValue(keywordDetails.getStatus());
            }
        }
        return keywordBuilder.build();
    }
}
