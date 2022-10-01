package com.example.theweatherapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



class DayWeatherAdapter extends RecyclerView.Adapter <DayWeatherAdapter.ViewHolder>{

    private ArrayList<DayWeather> dayWeather;
    private Context context;

    DayWeatherAdapter(Context context, ArrayList<DayWeather> dayWeather) {
        super();
        this.context = context;
        this.dayWeather = dayWeather;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_times, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.imageWeather.setImageResource(dayWeather.get(i).weatherImage);
        viewHolder.textTime.setText(dayWeather.get(i).timeText);
        viewHolder.textTemp.setText(dayWeather.get(i).tempText);
    }


    @Override
    public int getItemCount() {
        return dayWeather.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageWeather;
        TextView textTemp;
        TextView textTime;

        ViewHolder(View itemView) {
            super(itemView);
            imageWeather = itemView.findViewById(R.id.times_image);
            textTemp = itemView.findViewById(R.id.times_temp);
            textTime = itemView.findViewById(R.id.times_time);
        }
    }
}