# "weather-app" is a learning project
Web-app for viewing the current weather.
User can register and add to collection one or more locations,
after this main page of the app shows list of locations
with their current weather.

## Main goals
* Use cookies and sessions without any frameworks
* Work with external API

## Features
Work with users:
* Sign Up
* Login
* Logout

Work with locations:
* Search
* Add into collection
* Review list of locations, the weather is displayed for each location
* Delete from collection

## How to tweak this project for your own uses
1. Create account [*here*](https://openweathermap.org/)
   - Generate your API key
2. Clone this project
   - copy your API key to `/src/main/resources/application.properties`
3. Install [docker](https://www.docker.com/get-started/)
4. Go into your project directory
   - open your CLI 
   - start project by running `docker-compose up -d`
5. Enter http://localhost:8080/weather-app/ in a browser to see the application running.
   - if this doesn't resolve, you can also try http://127.0.0.1:8080/weather-app/
   - you should see

![](/src/main/resources/app_example.png)

> [!NOTE] 
>  stop project by running `docker-compose down`
