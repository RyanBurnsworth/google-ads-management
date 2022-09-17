import json
from random import randint
import requests
import colorama
from colorama import Fore

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
    # expected first campaign details object in the response list
    first_campaign_in_account_dict = {"campaignId": 17294642838, "campaignName": "Leads-Search-1", "campaignResourceName": "customers/9059845250/campaigns/17294642838", "status": "REMOVED", "advertisingChannelType": "SEARCH", "positiveGeoTargetType": 5, "negativeGeoTargetType": 5, "startDate": "2022-05-18", "endDate": "2037-12-30", "budgetResourceName": "customers/9059845250/campaignBudgets/10928544134", "budgetDetails": {"budgetId": 10928544134, "name": "Leads-Search-1", "resourceName": "customers/9059845250/campaignBudgets/10928544134", "deliveryMethod": 2, "dailyBudgetAmount": 5000, "status": 3, "shared": False}, "targetingSearchNetwork": True, "enhancedCpcEnabled": False, "targetingPartnerSearchNetwork": False, "targetingContentNetwork": False}

    print(Fore.YELLOW + "[*] Testing Successful Get All Campaign Details")
    
    # Make a GET request to /campaign/details endpoint
    url = BASE_URL + TEST_CLIENT_ACCOUNT_ID + "/campaign/details"
    resp = requests.get(url)

    # convert response body to json array
    json_response_body = resp.json()

    # get the number of campaigns retrieved from endpoint
    response_len = len(json_response_body)

    # get the first campaign details object in the list from response
    campaign_details = json_response_body[0]
    
    # verify the status code is 200, the response contains multiple campaign details and the first campaign details is as expected
    if (resp.status_code == 200 and response_len > 0 
        and _compare_campaign_details_objects(campaign_details, first_campaign_in_account_dict)):
        print(Fore.GREEN + 'SUCCESS')
    else:
        print(Fore.RED + 'FAILED!')

"""
    Fetch all the campaigns in the test client account with an invalid customer ID

    Expect status code 400
"""
def test_failed_get_all_campaign_details_invalid_customer_id():
    print(Fore.YELLOW + "[*] Testing Failed Get All Campaign Details -- Invalid Customer ID")

    # perform a GET request on the campaign/details endpoint using an invalid customer id
    url = BASE_URL + INVALID_CLIENT_ACCOUNT_ID + "/campaign/details"
    req = requests.get(url)

    # verify that the status_code is 400
    if req.status_code == 400:
        print(Fore.GREEN + 'SUCCESS')
    else:
        print(Fore.RED + 'FAILED!')

"""
    Test fetching a single campaign details object

    Expects campaign details object to equal the mock campaign details object
"""
def test_successful_get_single_campaign_details():
    campaign_in_account_dict = {"campaignId": 17294642838, "campaignName": "Leads-Search-1", "campaignResourceName": "customers/9059845250/campaigns/17294642838", "status": "REMOVED", "advertisingChannelType": "SEARCH", "positiveGeoTargetType": 5, "negativeGeoTargetType": 5, "startDate": "2022-05-18", "endDate": "2037-12-30", "budgetResourceName": "customers/9059845250/campaignBudgets/10928544134", "budgetDetails": {"budgetId": 10928544134, "name": "Leads-Search-1", "resourceName": "customers/9059845250/campaignBudgets/10928544134", "deliveryMethod": 2, "dailyBudgetAmount": 5000, "status": 3, "shared": False}, "targetingSearchNetwork": True, "enhancedCpcEnabled": False, "targetingPartnerSearchNetwork": False, "targetingContentNetwork": False}
    print(Fore.YELLOW + "[*] Testing Successful Get Single Campaign Details")

    # Fetch the campaign details object from the test account
    campaign_details = _get_single_campaign_details("Leads-Search-1")
    
    # verify the campaign details fetched matches what is expected
    if (_compare_campaign_details_objects(campaign_details, campaign_in_account_dict)):
        print(Fore.GREEN + 'SUCCESS')
    else:
        print(Fore.RED + 'FAILED!')

