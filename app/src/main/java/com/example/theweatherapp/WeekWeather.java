package com.example.theweatherapp;

import com.github.mikephil.charting.data.Entry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeekWeather {
    int x = 0;
    int weatherImage;
    String daysWeather;
    String porcentageRain;
    String maxTemp;
    String minTemp;
    ArrayList<Entry> hour = new ArrayList<>();

    public WeekWeather() {
    }


    public String DatetoDay(String date) throws ParseException {


        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = format1.parse(date);
        DateFormat format2 = new SimpleDateFormat("EEEE");
        this.daysWeather =  format2.format(dt1);
        return this.daysWeather;
    }
}
