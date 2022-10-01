package com.example.theweatherapp;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeekListUpdater {

    WeekListUpdater(View view, Context context, ArrayList<WeekWeather> listWeekTimes ){
        RecyclerView recyclerViewWeek = view.findViewById(R.id.recyclerView2);
        recyclerViewWeek.setHasFixedSize(true);
        recyclerViewWeek.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewWeek.setAdapter(new WeekWeatherAdapter(context, listWeekTimes,view));
    }
}