"""
    Create two campaigns on the client account

    Expects status code 201
"""
def test_create_campaigns_on_test_account():
    print(Fore.YELLOW + "[*] Testing Creating Campaign on Test Account")

    campaign_details_1_name = "Test Campaign 1 " + str(randint(100,500000))
    campaign_details_2_name = "Test Campaign 2 " + str(randint(100,500000))

    campaign_details_1 = {
            "campaignName": campaign_details_1_name,
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
                "name": campaign_details_1_name,
                "resourceName" : "",
                "deliveryMethod": 2,
                "isShared": False,
                "dailyBudgetAmount": 345,
                "status": 2
            }
        }

    campaign_details_2 = {
            "campaignName": campaign_details_2_name,
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
                "name": campaign_details_2_name,
                "resourceName" : "",
                "deliveryMethod": 2,
                "isShared": False,
                "dailyBudgetAmount": 567,
                "status": 2
            }
        }

    # add the two campaign details dicts to a list
    campaign_details_list = [campaign_details_1, campaign_details_2]

    # perform a POST request on the /campaign/create endpoint to create the 2 campaigns
    url = BASE_URL + TEST_CLIENT_ACCOUNT_ID + "/campaign/create"
    data = json.dumps(campaign_details_list)
    headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

    # perform the request to the endpoint
    resp = requests.post(url, data=data, headers=headers)

    # verify the status code is 201
    if (resp.status_code != 201):
        print("Status Code: ", resp.status_code)
        print("Response Body: ", resp.content)
        print(Fore.RED + 'FAILED!')
        return

    # fetch the newly created campaign details 1 from the test account
    actual_campaign_details_1 = _get_single_campaign_details(campaign_details_1_name)

    # fetch the newly created campaign details 2 from the test account
    actual_campaign_details_2 = _get_single_campaign_details(campaign_details_2_name)

    # verify both the campaigns are successfully created compare actual_campaign_details_1 to the campaign details we provided with campaign_details_1
    if _compare_campaign_details_objects(actual_campaign_details_1, campaign_details_1) and _compare_campaign_details_objects(actual_campaign_details_2, campaign_details_2):
        print(Fore.GREEN + 'SUCCESS')
    else:
        _find_exact_comparison_failure_reason(actual_campaign_details_1, campaign_details_1)
        print(Fore.RED + 'FAILED!')

"""
    Compare two campaign_details objects. 
    
    Return true if both are equal, false otherwise
"""
def _compare_campaign_details_objects(actual_campaign_details, expected_campaign_details):
    if(actual_campaign_details["campaignName"] == expected_campaign_details["campaignName"]
        and actual_campaign_details["status"] == expected_campaign_details["status"]
        and actual_campaign_details["advertisingChannelType"] == expected_campaign_details["advertisingChannelType"]
        and actual_campaign_details["positiveGeoTargetType"] == expected_campaign_details["positiveGeoTargetType"]
        and actual_campaign_details["negativeGeoTargetType"] == expected_campaign_details["negativeGeoTargetType"]
        and actual_campaign_details["enhancedCpcEnabled"] == expected_campaign_details["enhancedCpcEnabled"]
        and actual_campaign_details["startDate"] == expected_campaign_details["startDate"]
        and actual_campaign_details["endDate"] == expected_campaign_details["endDate"]
        and actual_campaign_details["targetingSearchNetwork"] == expected_campaign_details["targetingSearchNetwork"]
        and actual_campaign_details["targetingContentNetwork"] == expected_campaign_details["targetingContentNetwork"]
        and actual_campaign_details["targetingPartnerSearchNetwork"] == expected_campaign_details["targetingPartnerSearchNetwork"]
        and actual_campaign_details["budgetDetails"]["name"] == expected_campaign_details["budgetDetails"]["name"]
        and actual_campaign_details["budgetDetails"]["deliveryMethod"] == expected_campaign_details["budgetDetails"]["deliveryMethod"]
        and actual_campaign_details["budgetDetails"]["dailyBudgetAmount"] == expected_campaign_details["budgetDetails"]["dailyBudgetAmount"]
        and actual_campaign_details["budgetDetails"]["status"] == expected_campaign_details["budgetDetails"]["status"]):
        return True
    else:
        return False

