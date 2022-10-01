package com.example.theweatherapp;

import android.widget.ImageView;
import android.widget.TextView;

public class DayWeather {
    Integer weatherImage;
    String timeText;
    String tempText;


    public DayWeather(Integer weatherImage, String timeText, String tempText) {

        this.weatherImage = weatherImage;
        this.timeText = timeText;
        this.tempText = tempText;
    }
}
