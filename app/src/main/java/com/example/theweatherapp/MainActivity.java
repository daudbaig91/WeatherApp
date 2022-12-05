package com.example.theweatherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
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
    TextView tvProgressLabel;
    SeekBar seekBar;
    Switch mySwitch ;
    Boolean boolSwitch;
    ArrayList<String> citiesId = new ArrayList<>();
    private SharedPreferences prefsw ;
    private SharedPreferences prefs;
    public ArrayList<String> cities = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySwitch = findViewById(R.id.switch3);

        prefs = getSharedPreferences("colorCheck", MODE_PRIVATE); ;
        prefsw = getSharedPreferences("backgroundimage", MODE_PRIVATE);
        if(prefs.contains("mainprimarycolor")){
            changeColor(prefs.getInt("mainprimarycolor",0),"mainprimarycolor");
        }
        if(prefs.contains("mainsecondarycolor")){
            changeColor(prefs.getInt("mainsecondarycolor",0),"mainsecondarycolor");
        }
        if(prefs.contains("Botomprimarycolor")){
            changeColor(prefs.getInt("Botomprimarycolor",0),"Botomprimarycolor");
        }if(prefs.contains("Botomsecondarycolor")){
            changeColor(prefs.getInt("Botomsecondarycolor",0),"Botomsecondarycolor");
        }if(prefsw.contains("image_data")){
            loadImageFromStorage(prefsw.getString("image_data","null"));
            back(prefsw.getString("image_data","null"));
        }



        TinyDB db = new TinyDB(this);
        String[] nloc = {"51.766604","0.085071"};
        //GetLocation loc = new GetLocation();
        //String[] nloc = loc.GetLocationParam(this,this);
        db.putString("nav", nloc[0] + " " + nloc[1]);



        runFragment();
        //reloading - generating fragmetn panels

        recyclerViewCities = findViewById(R.id.citiesrecyclerview);

        recyclerViewCities.setHasFixedSize(true);
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        RelativeLayout main = findViewById(R.id.rlBlurr);

        viewPager = findViewById(R.id.viewpager);
        recyclerViewCities.setAdapter(new citiesListAdapter(MainActivity.this, cities, citiesId, main,viewPager));


        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolSwitch = isChecked;
                if (isChecked){
                    blurr(Float.valueOf(seekBar.getProgress())/4);
                }else {
                    blurr(0f);
                }
                editBlurrDatabase(boolSwitch,Float.valueOf(seekBar.getProgress())/4);
            }
        });


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
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        int progress = seekBar.getProgress();
        tvProgressLabel = findViewById(R.id.textView2);
        tvProgressLabel.setText(progress+"%");

        SharedPreferences editor = getSharedPreferences("backgroundimage", MODE_PRIVATE);
        if(editor.getFloat("blurr",0f)>=1){
            seekBar.setProgress(((int) editor.getFloat("blurr", 0f))*4);
            tvProgressLabel.setText(((int) editor.getFloat("blurr", 0f))*4+"%");
        }
        if (editor.getBoolean("blurrBool",false)){
            mySwitch.setChecked(true);
        }else{
            mySwitch.setChecked(false);
        }

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            tvProgressLabel.setText(progress+"%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
           if (boolSwitch) {
                blurr(Float.valueOf(seekBar.getProgress())/4);
           }
           editBlurrDatabase(boolSwitch,Float.valueOf(seekBar.getProgress())/4);
        }
    };

    void editBlurrDatabase(boolean b, float ft){
        SharedPreferences.Editor editor = getSharedPreferences("backgroundimage", MODE_PRIVATE).edit();
        editor.putFloat("blurr",ft);
        if (b){
            editor.putBoolean("blurrBool",true);
            Toast.makeText(getApplicationContext(),"True Written",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getApplicationContext(),"False Written",Toast.LENGTH_SHORT).show();
            editor.putBoolean("blurrBool",false);
        }
        editor.apply();
    }

    public void blurr(Float rad) {
        rv = findViewById(R.id.blurr2);
        if(rad<1f){
            rv.setBlurEnabled(false);
        }else {


            View decorView = getWindow().getDecorView();
            ViewGroup rootView = (ViewGroup) decorView.findViewById(R.id.rbase);
            rv.setupWith(rootView).setFrameClearDrawable(decorView.getBackground());
            rv.setBlurEnabled(true);
            rv.setBlurRadius(rad);
            rv.setBlurAutoUpdate(true);
        }

    }
    BlurView rv;
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
        String str = getResources().getResourceEntryName(view.getId());
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
                    SharedPreferences.Editor editor = getSharedPreferences("colorCheck", MODE_PRIVATE).edit();

                    if(str.equals("mainprimarycolor")){
                        editor.putInt("mainprimarycolor", color);
                        editor.apply();
                        changeColor(color,"mainprimarycolor");
                        Toast.makeText(getApplicationContext(),"mainprimarycolor written",Toast.LENGTH_SHORT).show();
                    }else if(str.equals("mainsecondarycolor")){
                        editor.putInt("mainsecondarycolor", color);
                        editor.apply();
                        changeColor(color,"mainsecondarycolor");
                        Toast.makeText(getApplicationContext(),"mainsecondarycolor written",Toast.LENGTH_SHORT).show();
                    }else if(str.equals("Botomprimarycolor")){
                        editor.putInt("Botomprimarycolor", color);
                        editor.apply();
                        changeColor(color,"Botomprimarycolor");
                        Toast.makeText(getApplicationContext(),"Botomprimarycolor written",Toast.LENGTH_SHORT).show();
                    }else if(str.equals("Botomsecondarycolor")){
                        editor.putInt("Botomsecondarycolor", color);
                        editor.apply();
                        changeColor(color,"Botomsecondarycolor");
                        Toast.makeText(getApplicationContext(),"Botomsecondarycolor written",Toast.LENGTH_SHORT).show();
                    }
                    runFragment();
                }
            });
    }

    public void changeColor(int col,String check) {


        View view = findViewById(R.id.mainprimarycolor);
        View view2 = findViewById(R.id.mainsecondarycolor);
        View view3 = findViewById(R.id.Botomprimarycolor);
        View view4 = findViewById(R.id.Botomsecondarycolor);



        if(check.equals("mainprimarycolor")){
            view.setBackgroundColor(col);
        }else if(check.equals("mainsecondarycolor")){
            view2.setBackgroundColor(col);
        }else if(check.equals("Botomprimarycolor")) {
            view3.setBackgroundColor(col);
        } else{
            view4.setBackgroundColor(col);
        }
    }

    public void back(String path){
        ImageView view5 = findViewById(R.id.backgroundChangerimage);

        File f=new File(path, "background.jpg");
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapDrawable ob = new BitmapDrawable(getResources(), b);
        view5.setBackground(ob);
    }
    public void imageChooser(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }


    public void reset(View view) {
        SharedPreferences prefsw = getSharedPreferences("backgroundimage", MODE_PRIVATE);
        SharedPreferences prefs = getSharedPreferences("colorCheck", MODE_PRIVATE);

        View view1 = findViewById(R.id.mainprimarycolor);
        View view2 = findViewById(R.id.mainsecondarycolor);
        View view3 = findViewById(R.id.Botomprimarycolor);
        View view4 = findViewById(R.id.Botomsecondarycolor);

        prefsw.edit().clear().commit();
        prefs.edit().clear().commit();

        view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        view2.setBackgroundColor(Color.parseColor("#cdc0c0"));
        view3.setBackgroundColor(Color.parseColor("#914F4F"));
        view4.setBackgroundColor(Color.parseColor("#FFFFFF"));
        RelativeLayout img=findViewById(R.id.rbase);
        img.setBackgroundColor(0);
        ImageView view5 = findViewById(R.id.backgroundChangerimage);
        view5.setBackgroundResource(R.drawable.back1);
        runFragment();
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

                        RelativeLayout img=findViewById(R.id.rbase);
                        img.setBackground(new BitmapDrawable(getResources(), selectedImageBitmap));
                        findViewById(R.id.backgroundChangerimage).setBackground(new BitmapDrawable(getResources(), selectedImageBitmap));;


                        SharedPreferences.Editor editor = getSharedPreferences("backgroundimage", MODE_PRIVATE).edit();
                            editor.putString("image_data",saveToInternalStorage(selectedImageBitmap));
                            editor.apply();

                    }
                }
            });

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"background.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private void loadImageFromStorage(String path){
        try {
            File f=new File(path, "background.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            RelativeLayout img= findViewById(R.id.rbase);
            BitmapDrawable ob = new BitmapDrawable(getResources(), b);
            img.setBackground(ob);



        }
        catch (FileNotFoundException e)
        {
           Log.d("strcheck",e.toString());
            e.printStackTrace();
        }
    }
}