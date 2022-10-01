package com.example.theweatherapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;

public class HideMenu {
    HideMenu(Context context){
        RelativeLayout mainPage = ((Activity)context).findViewById(R.id.rlBlurr);
        NavigationView menu = ((Activity)context).findViewById(R.id.menu);
        if(menu.getVisibility() == View.VISIBLE){
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    menu.animate().translationX(-menu.getWidth());
                    mainPage.animate().translationX(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            menu.setVisibility(View.GONE);
                        }
                    }).start();;
                    mainPage.setVisibility(View.VISIBLE);
                }
            });

        }else {

            menu.animate().translationX(0);
            menu.setVisibility(View.VISIBLE);


            mainPage.animate().translationX(mainPage.getWidth()).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mainPage.setVisibility(View.GONE);
                }
            }).start();
        }
    }
}
