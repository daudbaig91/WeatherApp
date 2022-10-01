package com.example.theweatherapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.ArrayList;


class WeekWeatherAdapter extends RecyclerView.Adapter <WeekWeatherAdapter.ViewHolder>{

    private final ArrayList<WeekWeather> weekWeather;
    private Context context;
    WeatherCode weatherCode;

    View vg;

    WeekWeatherAdapter(Context context, ArrayList<WeekWeather> weekWeather,View view) {
        super();
        this.weatherCode = new WeatherCode();
        try {
            weatherCode.create(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.context = context;
        this.weekWeather = weekWeather;
        this.vg = view;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_days, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        ImageView image = viewHolder.imageWeather;

        String strImage = weatherCode.getCode(String.valueOf(weekWeather.get(i).weatherImage));

        Glide.with(context)
                .load("https://openweathermap.org/img/wn/"+ strImage + "d@4x.png")
                .error(R.drawable.navigation)
                .into(image);

        viewHolder.dayOfWeek.setText(weekWeather.get(i).daysWeather);
        viewHolder.porcentageRain.setText(weekWeather.get(i).porcentageRain);
        viewHolder.upTemp.setText(weekWeather.get(i).maxTemp + "\u00B0");
        viewHolder.lowTemp.setText(weekWeather.get(i).minTemp + "\u00B0");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread one = new Thread() {
                    public void run() {
                        try {

                            v.setBackgroundColor(Color.parseColor("#b0d1d9"));
                            Thread.sleep(220);
                            v.setBackgroundColor(Color.TRANSPARENT);

                        } catch(InterruptedException v) {
                            System.out.println(v);
                        }
                    }
                };
                one.start();

                new LineChart(vg,weekWeather.get(viewHolder.getAdapterPosition()).hour);



            }
        });


    }


    @Override
    public int getItemCount() {
        return weekWeather.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageWeather;
        TextView dayOfWeek;
        TextView porcentageRain;
        TextView upTemp;
        TextView lowTemp;

        ViewHolder(View itemView) {
            super(itemView);
            imageWeather = itemView.findViewById(R.id.weather_daysImage);
            dayOfWeek = itemView.findViewById(R.id.days_weather);
            porcentageRain = itemView.findViewById(R.id.porcentage_id);
            upTemp = itemView.findViewById(R.id.uptemp);
            lowTemp = itemView.findViewById(R.id.lowtemp);
        }
    }
}