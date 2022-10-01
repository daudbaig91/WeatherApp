package com.example.theweatherapp;


import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    ArrayList<String> list;
    DynamicFragmentAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String > list) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.list = list;
    }

    // get the current item with position number
    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        Log.d("test11",list.get(position));
        b.putString("url", list.get(position));
        Fragment frag = DynamicFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }

    // get total number of tabs
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}