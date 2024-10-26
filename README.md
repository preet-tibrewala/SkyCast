# SkyCast

SkyCast is an Android application developed in Android Studio using Kotlin. It provides weather forecasts by utilizing data from the OpenWeather API. The app displays current weather conditions, 5-day forecasts, and other meteorological data for a specified location

## Features

- Current weather updates
- 5-day weather forecast
- Detailed weather information including temperature, humidity, and wind speed
- User-friendly interface

## Prerequisites

- Android Studio installed on your machine
- An OpenWeather API key

## Getting Started

To clone and run SkyCast on your local machine, follow these steps:

### Step 1: Obtain Your OpenWeather API Key

- Navigate to the [OpenWeather](https://openweathermap.org/api) API website and sign up for an API key if you haven't already.
- Once you have your API key, proceed to the next step to clone this repo.

### Step 2: Clone the Repository

- Open a terminal or command prompt.
- Navigate to the directory where you want to clone the project.
- Run the following command:

```bash
git clone https://github.com/preet-tibrewala/SkyCast.git
```

### Step 3: Configure the OpenWeather API Key

To keep sensitive information like API keys out of source control, follow these steps to add your OpenWeather API key:

- Locate your gradle.properties file in your GRADLE_USER_HOME directory:
  ** MacOS/Linux: `$HOME/.gradle/`
  ** Windows: `%USER_HOME%/.gradle/`

- If the gradle.properties file does not exist, create it.

- Add the following line to the gradle.properties file, replacing `<put_your_own_OpenWeather_API_key_here>` with your actual API key:
  ```text
  OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
  ```

### Step 4: Build and Run SkyCast

- Open the cloned project in Android Studio.
- Ensure you have an emulator or a physical Android device set up for debugging.
- Build the project (Android Studio will automatically fetch your API key from the gradle.properties file, if set up correctly).
- Run the app on your chosen device/emulator.

### Contact

Preet Dhiren Tibrewala
[LinkedIn](https://www.linkedin.com/in/preettibrewala/)
