package com.example.theweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonParser {

    ArrayList<WeekWeather> listWeatherHour = new ArrayList<>();
    WeatherData dataWeather = new WeatherData();
    WeatherCode weatherCode;
    Context context;


    JsonParser(String myRes, View view, Context context,int positon) throws JSONException, ParseException {



        this.context = context;
        this.weatherCode = new WeatherCode();
        try {
            weatherCode.create(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonLoadDate(myRes,view);
        jsonParserHourly(myRes,view,context);
        jsonParserWeather(myRes,view,positon);


    }

    public void JsonLoadDate(String resp,View view) throws JSONException, ParseException {

        Log.d("tst11" , resp.toString());
        String time = (new JSONObject(resp)).getJSONObject("location")
                .getString("localtime");

        dataWeather.DateConverter(time.substring(0,10));

        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                TextView timeText = view.findViewById(R.id.date_text);
                timeText.setText(dataWeather.date);

            }
        };
        mainHandler.post(myRunnable);

    }

    public void jsonParserHourly(String res, View view, Context context) throws JSONException, ParseException {

        JSONObject obj = new JSONObject(res);
        JSONArray datearr = (obj.getJSONObject("forecast")).getJSONArray("forecastday");

        for (int i = 0; i < datearr.length();i++) {
            WeekWeather hw = new WeekWeather();

            JSONObject dayarr = ((JSONObject)datearr.get(i)).getJSONObject("day");

            hw.x = i;
            hw.weatherImage = Integer.valueOf(dayarr.getJSONObject("condition").getString("code"));
            hw.maxTemp = dayarr.getString("maxtemp_c");
            hw.minTemp = dayarr.getString("mintemp_c");
            hw.porcentageRain=dayarr.getString("daily_chance_of_rain");
            hw.DatetoDay(((JSONObject)datearr.get(i)).getString("date"));



            JSONArray hourarr = ((JSONObject)datearr.get(i)).getJSONArray("hour");
            for (int y = 0; y < hourarr.length();y++){

                hw.hour.add(new Entry((float)y,
                        Float.parseFloat(((JSONObject)hourarr.get(y)).getString("temp_c"))));
            }
            listWeatherHour.add(hw);
        }

        Handler mainHandler3 = new Handler(Looper.getMainLooper());
        Runnable myRunnable3 = new Runnable() {
            @Override
            public void run() {

                new LineChart(view,listWeatherHour.get(0).hour);
                new WeekListUpdater(view,context,listWeatherHour);

            }
        };
        mainHandler3.post(myRunnable3);


    }

    public void jsonParserWeather(String resp,View view,int position) throws JSONException {

        JSONObject obj = new JSONObject(resp);

        JSONArray arr = obj.getJSONObject("forecast").getJSONArray("forecastday");

        Log.d("sas",arr.toString());
        JSONObject objday = ((JSONObject)arr.get(0)).getJSONObject("day");


        dataWeather.weatherCode = Integer.valueOf(objday.getJSONObject("condition").getString("code")).toString();
        dataWeather.city = (obj.getJSONObject("location")).getString("name");
        dataWeather.temperature = obj.getJSONObject("current").getString("temp_c");
        dataWeather.weather = ((JSONObject)arr.get(0)).getJSONObject("day").getJSONObject("condition").getString("text");
        dataWeather.feelLike =  obj.getJSONObject("current").getString("feelslike_c");
        dataWeather.minTemp = objday.getString("mintemp_c");
        dataWeather.maxTemp = objday.getString("maxtemp_c");
        dataWeather.humidty = obj.getJSONObject("current").getString("humidity");
        dataWeather.windspeed = obj.getJSONObject("current").getString("wind_mph");
        dataWeather.chancerain = objday.getString("daily_chance_of_rain");

        dataWeather.converData();
        //running on main Therad;;

        Handler mainHandler2 = new Handler(Looper.getMainLooper());
        Runnable myRunnable2 = new Runnable() {
            @Override
            public void run() {

                TextView city = view.findViewById(R.id.citynamedisplay);
                TextView degrees = view.findViewById(R.id.degreestextView);
                TextView lowDeg = view.findViewById(R.id.lowdeg);
                TextView highDeg = view.findViewById(R.id.highdeg);
                TextView weatherDesc = view.findViewById(R.id.weatherdescp);
                TextView feelsLikeDesc = view.findViewById(R.id.feelslikedesc);
                TextView Humidity = view.findViewById(R.id.humidity);
                TextView rainChance = view.findViewById(R.id.rainchance);
                TextView windSpeed = view.findViewById(R.id.windspeed);

                TinyDB db = new TinyDB(context
                );
               int index = db.getListString("Data").size()+1;

                LinearLayout ln = view.findViewById(R.id.progressbar);
                ImageView ivcopy = view.findViewById(R.id.dot4);
                android.view.ViewGroup.LayoutParams layoutParams = ivcopy.getLayoutParams();
                layoutParams.width = dpToPx(10,context);
                layoutParams.height = dpToPx(10,context);
                if(position==0){
                    ivcopy.setImageResource(R.drawable.dots);;

                }
                    for (int i = 0; i < index - 1; i++) {
                        ImageView iv = new ImageView(context);
                        iv.setLayoutParams(layoutParams);
                        if (position == (i + 1)) {
                            iv.setImageResource(R.drawable.dots);
                        } else {
                            iv.setImageResource(R.drawable.dotn);
                        }
                        ln.addView(iv);
                    }
                Log.d("tst43321",String.valueOf( position));
                LinearLayout linearLayout = view.findViewById(R.id.progressbar);

                ImageView image = view.findViewById(R.id.weathersign);
                String strImage = weatherCode.getCode(dataWeather.weatherCode);
                Glide.with(context)
                        .load("https://openweathermap.org/img/wn/"+ strImage + "d@4x.png")
                        .error(R.drawable.navigation)
                        .into(image);
                city.setText(dataWeather.city);
                degrees.setText(dataWeather.temperature + "\u00B0");
                lowDeg.setText(dataWeather.minTemp + "\u00B0");
                highDeg.setText(dataWeather.maxTemp + "\u00B0");
                weatherDesc.setText(dataWeather.weather);
                feelsLikeDesc.setText("Feels like " + dataWeather.feelLike + "\u00B0");
                Humidity.setText("Humidity: "+dataWeather.humidty +"%");
                windSpeed.setText("Wind Speed: "+dataWeather.windspeed +"m/h");
                rainChance.setText("Chance of rain is : "+dataWeather.chancerain +"%");
            }
        };
        mainHandler2.post(myRunnable2);
    }
    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
