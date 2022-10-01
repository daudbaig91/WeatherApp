package com.example.theweatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Map;

public class FragmentLoader {

    FragmentLoader(Context context,int index,int color){
        ViewPager viewPager = ((Activity)context).findViewById(R.id.viewpager);

        TinyDB db = new TinyDB(context);
        ArrayList<String> Urls = db.getListString("Data");
        Urls.add(0,db.getString("nav"));

        viewPager.setOffscreenPageLimit(5);
        DynamicFragmentAdapter mDynamicFragmentAdapter =
                new DynamicFragmentAdapter(((FragmentActivity)((Activity)context)).getSupportFragmentManager(), Urls.size(),Urls);

        // set the adapter

        viewPager.setAdapter(mDynamicFragmentAdapter);

        // set the current item as 0 (when app opens for first time)
        viewPager.setCurrentItem(index);
    }

}
