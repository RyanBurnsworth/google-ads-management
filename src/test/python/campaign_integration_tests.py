import json
from random import randint
import time
import requests
import colorama
from colorama import Fore

first_campaign_in_account_dict = {"campaignId": 17294642838, "campaignName": "Leads-Search-1", "campaignResourceName": "customers/9059845250/campaigns/17294642838", "status": "REMOVED", "advertisingChannelType": "SEARCH", "positiveGeoTargetType": 5, "negativeGeoTargetType": 5, "startDate": "2022-05-18", "endDate": "2037-12-30", "budgetResourceName": "customers/9059845250/campaignBudgets/10928544134", "budgetDetails": {"budgetId": 10928544134, "name": "Leads-Search-1", "resourceName": "customers/9059845250/campaignBudgets/10928544134", "deliveryMethod": 2, "dailyBudgetAmount": 5000, "status": 3, "shared": False}, "targetingSearchNetwork": True, "enhancedCpcEnabled": False, "targetingPartnerSearchNetwork": False, "targetingContentNetwork": False}

TEST_CLIENT_ACCOUNT_ID = "9059845250"
INVALID_CLIENT_ACCOUNT_ID = "902834928"

BASE_URL = "http://localhost:8080/api/v1/"

colorama.init()

"""
    Fetch all the campaigns in the test client account

    Expects status code 200
    Expects response body length > 0
    Expects first campaignDetails object to be set
"""
def test_successful_get_all_campaign_details():
    
    print(Fore.YELLOW + "[*] Testing Successful Get All Campaign Details")
    
    # Make a GET request to /campaign/details endpoint
    url = BASE_URL + TEST_CLIENT_ACCOUNT_ID + "/campaign/details"
    resp = requests.get(url)

    # convert response body to json array
    json_response_body = resp.json()

    # get the number of campaigns retrieved from endpoint
    response_len = len(json_response_body)

    # get the first campaign details object from response
    campaign_details = json_response_body[0]
    
    if (resp.status_code == 200 
        and response_len > 0
        and campaign_details["campaignName"] == first_campaign_in_account_dict["campaignName"]
        and campaign_details["campaignId"] == first_campaign_in_account_dict["campaignId"]
        and campaign_details["campaignResourceName"] == first_campaign_in_account_dict["campaignResourceName"]
        and campaign_details["status"] == first_campaign_in_account_dict["status"]
        and campaign_details["advertisingChannelType"] == first_campaign_in_account_dict["advertisingChannelType"]
        and campaign_details["positiveGeoTargetType"] == first_campaign_in_account_dict["positiveGeoTargetType"]
        and campaign_details["negativeGeoTargetType"] == first_campaign_in_account_dict["negativeGeoTargetType"]
        and campaign_details["enhancedCpcEnabled"] == first_campaign_in_account_dict["enhancedCpcEnabled"]
        and campaign_details["startDate"] == first_campaign_in_account_dict["startDate"]
        and campaign_details["endDate"] == first_campaign_in_account_dict["endDate"]
        and campaign_details["targetingSearchNetwork"] == first_campaign_in_account_dict["targetingSearchNetwork"]
        and campaign_details["targetingContentNetwork"] == first_campaign_in_account_dict["targetingContentNetwork"]
        and campaign_details["targetingPartnerSearchNetwork"] == first_campaign_in_account_dict["targetingPartnerSearchNetwork"]
        and campaign_details["campaignId"] == first_campaign_in_account_dict["campaignId"]
        and campaign_details["budgetResourceName"] == first_campaign_in_account_dict["budgetResourceName"]
        and campaign_details["budgetDetails"]["budgetId"] == first_campaign_in_account_dict["budgetDetails"]["budgetId"]
        and campaign_details["budgetDetails"]["name"] == first_campaign_in_account_dict["budgetDetails"]["name"]
        and campaign_details["budgetDetails"]["resourceName"] == first_campaign_in_account_dict["budgetDetails"]["resourceName"]
        and campaign_details["budgetDetails"]["deliveryMethod"] == first_campaign_in_account_dict["budgetDetails"]["deliveryMethod"]
        and campaign_details["budgetDetails"]["shared"] == first_campaign_in_account_dict["budgetDetails"]["shared"]
        and campaign_details["budgetDetails"]["dailyBudgetAmount"] == first_campaign_in_account_dict["budgetDetails"]["dailyBudgetAmount"]
        and campaign_details["budgetDetails"]["status"] == first_campaign_in_account_dict["budgetDetails"]["status"]
    ):
        print(Fore.GREEN + 'SUCCESS')
    else:
        print(Fore.RED + 'FAILED!')

"""
    Attempt t fetch all the campaigns in the test client account
    with an invalid customer ID

    Expect status code 400
"""
def test_failed_get_all_campaign_details_invalid_customer_id():
    print(Fore.YELLOW + "[*] Testing Failed Get All Campaign Details -- Invalid Customer ID")
    url = BASE_URL + INVALID_CLIENT_ACCOUNT_ID + "/campaign/details"
    req = requests.get(url)
    if req.status_code == 400:
        print(Fore.GREEN + 'SUCCESS')
    else:
        print(Fore.RED + 'FAILED!')

def test_create_campaign_on_test_account():
    print(Fore.YELLOW + "[*] Testing Creating Campaign on Test Account")
    campaign_details_list = [
        {
            "campaignName": "Test Campaign Creation " + str(randint(100,50000)),
            "status": "ENABLED",
            "advertisingChannelType": "SEARCH",
            "positiveGeoTargetType": 7,
            "negativeGeoTargetType": 5,
            "enhancedCpcEnabled": False,
            "startDate": "2023-09-02",
            "endDate": "2037-09-01",
            "budgetResourceName": "",
            "targetingPartnerSearchNetwork": False,
            "targetingSearchNetwork": True,
            "targetingContentNetwork": True,
            "budgetDetails": {
                "name": "Budget "  + str(randint(100,50000)),
                "resourceName" : "",
                "deliveryMethod": 2,
                "isShared": False,
                "dailyBudgetAmount": 345,
                "status": 2
            }
        },
        {
            "campaignName": "Test Campaign Creation 2 " + str(randint(100,50000)),
            "status": "PAUSED",
            "advertisingChannelType": "SEARCH",
            "positiveGeoTargetType": 7,
            "negativeGeoTargetType": 5,
            "enhancedCpcEnabled": False,
            "startDate": "2025-09-02",
            "endDate": "2037-09-01",
            "budgetResourceName": "",
            "targetingPartnerSearchNetwork": False,
            "targetingSearchNetwork": True,
            "targetingContentNetwork": False,
            "budgetDetails": {
                "name": "Budget "  + str(randint(100,50000)),
                "resourceName" : "",
                "deliveryMethod": 2,
                "isShared": False,
                "dailyBudgetAmount": 567,
                "status": 2
            }
        }
    ]

    url = BASE_URL + TEST_CLIENT_ACCOUNT_ID + "/campaign/create"
    data = json.dumps(campaign_details_list)
    headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

    resp = requests.post(url, data=data, headers=headers)

    if (resp.status_code == 201):
        print(Fore.GREEN + 'SUCCESS')
    else:
        print("Status Code: ", resp.status_code)
        print("Response Body: ", resp.content)
        print(Fore.RED + 'FAILED!')


test_successful_get_all_campaign_details()

test_failed_get_all_campaign_details_invalid_customer_id()

test_create_campaign_on_test_account()