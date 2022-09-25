import json
from re import S
from utils import object_comparator
from utils import service_request_handler
from utils.mock_campaign_objects import MockCampaignObjects
from utils.object_comparator import ObjectComparator
from utils.service_request_handler import ServiceRequestHandler

CUSTOMER_ID = "9059845250"

BASE_URL_CAMPAIGN = "http://localhost:8080/api/v1/" + CUSTOMER_ID + "/campaign/"

CREATE_ENDPOINT = "create"
UPDATE_ENDPOINT = "update"
DELETE_ENDPOINT = "remove"
FETCH_ALL_ENDPOINT = "details"

mock_objects_generator = MockCampaignObjects()
object_comparator = ObjectComparator()
service_request_handler = ServiceRequestHandler()

payload = [mock_objects_generator.getMockCampaign()]

def create_campaign():
    global payload

    writeCampaign(payload)

    resp = service_request_handler.performPostServiceRequest(BASE_URL_CAMPAIGN + CREATE_ENDPOINT, payload=payload)

    status, _ = resp
    
    return status

def fetchAllCampaigns() -> json:
    resp = service_request_handler.performGetServiceRequest(BASE_URL_CAMPAIGN + FETCH_ALL_ENDPOINT)

    _, content = resp

    if (len(content) > 0):
        return json.dumps(content[0])

    return json.dumps("[]")


def writeCampaign(data: str):
    f = open('test.txt', 'a')
    f.write("\n\n")
    f.write(str(data))
    f.close()


status = create_campaign()

if (status == 201):
    j_obj = fetchAllCampaigns()
    
    writeCampaign(str(j_obj))

else:
    print("FAILED")