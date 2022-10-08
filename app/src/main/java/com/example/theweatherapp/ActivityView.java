package com.example.theweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.
        String[] param = value.split(" ");
        String weatherKey = BuildConfig.Weather_Api;
        String urlWeather = "https://api.weatherapi.com/v1/forecast.json?key="+ weatherKey+"&q="
                +param[0]+","+param[1]+"&days=14&aqi=no&alerts=no";
        OkHttpHandler client = new OkHttpHandler();

        RelativeLayout rl = findViewById(R.id.rlBlurr);
        client.doInBackground2(urlWeather,rl,this,0);

        ImageView iv = findViewById(R.id.imageView);
        iv.setVisibility(View.GONE);
        ImageView ivclose = findViewById(R.id.deletefrag);
        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}