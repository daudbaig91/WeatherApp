package com.example.theweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;
import okhttp3.OkHttpClient;

public class  DynamicFragment extends Fragment {



    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    String[] param = {"51.507351","-0.127758"};
    int position;
    // adding the layout with inflater

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.param = (getArguments().getString("url")).split(" ");

        this.position = (getArguments().getInt("position"));


        View view = inflater.inflate(R.layout.fragment_content, container, false);
        changeColor(view);
        if(position == 0){
            ImageView deletebttn = view.findViewById(R.id.deletefrag);
            deletebttn.setVisibility(View.GONE);
        }
        initViews(view);

        return view;
    }
    String url1 = "";
    // initialise the categories
    private void initViews(View view) {




        Log.d("urlTest",url1);
        //TextView textView = view.findViewById(R.id.commonTextView);
        //textView.setText(String.valueOf("Category :  " + getArguments().getInt("position")));

        RelativeLayout tl = view.findViewById(R.id.bendview);
        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidebar(view);
            }
        });

        ImageView iv = view.findViewById(R.id.deletefrag);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB db = new TinyDB(getContext());
                ArrayList<String> list = db.getListString("Data");
                list.remove(position-1);
                db.putListString("Data", list);
                new FragmentLoader(getContext(),0,0);

            }
        });


        GetLocation gl = new GetLocation();

        //param = gl.GetLocationParam(this, );


        String urlWeather = "https://api.weatherapi.com/v1/forecast.json?key=" + BuildConfig.Weather_Api+"&q="
                +param[0]+","+param[1]+"&days=14&aqi=no&alerts=no";
        OkHttpHandler client = new OkHttpHandler();

        client.doInBackground2(urlWeather,view,getContext(),position);

    }

    public void changeColor(View view) {

        SharedPreferences prefs = this.getContext().getSharedPreferences("colorCheck", this.getContext().MODE_PRIVATE);
        TextView tv1 = view.findViewById(R.id.date_text);
        TextView tv2 = view.findViewById(R.id.degreestextView);
        TextView tv3 = view.findViewById(R.id.weatherdescp);
        View view2 = view.findViewById(R.id.mainsecondarycolor);
        TextView tv4 = view.findViewById(R.id.lowdeg);
        TextView tv5 = view.findViewById(R.id.highdeg);
        TextView tv6 = view.findViewById(R.id.feelslikedesc);
        TextView tv7 = view.findViewById(R.id.citynamedisplay);
        if (prefs.contains("mainprimarycolor")) {
            int col = prefs.getInt("mainprimarycolor", 0);

            tv1.setTextColor(col);
            tv2.setTextColor(col);
            tv3.setTextColor(col);
        }
        if (prefs.contains("mainsecondarycolor")) {
            int col2 = prefs.getInt("mainsecondarycolor", 0);

            tv4.setTextColor(col2);
            tv5.setTextColor(col2);
            tv6.setTextColor(col2);
            tv7.setTextColor(col2);
        }
    }


    public void hidebar(View view) {
        View v = view.findViewById(R.id.textView);
        LinearLayout l = view.findViewById(R.id.daytimes);
        RelativeLayout rl = view.findViewById(R.id.rL);

        if (l.getVisibility() == View.VISIBLE) {

            l.setVisibility(View.GONE);
            v.setVisibility(View.GONE);

        } else {

            l.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // pause function call
    @Override
    public void onPause() {
        super.onPause();
    }

    // resume function call
    @Override
    public void onResume() {
        super.onResume();
    }

    // stop when we close
    @Override
    public void onStop() {
        super.onStop();
    }

    // destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}





