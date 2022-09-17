package com.addyai.utils;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.*;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.resources.CampaignCriterion;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.Constants.MICRO_FACTOR;

public class CampaignUtils {

    /**
     * Generate a campaign object using a CampaignDetails object (for use in creating new campaigns)
     *
     * @param campaignDetails [CampaignDetails] to be parsed into a [Campaign]
     * @param shouldCreate    true/false if this campaign should be created or updated
     * @return [Campaign] parsed from [CampaignDetails] provided
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
                campaignDetails.isTargetingPartnerSearchNetwork(),
                campaignDetails.isTargetingSearchNetwork()
        );

        // create the campaign status object
        CampaignStatusEnum.CampaignStatus status =
                CampaignStatusEnum.CampaignStatus.valueOf(campaignDetails.getStatus());

        //create the advertising channel type object
        AdvertisingChannelTypeEnum.AdvertisingChannelType advertisingChannelType =
                AdvertisingChannelTypeEnum.AdvertisingChannelType.valueOf(campaignDetails.getAdvertisingChannelType());

        // create and return a campaign object with the above settings
        if (shouldCreate) {
            // if shouldCreate is true return a campaign object for creation
            return Campaign.newBuilder()
                    .setStatus(status)
                    .setId(campaignDetails.getCampaignId())
                    .setStartDate(campaignDetails.getStartDate())
                    .setEndDate(campaignDetails.getEndDate())
                    .setName(campaignDetails.getCampaignName())
                    .setGeoTargetTypeSetting(geoTargetTypeSetting)
                    .setCampaignBudget(campaignDetails.getBudgetResourceName())
                    .setManualCpc(manualCpc)
                    .setNetworkSettings(networkSettings)
                    .setAdvertisingChannelType(advertisingChannelType) // only set for campaign creation
                    .build();
        } else {
            // if shouldCreate is false return a campaign object for updating
            return Campaign.newBuilder()
                    .setStatus(status)
                    .setId(campaignDetails.getCampaignId())
                    .setStartDate(campaignDetails.getStartDate())
                    .setEndDate(campaignDetails.getEndDate())
                    .setName(campaignDetails.getCampaignName())
                    .setResourceName(campaignDetails.getCampaignResourceName()) // needed for updating
                    .setGeoTargetTypeSetting(geoTargetTypeSetting)
                    .setCampaignBudget(campaignDetails.getBudgetResourceName())
                    .setManualCpc(manualCpc)
                    .setNetworkSettings(networkSettings)
                    .build();
        }
    }

    /**
     * Build a list of CampaignBudgetOperations to be used for creating or updating campaign budgets
     *
     * @param budgetDetailsList a list of budget details
     * @param shouldCreate      true if it should create budget, false if it should update
     * @return list of CampaignBudgetOperations to be performed on the client account
     */
    public List<CampaignBudgetOperation> buildCampaignBudgetOperationList(List<BudgetDetails> budgetDetailsList,
                                                                          boolean shouldCreate) {
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        for (BudgetDetails budgetDetails : budgetDetailsList) {
            // if shouldCreate is true, create the budget within the client account
            CampaignBudgetOperation operation;
            if (shouldCreate) {
                // build a CampaignBudget object using the given budget details
                CampaignBudget budget = CampaignBudget.newBuilder()
                        .setName(budgetDetails.getName())
                        .setAmountMicros(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR)
                        .setStatus(BudgetStatusEnum.BudgetStatus
                                .forNumber(budgetDetails.getStatus()))
                        .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod
                                .forNumber(budgetDetails.getDeliveryMethod()))
                        .setExplicitlyShared(budgetDetails.isShared()) // only allowed in budget creation
                        .build();

                operation = CampaignBudgetOperation.newBuilder()
                        .setCreate(budget)
                        .build();
            } else {
                // if shouldCreate is false, update the existing budget within the client account
                CampaignBudget budget = CampaignBudget.newBuilder()
                        .setName(budgetDetails.getName())
                        .setResourceName(budgetDetails.getResourceName()) // must include in order to update
                        .setAmountMicros(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR)
                        .setStatus(BudgetStatusEnum.BudgetStatus
                                .forNumber(budgetDetails.getStatus()))
                        .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod
                                .forNumber(budgetDetails.getDeliveryMethod()))
                        .build();

                operation = CampaignBudgetOperation.newBuilder()
                        .setUpdate(budget)
                        .setUpdateMask(FieldMasks.allSetFieldsOf(budget))
                        .build();
            }

