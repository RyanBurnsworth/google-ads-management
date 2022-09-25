from random import randint
from utils.random_resource_data_generator import RandomResourceDataGenerator

class MockCampaignObjects:
    _res_gen: RandomResourceDataGenerator

    def __init__(self):
        self._res_gen = RandomResourceDataGenerator()

    def getMockCampaign(self):
        return {
                    "campaignName": self._res_gen.get_random_campaign_name(),
                    "status": self._res_gen.get_random_campaign_status(),
                    "advertisingChannelType": 2,
                    "positiveGeoTargetType": self._res_gen.get_random_positive_geo_target(),
                    "negativeGeoTargetType": self._res_gen.get_random_negative_geo_target(),
                    "enhancedCpcEnabled": self._res_gen.get_random_true_false(),
                    "startDate": self._res_gen.get_random_date(is_start_date=True),
                    "endDate": self._res_gen.get_random_date(is_start_date=False),
                    "budgetResourceName": "",
                    "targetingGoogleSearchNetwork": self._res_gen.get_random_true_false(),
                    "targetingSearchNetwork": self._res_gen.get_random_true_false(),
                    "targetingContentNetwork": self._res_gen.get_random_true_false(),
                    "campaignCriteriaList": [
                        {
                            "type": "keyword",
                            "keywordText": self._res_gen.get_random_keyword_text(),
                            "keywordMatchType": randint(2,4)
                        },
                        {
                            "type": "ad_schedule",
                            "criterionType": 9,
                            "status": randint(2,3),
                            "bidModifier": (randint(0, 10) * 1.0),
                            "dayOfWeek": randint(2,8),
                            "startHour": randint(0,6),
                            "endHour": randint(7,23),
                            "startMinute": randint(2,5),
                            "endMinute": randint(2,5)
                        },
                        {
                            "type": "device",
                            "criterionType": 20,
                            "status": randint(2,3),
                            "bidModifier": (randint(1, 10) * 1.0),
                            "deviceType": randint(2,4)
                        },
                        {
                            "type": "language",
                            "criterionType": 20,
                            "status": randint(2,3),
                            "languageCode": "languageConstants/1000"
                        },
                        {
                            "type": "proximity",
                            "criterionType": 17,
                            "status": randint(2,3),
                            "bidModifier": (randint(0, 10) * 1.0),
                            "cityName": self._res_gen.get_random_us_city(),
                            "countryCode": "US",
                            "postalCode": self._res_gen.get_random_postal_code(),
                            "provinceName": self._res_gen.get_random_us_state(),
                            "streetAddress": str(randint(10,4000)) + " West Broadway Avenue",
                            "radius": randint(1,25),
                            "radiusUnits": 2
                        },
                        {
                            "type": "proximity",
                            "criterionType": 17,
                            "status": randint(2,3),
                            "bidModifier":  (randint(0, 10) * 1.0),
                            "longitude": (randint(-180, 180) * 1.0),
                            "latitude": (randint(-90, 90) * 1.0),
                            "radius": randint(1,25),
                            "radiusUnits": 2
                        },
                        {
                            "type": "location",
                            "criterionType": 0,
                            "status": randint(2,3),
                            "bidModifier": (randint(0, 10) * 1.0),
                            "locale": "en",
                            "countryCode": "US",
                            "location": self._res_gen.get_random_us_state()
                        }
                    ],
                    "budgetDetails": {
                        "deliveryMethod": 2,
                        "isShared": randint(0,1),
                        "dailyBudgetAmount": randint(10, 2000),
                        "status": 2
                    }
            }
