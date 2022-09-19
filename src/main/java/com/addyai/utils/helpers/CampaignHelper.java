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

package com.addyai.utils.helpers;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.*;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.*;
import com.google.ads.googleads.v11.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;
import com.google.ads.googleads.v11.enums.NegativeGeoTargetTypeEnum;
import com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.resources.CampaignCriterion;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.MICRO_FACTOR;

public class CampaignHelper {

    /**
     * Build a campaign object using a CampaignDetails object
     *
     * @param campaignDetails details to be parsed into a [Campaign]
     * @param shouldCreate    true/false if this campaign should be created or updated
     * @return Campaign created from the CampaignDetails provided
     */
    public Campaign buildCampaignFromDetails(CampaignDetails campaignDetails,
                                             boolean shouldCreate) {
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
        if (shouldCreate)
            campaignBuilder.setAdvertisingChannelType(advertisingChannelType);
        else
            campaignBuilder.setResourceName(campaignDetails.getCampaignResourceName());

        return campaignBuilder.build();
    }

    /**
     * Build [CampaignBudgetOperation] to be used for creating or updating campaign budgets
     *
     * @param budgetDetailsList [BudgetDetails] to create budgets from
     * @param shouldCreate      true if it should create budget, false if it should update
     * @return [CampaignBudgetOperation]
     */
    public List<CampaignBudgetOperation> buildCampaignBudgetOperationList(List<BudgetDetails> budgetDetailsList,
                                                                          boolean shouldCreate) {
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        // create and store a budget operation for each budget detail given
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            CampaignBudget.Builder budgetBuilder = CampaignBudget.newBuilder();
            CampaignBudgetOperation.Builder budgetOperationBuilder = CampaignBudgetOperation.newBuilder();

            // set the general budget fields
            budgetBuilder
                    .setName(budgetDetails.getName())
                    .setAmountMicros(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR)
                    .setStatusValue(budgetDetails.getStatus())
                    .setDeliveryMethodValue(budgetDetails.getDeliveryMethod());

            // set the explicitly shared flag when creating budgets
            // set the budget resource name when updating budgets
            if (shouldCreate) {
                budgetBuilder.setExplicitlyShared(budgetDetails.isShared());
                budgetOperationBuilder.setCreate(budgetBuilder.build());
            } else {
                budgetBuilder.setResourceName(budgetDetails.getResourceName());

                CampaignBudget budget = budgetBuilder.build();

                budgetOperationBuilder.setUpdate(budget);
                budgetOperationBuilder.setUpdateMask(FieldMasks.allSetFieldsOf(budget));
            }

            // add the budget operation to the list of operations
            campaignBudgetOperations.add(budgetOperationBuilder.build());
        }

