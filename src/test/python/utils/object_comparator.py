
import json


class ObjectComparator:
    def __init__(self):
        print("")
    
    """
    Compare the base details of two campaign_details dicts. 

    Args:
        expected_campaign_dict (dict): The dictionary of campaign details expected
        actual_campaign_dict (dict): The actual dictionary of campaign details 

    Returns:
        bool: True is both dicts are equal and False if they are not equal
    """
    def compareCampaignObjects(self, expected_campaign:str, actual_campaign:str) -> bool:
        expected_campaign_dict = json.loads(expected_campaign)
        actual_campaign_dict = json.loads(actual_campaign)

        if (expected_campaign_dict['campaignName'] != actual_campaign_dict['campaignName']): 
            return False;

        if (expected_campaign_dict['status'] != actual_campaign_dict['status']):
            return False;
        
        if (expected_campaign_dict['advertisingChannelType'] != actual_campaign_dict['advertisingChannelType']): 
            return False;

        if (expected_campaign_dict['positiveGeoTargetType'] != actual_campaign_dict['positiveGeoTargetType']):
            return False;
        
        if (expected_campaign_dict['negativeGeoTargetType'] != actual_campaign_dict['negativeGeoTargetType']): 
            return False;

        if (expected_campaign_dict['enhancedCpcEnabled'] != actual_campaign_dict['enhancedCpcEnabled']):
            return False;
        
        if (expected_campaign_dict['advertisingChannelType'] != actual_campaign_dict['advertisingChannelType']): 
            return False;

        if (expected_campaign_dict['startDate'] != actual_campaign_dict['startDate']):
            return False; 

        if (expected_campaign_dict['endDate'] != actual_campaign_dict['endDate']): 
            return False;

        if (expected_campaign_dict['budgetResourceName'] != actual_campaign_dict['budgetResourceName']):
            return False;
        
        if (expected_campaign_dict['targetingGoogleSearchNetwork'] != actual_campaign_dict['targetingGoogleSearchNetwork']): 
            return False;

        if (expected_campaign_dict['targetingSearchNetwork'] != actual_campaign_dict['targetingSearchNetwork']):
            return False;
        
        if (expected_campaign_dict['targetingContentNetwork'] != actual_campaign_dict['targetingContentNetwork']): 
            return False;        

        return True;

    def compareCriterionLists(self, expected_criterion: str, actual_criterion: str) -> bool:
        expected_criterion_list = json.loads(expected_criterion)
        actual_criterion_list = json.loads(actual_criterion)
        
        # track the number of comparisons resulting in the same object
        success_counter = 0
        
        # For each criterion object in expected_criterion_list try to find a match in actual_criterion_list
        # if a match is found add 1 to the success_counter and move on
        for i in range (0, (len(expected_criterion_list))):
            for j in range(0, len(actual_criterion_list)):
                # if both dicts are of the same 'type' compare them
                if (expected_criterion_list[i]['type'] == actual_criterion_list[j]['type']):
                    outcome = self._compareCriterionObjects(expected_criterion_list[i], actual_criterion_list[j])

                    if (outcome == True):
                        success_counter = success_counter + 1;
                        j = len(actual_criterion_list) # break the second for loop
        
        # Return True if expected_criterion_list matches actual_criterion_list in size 
        # and there is a 1-1 match between both lists
        return success_counter == len(expected_criterion_list) == len(actual_criterion_list)

    def _compareCriterionObjects(self, expected_criterion_dict: dict, actual_criterion_dict: dict) -> bool:
        for key in expected_criterion_dict:
            if (key not in actual_criterion_dict or actual_criterion_dict[key] != expected_criterion_dict[key]):
                return False

        return True;