            // add the budget operation to the list of operations
            campaignBudgetOperations.add(operation);
        }
        return campaignBudgetOperations;
    }

    /**
     * Build a list of CampaignCriterionOperation to be used for creating or updating campaign criterion.
     * The campaign to be updated is specified by it's campaignResourceName
     *
     * @param campaignCriterionDetailsList a list of [CampaignCriterionDetails]
     * @param shouldCreate                 true if it should create, false if it should update
     * @return list of [CampaignCriterionOperations] to be performed on the specified campaign
     */
    public List<CampaignCriterionOperation> buildCampaignCriterionOperationList(
            List<CampaignCriterionDetails> campaignCriterionDetailsList,
            boolean shouldCreate) {
        List<CampaignCriterionOperation> campaignCriterionOperationList = new ArrayList<>();

        // TODO: Validate campaign resource name in each object

        for (CampaignCriterionDetails campaignCriterionDetails : campaignCriterionDetailsList) {
            CampaignCriterion campaignCriterion = null;

            if (campaignCriterionDetails instanceof AdScheduleDetails) {
                // create ad schedule info
                AdScheduleInfo adScheduleInfo = AdScheduleInfo.newBuilder()
                        .setDayOfWeek(DayOfWeekEnum.DayOfWeek.forNumber(
                                ((AdScheduleDetails) campaignCriterionDetails).getDayOfWeek()))
                        .setStartHour(((AdScheduleDetails) campaignCriterionDetails).getStartHour())
                        .setEndHour(((AdScheduleDetails) campaignCriterionDetails).getEndHour())
                        .setStartMinuteValue(((AdScheduleDetails) campaignCriterionDetails).getStartMinute())
                        .setEndMinuteValue(((AdScheduleDetails) campaignCriterionDetails).getEndMinute())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setAdSchedule(adScheduleInfo)
                        .setCampaign(campaignCriterionDetails.getCampaignResourceName())
                        .setBidModifier(campaignCriterionDetails.getBidModifier())
                        .build();
            } else if (campaignCriterionDetails instanceof KeywordDetails) {
                KeywordInfo keywordInfo = KeywordInfo.newBuilder()
                        .setMatchTypeValue(((KeywordDetails) campaignCriterionDetails).getKeywordMatchType())
                        .setText(((KeywordDetails) campaignCriterionDetails).getKeywordText())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setKeyword(keywordInfo)
                        .setCampaign(campaignCriterionDetails.getCampaignResourceName())
                        .setNegative(campaignCriterionDetails.isNegative())
                        .build();

            } else if (campaignCriterionDetails instanceof LanguageDetails) {
                LanguageInfo languageInfo = LanguageInfo.newBuilder()
                        .setLanguageConstant(((LanguageDetails) campaignCriterionDetails).getLanguageCode())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setLanguage(languageInfo)
                        .setCampaign(campaignCriterionDetails.getCampaignResourceName())
                        .build();

            } else if (campaignCriterionDetails instanceof DeviceDetails) {
                DeviceInfo deviceInfo = DeviceInfo.newBuilder()
                        .setTypeValue(((DeviceDetails) campaignCriterionDetails).getDeviceType())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setDevice(deviceInfo)
                        .setBidModifier(campaignCriterionDetails.getBidModifier())
                        .setCampaign(campaignCriterionDetails.getCampaignResourceName())
                        .build();

            } else if (campaignCriterionDetails instanceof LocationDetails) {
                LocationInfo locationInfo = LocationInfo.newBuilder()
                        .setGeoTargetConstant(((LocationDetails) campaignCriterionDetails).getGeoTargetingConstant())
                        .build();

                campaignCriterion = CampaignCriterion.newBuilder()
                        .setLocation(locationInfo)
                        .setCampaign(campaignCriterionDetails.getCampaignResourceName())
                        .setNegative(campaignCriterionDetails.isNegative())
                        .setBidModifier(campaignCriterionDetails.getBidModifier())
                        .build();

            } else if (campaignCriterionDetails instanceof ProximityDetails) {
                ProximityInfo.Builder proximityInfoBuilder = ProximityInfo.newBuilder();
                ProximityDetails proximityDetails = ((ProximityDetails) campaignCriterionDetails);

                // if the address details are not empty, create Address info object and set it in the builder
                if (!proximityDetails.getStreetAddress().isEmpty() && !proximityDetails.getPostalCode().isEmpty() &&
                        !proximityDetails.getCityName().isEmpty() && !proximityDetails.getProvinceName().isEmpty() &&
                        !proximityDetails.getCountryCode().isEmpty()) {

                    // build an AddressInfo object and set it in the builder
                    AddressInfo addressInfo = AddressInfo.newBuilder()
                            .setStreetAddress(proximityDetails.getStreetAddress())
                            .setPostalCode(proximityDetails.getPostalCode())
                            .setCityName(proximityDetails.getCityName())
                            //.setProvinceCode(proximityDetails.getProvinceCode())
                            .setProvinceName(proximityDetails.getProvinceName())
                            .setCountryCode(proximityDetails.getCountryCode())
                            .build();

                    proximityInfoBuilder.setAddress(addressInfo);
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

                // create the campaign criterion object from the details
                campaignCriterion = CampaignCriterion.newBuilder()
                        .setCampaign(campaignCriterionDetails.getCampaignResourceName())
                        .setProximity(proximityInfoBuilder.build())
                        .setNegative(campaignCriterionDetails.isNegative())
                        .setBidModifier(campaignCriterionDetails.getBidModifier())
                        .build();
            }

            // if campaignCriterion is null, go to the next in the list
            if (campaignCriterion == null)
                continue;

            // build a create or update campaignCriterionOperation
            CampaignCriterionOperation operation;
            if (shouldCreate) {
                operation = CampaignCriterionOperation.newBuilder()
                        .setCreate(campaignCriterion)
                        .build();
            } else {
                operation = CampaignCriterionOperation.newBuilder()
                        .setUpdate(campaignCriterion)
                        .setUpdateMask(FieldMasks.allSetFieldsOf(campaignCriterion))
                        .build();
            }

            // add CampaignCriterionOperation to list
            campaignCriterionOperationList.add(operation);
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
                .setTargetPartnerSearchNetwork(isTargetingPartnerSearchNetwork)
                .build();
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
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    /**
     * Validate budget details before pushing to Google Ads API
     * If validation fails throw InvalidRequestException
     *
     * @param budgetDetails [BudgetDetails] to be validated
     */
    public void validateCampaignBudgetDetails(BudgetDetails budgetDetails) {
        // validate budget details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    /**
     * Retrieve a single [BudgetDetails] based on a resource name
     *
     * @param resourceName      the resource name of the campaign budget
     * @param budgetDetailsList a list of existing budgetDetails from the client account
     * @return [BudgetDetails] that matched the resource name given
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
     * @return [BudgetDetails] that matched the budget name given
     */
    public BudgetDetails findBudgetDetailsByName(String budgetName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getName().equals(budgetName))
                return budgetDetails;
        }

        return null;
    }
}