        return campaignBudgetOperations;
    }

    /**
     * Build [CampaignCriterionOperation] to be used for creating or updating campaign criterion.
     *
     * @param criterionDetailsList [CampaignCriterionDetails] to create operations from
     * @param shouldCreate         true if it should create, false if it should update
     * @return [CampaignCriterionOperations]
     */
    public List<CampaignCriterionOperation> buildCampaignCriterionOperationList(
            List<CriterionDetails> criterionDetailsList,
            boolean shouldCreate) {
        List<CampaignCriterionOperation> campaignCriterionOperationList = new ArrayList<>();

        for (CriterionDetails criterionDetails : criterionDetailsList) {
            CampaignCriterion.Builder campaignCriterionBuilder = CampaignCriterion.newBuilder();

            if (criterionDetails instanceof AdScheduleDetails) {
                // create an ad schedule object info for criterion object
                AdScheduleInfo adScheduleInfo = buildAdScheduleInfo((AdScheduleDetails) criterionDetails);

                // if the bid modifier is set, apply to campaign criterion
                if (criterionDetails.getBidModifier() > 0.0f)
                    campaignCriterionBuilder.setBidModifier(criterionDetails.getBidModifier());

                campaignCriterionBuilder
                        .setAdSchedule(adScheduleInfo)
                        .setCampaign(criterionDetails.getCampaignResourceName());

            } else if (criterionDetails instanceof NegativeKeywordDetails) {
                // create a keyword info object for campaign criterion
                KeywordInfo keywordInfo = KeywordInfo.newBuilder()
                        .setMatchTypeValue(((NegativeKeywordDetails) criterionDetails).getKeywordMatchType())
                        .setText(((NegativeKeywordDetails) criterionDetails).getKeywordText())
                        .build();

                campaignCriterionBuilder
                        .setKeyword(keywordInfo)
                        .setCampaign(criterionDetails.getCampaignResourceName())
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
                        .setLanguage(languageInfo)
                        .setCampaign(criterionDetails.getCampaignResourceName());

            } else if (criterionDetails instanceof DeviceDetails) {
                // create device info object for campaign criterion
                DeviceInfo deviceInfo = buildDeviceInfo((DeviceDetails) criterionDetails);

                campaignCriterionBuilder
                        .setDevice(deviceInfo)
                        .setBidModifier(criterionDetails.getBidModifier()) // always set for DeviceInfo
                        .setCampaign(criterionDetails.getCampaignResourceName());

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
                if (locationDetails.getBidModifier() != 0.0)
                    campaignCriterionBuilder.setBidModifier(locationDetails.getBidModifier());

                campaignCriterionBuilder
                        .setLocation(locationInfo)
                        .setCampaign(criterionDetails.getCampaignResourceName());

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
                        .setCampaign(criterionDetails.getCampaignResourceName())
                        .setProximity(proximityInfoBuilder.build());
            }

            // build campaign criterion object
            CampaignCriterion campaignCriterion = campaignCriterionBuilder.build();

            // build a create or update campaignCriterionOperation
            CampaignCriterionOperation.Builder campaignCriterionOperationBuilder = CampaignCriterionOperation.newBuilder();

            // set create or update flag for campaign criterion operation
            if (shouldCreate)
                campaignCriterionOperationBuilder
                        .setCreate(campaignCriterion);
            else
                campaignCriterionOperationBuilder
                        .setUpdate(campaignCriterion)
                        .setUpdateMask(FieldMasks.allSetFieldsOf(campaignCriterion));

            // add CampaignCriterionOperation to list
            campaignCriterionOperationList.add(campaignCriterionOperationBuilder.build());
        }
        return campaignCriterionOperationList;
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
     * @param isTargetingContentNetwork       is this campaign targeting the content network
     * @param isTargetingPartnerSearchNetwork is this campaign targeting the partner search network
     * @param isTargetingSearchNetwork        is this campaign targeting search networking
     * @return completed [Campaign.NetworkSettings]
     */
    private Campaign.NetworkSettings buildNetworkSettings(
            boolean isTargetingContentNetwork,
            boolean isTargetingPartnerSearchNetwork,
            boolean isTargetingSearchNetwork
    ) {
        return Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(isTargetingContentNetwork)
                .setTargetSearchNetwork(isTargetingSearchNetwork)
                .setTargetGoogleSearch(isTargetingPartnerSearchNetwork)
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

        // if the latitude and longitude are set, create and set the geopoints info object
        if (proximityDetails.getMicroLatitude() != 0 &&
                proximityDetails.getMicroLongitude() != 0) {

            // convert longitude and latitude to micro degrees
            int longitude = Math.round(proximityDetails.getMicroLongitude() * MICRO_FACTOR);
            int latitude = Math.round(proximityDetails.getMicroLatitude() * MICRO_FACTOR);

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

    /**
     * Validate campaign details before pushing to Google Ads API
     * If validation fails throw InvalidRequestException
     *
     * @param campaignDetails [CampaignDetails] to be validated
     */
    public void validateCampaignDetails(CampaignDetails campaignDetails) {
        // validate campaign details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isCampaignDetailsValid(campaignDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorCode(), validationErrorResponse.getErrorMessage());
    }

    /**
     * Validate BudgetDetails before pushing to Google Ads API
     * If validation fails throw InvalidRequestException
     *
     * @param budgetDetails [BudgetDetails] to be validated
     */
    public void validateCampaignBudgetDetails(BudgetDetails budgetDetails) {
        // validate budget details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    /**
     * Validate CriterionDetails before pushing to Google Ads API
     * If validation fails throw InvalidRequestException
     *
     * @param criterionDetails [CriterionDetails] to be validated
     */
    public void validateCampaignCriterionDetails(List<CriterionDetails> criterionDetails) {
        // validate criterion details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isCriterionDetailsValid(criterionDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    /**
     * Retrieve a single [BudgetDetails] based on a resource name
     *
     * @param resourceName      the resource name of the campaign budget
     * @param budgetDetailsList a list of existing budgetDetails from the client account
     * @return [BudgetDetails] that matched the resource name given; null if no budget is found
     */
    public BudgetDetails findBudgetDetailsByResourceName(String resourceName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getResourceName().equals(resourceName))
                return budgetDetails;
        }

        return null;
    }

    /**
     * Retrieve a single [BudgetDetails] based on a budget name
     *
     * @param budgetName        the name of the campaign budget
     * @param budgetDetailsList a list of existing budgetDetails from the client account
     * @return [BudgetDetails] that matched the budget name given; null if no budget is found
     */
    public BudgetDetails findBudgetDetailsByName(String budgetName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getName().equals(budgetName))
                return budgetDetails;
        }

        return null;
    }
}
