from random import randint

class RandomResourceDataGenerator:
    random_keyword = ['word', 'on sale', 'brand new', 'keyword washer', 'dryer and appliance', 'hello there']
    random_us_states = ['Indiana', 'California', 'Georgia', 'Florida', 'Ohio']
    random_us_zip_code = ['46805', '60066', '10101', '20019']
    random_us_cities = ['Fort Wayne', 'Boston', 'Los Angeles', 'New York']

    def __init__(self):
        print("")

    def get_random_campaign_status(self):
        return randint(2,4)

    def get_random_positive_geo_target(self):
        return randint(5,7)
    
    def get_random_negative_geo_target(self):
        return randint(4,5)

    def get_random_true_false(self):
        return randint(0,1)

    def get_random_campaign_name(self):
        random_int = randint(1000, 5000)
        return "Test Campaign " + str(random_int)

    def get_random_date(self, is_start_date: bool):
        rand_date = "2022-10-20"

        if (is_start_date):
            yr = randint(23,29)
        else:
            yr = randint(30, 35)
        
        month = randint(1,12)
        day = randint(1,30)

        if (month < 10):
            month = '0' + str(month)

        if (day < 10):
            day = '0' + str(day)

        rand_date = rand_date.replace('22-', str(yr) + "-")
        rand_date = rand_date.replace('-10-', "-" + str(month) + "-")
        rand_date = rand_date.replace('-20', "-" + str(day))

        return rand_date

    def get_random_keyword_text(self):
        rnd = randint(0, len(self.random_keyword) - 1)
        return self.random_keyword[rnd]

    def get_random_us_state(self):
        rnd = randint(0, len(self.random_us_states) - 1)
        return self.random_us_states[rnd]

    def get_random_postal_code(self):
        rnd = randint(0, len(self.random_us_zip_code) - 1)
        return self.random_us_zip_code[rnd]

    def get_random_us_city(self):
        rnd = randint(0, len(self.random_us_cities) - 1)
        return self.random_us_cities[rnd]