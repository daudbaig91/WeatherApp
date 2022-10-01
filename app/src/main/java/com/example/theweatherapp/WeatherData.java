package com.example.theweatherapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherData {

    String city;
    String temperature;
    String weather;
    String feelLike;
    String minTemp;
    String maxTemp;
    String date;
    String weatherCode;

    public WeatherData() {

    }

    public void converData() {
        this.city = city;
        this.temperature = KtoC(this.temperature);
        this.weather = weather;
        this.feelLike = KtoC(this.feelLike);
        this.minTemp = KtoC(this.minTemp);
        this.maxTemp = KtoC(this.maxTemp);
    }

    public String KtoC(String k){

        return String.valueOf((int) Math.round((Double.parseDouble(k))));
    }


    public String DateConverter(String time) throws ParseException {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = input.parse(time);

        SimpleDateFormat output = new SimpleDateFormat("EEE, dd MMM yyyy");
        this.date = output.format(dateValue);
        return this.date;

    }
}
