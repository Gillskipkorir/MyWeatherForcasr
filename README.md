# My Weather App

The Api I used are
** https://api.openweathermap.org/data/2.5/weather

## Encountered Issues With Api

When trying to The Json results of this endpoint:
** https://api.openweathermap.org/data/2.5/forecast/daily?q=nairobi&cnt=2&appid=358c550eaa931d17bbe2602e25ec8d72

I get the error below despite using the api Key working on all other endpoints and as a result making
me unable to fetch my daily weather forecast

````json
    {
    cod: 401,
    message: "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."
    }
````json

