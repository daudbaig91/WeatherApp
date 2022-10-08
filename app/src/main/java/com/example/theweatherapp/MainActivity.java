package com.example.theweatherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.defaults.colorpicker.ColorPickerPopup;


public class MainActivity extends AppCompatActivity{

    ImageView background;

    RecyclerView recyclerViewCities;
    private ViewPager viewPager;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TinyDB db = new TinyDB(this);
        String[] nloc = {"51.766604","0.085071"};
        //GetLocation loc = new GetLocation();
        //String[] nloc = loc.GetLocationParam(this,this);
        db.putString("nav", nloc[0] + " " + nloc[1]);



        runFragment();

        recyclerViewCities = findViewById(R.id.citiesrecyclerview);
        recyclerViewCities.setHasFixedSize(true);
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        RelativeLayout main = findViewById(R.id.rlBlurr);


        viewPager = findViewById(R.id.viewpager);
        recyclerViewCities.setAdapter(new citiesListAdapter(MainActivity.this, cities, citiesId, main,viewPager));




        TextView search = findViewById(R.id.city_search);
        RecyclerView rv = findViewById(R.id.citiesrecyclerview);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rv.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.GONE);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.citiesrecyclerview);
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recyclerView
                            .getWindowToken(), 0);
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() >= 3)
                        search(s.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void runFragment() {

        new FragmentLoader(this,0,0);
    }

    public void search(String str) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String placekey = BuildConfig.Places_Api;
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+str +"&types=geocode&types=region&key=" + placekey;

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
                        getcities(myRes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    ArrayList<String> citiesId = new ArrayList<>();
    public ArrayList<String> cities = new ArrayList<>();

    public void getcities(String str) throws JSONException {
        Log.d("ts121",str);
        JSONObject obj= new JSONObject(str);
        JSONArray arr = new JSONArray(obj.getString("predictions"));
        citiesId.clear();
        cities.clear();
        for(int i = 0; i < arr.length(); i++){
            cities.add(((JSONObject)arr.get(i)).getString("description"));
            citiesId.add(((JSONObject)arr.get(i)).getString("place_id"));
        }

        Log.d("tst1",cities.toString());
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                recyclerViewCities.getAdapter().notifyDataSetChanged();
            }
        };
        mainHandler.post(myRunnable);

    }

    public void clicked(View view){

        new HideMenu(this);

    }

    public void opendialog(View view){
        new ColorPickerPopup.Builder(MainActivity.this).initialColor(
            Color.RED).enableBrightness(true)
            .enableAlpha(true).okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true).build()
            .show(view, new ColorPickerPopup.ColorPickerObserver() {
                @Override
                public void
                onColorPicked(int color) {
                    view.setBackgroundColor(color);
                }
            });
    }

    public void imageChooser(View view)
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    public void change(View view)
    {
        TextView tv = findViewById(R.id.citynamedisplay);
        tv.setTextColor(Color.RED);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        ImageView iv = findViewById(R.id.imageviewbackground);
                        iv.setImageBitmap(selectedImageBitmap);

                    }
                }
            });
}