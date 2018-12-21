# Prometheus
Prometheus is an application to predict future by applying advanced artificial intelligence algorithms. You are hired as a new Android developer to maintain and improve it.

## Android Technical Test

We have created a small technical test for you to showcase how you work. Your task will be to create a small Android Application that should request weather for user requests locations and show that information to the user.

### Challenges

* Implement cities list and weather detail screen with UX provided.
* User could request a report for any location (by pointing location on the map).

#### Bonus Points

* Make a persistent data layer.
* Style example with provided UI.
* Implement weather feed/history screen.
* Implement change of units and language

#### API

The following endpoint `https://prometheus-api.draewil.net/[APIKEY]/[latitude],[longitude]?[additional params - optional]` responds with JSON data. The API requires you to specify a API key in URI.

`Authorization API key: c6987ba1fcc7450aea9cff041bb42825`

#### GET https://prometheus-api.draewil.net/c6987ba1fcc7450aea9cff041bb42825/51.500334,-0.085013

```json 
[
  {
"latitude": 51.500334,
"longitude": -0.085013,
"timezone": "Europe/London",
"offset": 1,
"currently": {
"time": 1495028635,
"summary": "Light Rain",
"icon": "rain",
"nearestStormDistance": 0,
"precipIntensity": 0.0206,
"precipIntensityError": 0.0119,
"precipProbability": 0.98,
"precipType": "rain",
"temperature": 64.18,
"apparentTemperature": 64.18,
"dewPoint": 60.16,
"humidity": 0.87,
"windSpeed": 2.96,
"windBearing": 295,
"visibility": 5.73,
"cloudCover": 0.62,
"pressure": 1014.25,
"ozone": 302.9
},
"minutely": {
"summary": "Light rain stopping in 12 min., starting again 35 min. later.",
"icon": "rain",
"data": [
{
"time": 1495028580,
"precipIntensity": 0.027,
"precipIntensityError": 0.011,
"precipProbability": 0.99,
"precipType": "rain"
},
{
"time": 1495028640,
"precipIntensity": 0.02,
"precipIntensityError": 0.012,
"precipProbability": 0.98,
"precipType": "rain"
},
...
]
```

You can preview the request in the terminal.
> $ curl https://prometheus-api.draewil.net/c6987ba1fcc7450aea9cff041bb42825/51.500334,-0.085013?lang=fr&units=si

Additional params:

You can specify language parameter:
lang=[language]
where values are:
en - English
fr - French
es - Spanish
de - German

You can specify units parameter:
units=[units]
where values are:
auto - selects units based on location
si - si units
us - imperial units


### UI

* https://raw.githubusercontent.com/DraewilTech/interview-assets/master/prometheus-android-mainscreen.png
* https://raw.githubusercontent.com/DraewilTech/interview-assets/master/prometheus-android-addlocation.png

### Review

We value simplicity and quality over feature-completeness. We do take into consideration your experience level. The goal of this code sample is to help us identify what you consider production-ready code. 
The aspects of your code we will assess include:

* **Architecture**: how clean is the separation between layers
* **Correctness**: does the application do what was asked? 
* **Code quality**: is the code simple, easy to understand, and maintainable? Are there any code smells or other red flags? Does object-oriented code follows principles such as the single responsibility principle? Is the coding style consistent with the language's guidelines? Is it consistent throughout the codebase?
* **Technical choices**: do choices of libraries, architecture etc. seem appropriate for the chosen application?


### Aditional Information

We understand that you've probably got a full-time job and a personal life so doing the above test could be definitely finished in a couple of hours.

If you have any questions, email us and we can add it to this readme.

Good luck!