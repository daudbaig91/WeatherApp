package com.example.theweatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


class citiesListAdapter extends RecyclerView.Adapter <citiesListAdapter.ViewHolder>{

    private final ArrayList<String> cityId;
    private final ArrayList<String> locationCity;
    private final Context context;
    RelativeLayout rv;
    ViewPager vp;
    citiesListAdapter(Context context, ArrayList<String> weekWeather, ArrayList<String>  cityId, RelativeLayout rv,
                      ViewPager viewPager) {
        super();

        this.context = context;
        this.locationCity = weekWeather;
        this.cityId = cityId;
        this.rv = rv;
        vp = viewPager;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_locations, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.location.setText(locationCity.get(i));




        viewHolder.imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                try {
                    getCordinates(cityId.get(viewHolder.getAdapterPosition()),true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        viewHolder.viewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();

                try {
                    getCordinates(cityId.get(viewHolder.getAdapterPosition()),false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (lat != null) {


                    Intent myIntent = new Intent(context, ActivityView.class);
                    myIntent.putExtra("key", lat + " " + lon); //Optional parameters
                    context.startActivity(myIntent);
                }



            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = cityId.get(viewHolder.getAdapterPosition());

                hidekeyboard();
            }
        });

    }

    public void getCordinates(String s,Boolean bool) throws JSONException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + s +"&key=AIzaSyBUH6Bu-8nueKwtTqsWnepO01mnKw6jOj4";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {

                if (response.isSuccessful()) {
                    String myRes = response.body().string();

                    try {

                        Jsongetlen(myRes,bool);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public void hidekeyboard(){
        View view = ((Activity)context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager)((context).getSystemService(Context.INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    String lon;
    String lat;
    public String Jsongetlen(String str,Boolean bool) throws JSONException {
        JSONObject obj = new JSONObject(str);
        JSONObject newobj = obj.getJSONObject("result").getJSONObject("geometry")
                                .getJSONObject("location");

         lat = newobj.getString("lat");
         lon = newobj.getString("lng");


        if(bool) {
            TinyDB db = new TinyDB(context);

            ArrayList<String> s = db.getListString("Data");


            s.add(lat + " " + lon);
            db.putListString("Data", s);

            //vp.setCurrentItem(index);


            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    new FragmentLoader(context, s.size(), 0);

                }
            };
            mainHandler.post(myRunnable);
            hidekeyboard();
            new HideMenu(context);
//        OkHttpHandler client = new OkHttpHandler();
//        client.doInBackground2(urlHourlyWeather,rv,context);
        }
        return  null;
    }

    @Override
    public int getItemCount() {
        return locationCity.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        ImageView imageadd;
        TextView viewView;
        ViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.city_location);
            imageadd = itemView.findViewById(R.id.addbutton);
            viewView = itemView.findViewById(R.id.viewbutton);
        }
    }


}