"""
    Print out the exact failure reason for a comparison between two campaign details objects
"""
def _find_exact_comparison_failure_reason(actual_campaign_details, expected_campaign_details):
    if (actual_campaign_details["campaignName"] != expected_campaign_details["campaignName"]):
        print("[!] Campaign Names DO NOT MATCH!")
    if (actual_campaign_details["status"] != expected_campaign_details["status"]):
        print("[!] Campaign statuses DO NOT MATCH!")
    if (actual_campaign_details["advertisingChannelType"] != expected_campaign_details["advertisingChannelType"]):
        print("[!] Campaign Advertising Channel Types DO NOT MATCH!")
    if (actual_campaign_details["positiveGeoTargetType"] != expected_campaign_details["positiveGeoTargetType"]):
        print("[!] Campaign Positive Geo Target Types DO NOT MATCH!")
    if (actual_campaign_details["negativeGeoTargetType"] != expected_campaign_details["negativeGeoTargetType"]):
        print("[!] Campaign Negative Geo Target Types DO NOT MATCH!")
    if (actual_campaign_details["enhancedCpcEnabled"] != expected_campaign_details["enhancedCpcEnabled"]):
        print("[!] Campaign enhanced CPC DO NOT MATCH!")
    if (actual_campaign_details["startDate"] != expected_campaign_details["startDate"]):
        print("[!] Campaign Start Date DO NOT MATCH!")
    if (actual_campaign_details["endDate"] != expected_campaign_details["endDate"]):
        print("[!] Campaign End Date DO NOT MATCH!")
    if (actual_campaign_details["targetingSearchNetwork"] != expected_campaign_details["targetingSearchNetwork"]):
        print("[!] Campaign Search Network Targeting DO NOT MATCH!")
    if (actual_campaign_details["targetingContentNetwork"] != expected_campaign_details["targetingContentNetwork"]):
        print("[!] Campaign Names DO NOT MATCH!")
    if (actual_campaign_details["targetingPartnerSearchNetwork"] != expected_campaign_details["targetingPartnerSearchNetwork"]):
        print("[!] Campaign Names DO NOT MATCH!")
    if (actual_campaign_details["budgetDetails"]["name"] != expected_campaign_details["budgetDetails"]["name"]):
        print("[!] Campaign Budget Names DO NOT MATCH!")
    if (actual_campaign_details["budgetDetails"]["deliveryMethod"]  != expected_campaign_details["budgetDetails"]["deliveryMethod"] ):
        print("[!] Campaign Budget Delivery Methods DO NOT MATCH!")
    if (actual_campaign_details["budgetDetails"]["dailyBudgetAmount"] != expected_campaign_details["budgetDetails"]["dailyBudgetAmount"]):
        print("[!] Campaign Budget Amounts DO NOT MATCH!")
    if (actual_campaign_details["budgetDetails"]["status"] != expected_campaign_details["budgetDetails"]["status"]):
        print("[!] Campaign Budget Statuses DO NOT MATCH!")
    
"""
    Fetches a single campaign details object based on its name and budget name from the test account

    Returns a single campaign details object
"""
def _get_single_campaign_details(campaignName):
    # Make a GET request to /campaign/{campaignName}/details endpoint
    url = BASE_URL + TEST_CLIENT_ACCOUNT_ID + "/campaign/" + campaignName + "/details"
    resp = requests.get(url)

    # convert response body to json array
    json_response_body = resp.json()
    return json_response_body

test_successful_get_all_campaign_details()

test_failed_get_all_campaign_details_invalid_customer_id()

test_create_campaigns_on_test_account()

test_successful_get_single_campaign_details()
