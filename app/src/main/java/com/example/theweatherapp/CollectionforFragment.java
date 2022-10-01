package com.example.theweatherapp;

public class CollectionforFragment {
    public WeatherData getWeekWeather() {
        return dataWeather;
    }

    public void setWeekWeather(WeatherData dataWeather) {
        this.dataWeather = dataWeather;
    }

    public WeatherCode getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(WeatherCode weatherCode) {
        this.weatherCode = weatherCode;
    }

    WeatherData dataWeather;
    WeatherCode weatherCode;
    CollectionforFragment(WeatherData weatherData,WeatherCode weatherCode){
        this.weatherCode = weatherCode;
        this.dataWeather = weatherData;
    }
}
