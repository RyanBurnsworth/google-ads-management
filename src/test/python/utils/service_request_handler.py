import json
import requests

class ServiceRequestHandler:

    def __init__(self):
        print("")

    """
    Perform a GET http request

    Args:
        url (str): the URL being targeted with the GET request

    Returns:
        tuple: a tuple containing the status_code and the response body content
    """
    def performGetServiceRequest(self, url: str) -> tuple:
        resp = requests.get(url)
        return (resp.status_code, resp.json())

    """
    Perform a POST http request
    
    Args:
        url (str): the URL being targeted with the POST request
        payload (list): the data payload to be sent with the request

    Returns:
        tuple: a tuple containing the status_code and the response body content
    """
    def performPostServiceRequest(self, url: str, payload: list) -> tuple:
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        payload_json = json.dumps(payload)

        resp = requests.post(url, data=payload_json, headers=headers)
        return (resp.status_code, resp.content)

handler = ServiceRequestHandler()
